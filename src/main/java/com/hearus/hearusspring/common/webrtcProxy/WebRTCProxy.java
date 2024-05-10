package com.hearus.hearusspring.common.webrtcProxy;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.hearus.hearusspring.common.environment.ConfigUtil;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.drafts.Draft_6455;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.*;

@Slf4j
@Component
public class WebRTCProxy {

    private final String FastAPIEndpoint;
    private final SocketIOServer server;
    private final SocketIONamespace namespace;
    private WebSocketUtil fastAPIWebSocket;

    @Autowired
    public WebRTCProxy(SocketIOServer server, ConfigUtil configUtil) {
        this.server = server;
        this.FastAPIEndpoint = configUtil.getProperty("FAST_API_ENDPOINT");
        this.namespace = server.addNamespace("/webrtc");
        this.namespace.addConnectListener(onConnected());
        this.namespace.addDisconnectListener(onDisconnected());
        this.namespace.addEventListener("transcription", String.class, audioListener());
        connectFastAPI();
    }

    // WebSocket
    private void connectFastAPI(){
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    if(fastAPIWebSocket == null || fastAPIWebSocket.isClosed()) {
                        fastAPIWebSocket = new WebSocketUtil(
                                new URI(FastAPIEndpoint + "/ws"),
                                new Draft_6455(),
                                namespace
                        );
                        fastAPIWebSocket.connectBlocking();
                        fastAPIWebSocket.send("Spring Server");
                    }
                } catch (Exception e) {
                    // Handle connection exceptions
                    e.printStackTrace();
                }
            }
        }, 0, 60);
    }

    // Socketio Listeners
    private ConnectListener onConnected() {
        return client -> {
            HandshakeData handshakeData = client.getHandshakeData();
            log.info("[WebRTCProxy]-[Socketio]-[{}] Connected to WebRTCProxy Socketio through '{}'", client.getSessionId().toString(), handshakeData.getUrl());
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            log.info("[WebRTCProxy]-[Socketio]-[{}] Disconnected from WebRTCProxy Socketio Module.", client.getSessionId().toString());
        };
    }

    private DataListener<String> audioListener() {
        return (client, audioData, ackSender) -> {
            try {
                if (audioData == null) {
                    log.error("[WebRTCProxy]-[Socketio] Audio data is null");
                    return;
                }

                if (fastAPIWebSocket != null && fastAPIWebSocket.isOpen()) {
                    // Decode Base64 string to byte array
                    byte[] decodedBytes = Base64.getDecoder().decode(audioData);

                    // Wrap decoded bytes in ByteBuffer
                    ByteBuffer byteBuffer = ByteBuffer.wrap(decodedBytes);

                    log.info("[WebRTCProxy]-[Socketio] Forwarding audio data to FastAPI server [{}]", decodedBytes.length);

                    // Send binary data using fastAPISession
                    fastAPIWebSocket.send(byteBuffer);
                } else {
                    log.error("[WebRTCProxy]-[Socketio] FastAPI WebSocket is not connected");
                }
            } catch (Exception ex) {
                log.error("[WebRTCProxy]-[Socketio] Exception", ex);
            }
        };
    }
}