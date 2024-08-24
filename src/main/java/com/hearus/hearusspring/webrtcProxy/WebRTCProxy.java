package com.hearus.hearusspring.webrtcProxy;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.hearus.hearusspring.common.environment.ConfigUtil;
import com.hearus.hearusspring.common.ffmpeg.AudioConverter;
import com.hearus.hearusspring.common.ffmpeg.FFmpegConfig;
import com.hearus.hearusspring.data.dao.LectureDAO;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.drafts.Draft_6455;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class WebRTCProxy {

    @Autowired
    private LectureDAO lectureDAO;

    @Autowired
    private FFmpegConfig fFmpegConfig;

    private final String FastAPIEndpoint;
    private final SocketIOServer server;
    private final SocketIONamespace namespace;
    private WebSocketUtil fastAPIWebSocket;
    private final AudioConverter audioConverter;
    private Timer timer;
    private String lectureId;

    @Autowired
    public WebRTCProxy(SocketIOServer server, ConfigUtil configUtil) {
        this.server = server;
        this.FastAPIEndpoint = configUtil.getProperty("FAST_API_WS_ENDPOINT");
        this.namespace = server.addNamespace("/webrtc");
        this.namespace.addConnectListener(onConnected());
        this.namespace.addDisconnectListener(onDisconnected());
        this.namespace.addEventListener("transcription", String.class, audioListener());
        this.namespace.addEventListener("lectureId", String.class, lectureIdListener());
        this.audioConverter = new AudioConverter(fFmpegConfig);
    }

    // WebSocket
    private void connectFastAPI(Timer timer, SocketIOClient client){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                URI fastAPIWSURI = null;
                try {
                    fastAPIWSURI  = new URI(FastAPIEndpoint + "/ws");
                    if(fastAPIWebSocket == null || fastAPIWebSocket.isClosed()) {
                        fastAPIWebSocket = new WebSocketUtil(
                                lectureDAO,
                                fastAPIWSURI,
                                new Draft_6455(),
                                client,
                                lectureId
                        );
                        fastAPIWebSocket.connectBlocking();
                        fastAPIWebSocket.send(String.valueOf(UUID.randomUUID()));
                    }
                } catch (InterruptedException e) {
                    log.error("[WebRTCProxy]-[connectFastAPI] WebSocket connection interrupted: ", e);
                }
                catch (Exception e) {
                    log.error("[WebRTCProxy]-[connectFastAPI] WebSocket connection failed: ", e);
                    log.info("[WebRTCProxy]-[connectFastAPI] {}", fastAPIWSURI);
                }
            }
        }, 0, 60);
    }

    // Socketio Listeners
    private ConnectListener onConnected() {
        return client -> {
            HandshakeData handshakeData = client.getHandshakeData();
            log.info("[WebRTCProxy]-[Socketio]-[{}] Connected to WebRTCProxy Socketio through '{}'", client.getSessionId().toString(), handshakeData.getUrl());
            timer = new Timer();
            connectFastAPI(timer, client);
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            log.info("[WebRTCProxy]-[Socketio]-[{}] Disconnected from WebRTCProxy Socketio Module.", client.getSessionId().toString());
            if(timer != null) {
                timer.cancel();
                timer.purge();
            }

            if(fastAPIWebSocket != null)
                fastAPIWebSocket.close();
        };
    }

    private DataListener<String> lectureIdListener() {
        return (client, lectureId, ackSender) -> {
            log.info("[WebRTCProxy]-[Socketio]-[{}] Received LectureID: {}", client.getSessionId().toString(), lectureId);
            this.lectureId = lectureId;
        };
    }

    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private DataListener<String> audioListener() {
        return (client, audioData, ackSender) -> {
            if (audioData == null) {
                log.error("[WebRTCProxy]-[Socketio] Audio data is null");
                return;
            }

            CompletableFuture.supplyAsync(() -> {
                try {
                    // Decode Base64 string to byte array
                    byte[] decodedBytes = Base64.getDecoder().decode(audioData);
                    // Convert to codec : pcm_s16le, format : s16le
                    return audioConverter.convertAudio(decodedBytes);
                } catch (Exception ex) {
                    log.error("[WebRTCProxy]-[Socketio] Exception during audio conversion", ex);
                    throw new RuntimeException(ex);
                }
            }, executorService).thenAccept(convertedBytes -> {
                try {
                    if (fastAPIWebSocket != null && fastAPIWebSocket.isOpen()) {
                        // Wrap converted bytes in ByteBuffer
                        ByteBuffer byteBuffer = ByteBuffer.wrap(convertedBytes);

                        //log.info("[WebRTCProxy]-[Socketio] Forwarding audio data to FastAPI server [{}]", convertedBytes.length);

                        // Send binary data using fastAPISession
                        fastAPIWebSocket.send(byteBuffer);
                    } else {
                        log.error("[WebRTCProxy]-[Socketio] FastAPI WebSocket is not connected");
                    }
                } catch (Exception ex) {
                    log.error("[WebRTCProxy]-[Socketio] Exception during WebSocket send", ex);
                }
            }).exceptionally(ex -> {
                log.error("[WebRTCProxy]-[Socketio] Unhandled exception in audio processing", ex);
                return null;
            });
        };
    }
}