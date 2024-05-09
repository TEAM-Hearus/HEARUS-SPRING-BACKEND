package com.hearus.hearusspring.common.nettySocketio;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.hearus.hearusspring.common.environment.ConfigUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class WebRTCProxy {

    private final String FastAPIEndpoint;
    private final SocketIOServer server;
    private final SocketIONamespace namespace;
    private WebSocket fastAPIWebSocket;
    private final ScheduledExecutorService reconnectExecutor;

    @Autowired
    public WebRTCProxy(SocketIOServer server, ConfigUtil configUtil) {
        this.server = server;
        this.FastAPIEndpoint = configUtil.getProperty("FAST_API_ENDPOINT");
        this.namespace = server.addNamespace("/webrtc");
        this.namespace.addConnectListener(onConnected());
        this.namespace.addDisconnectListener(onDisconnected());
        this.namespace.addEventListener("transcription", byte[].class, audioListener());
        this.reconnectExecutor = Executors.newSingleThreadScheduledExecutor();

        connectToFastAPI();
    }

    private ConnectListener onConnected() {
        return client -> {
            HandshakeData handshakeData = client.getHandshakeData();
            log.info("[WebRTCProxy]-[onConnected]-[{}] Connected to WebRTCProxy Socketio through '{}'", client.getSessionId().toString(), handshakeData.getUrl());
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            log.info("[WebRTCProxy]-[onDisconnected]-[{}] Disconnected from WebRTCProxy Socketio Module.", client.getSessionId().toString());
        };
    }

    private DataListener<byte[]> audioListener() {
        return (client, audioData, ackSender) -> {
            try {
                if (fastAPIWebSocket != null && !fastAPIWebSocket.isInputClosed()) {
                    log.info("[WebRTCProxy]-[audioListener] Forwarding audio data to FastAPI server");
                    fastAPIWebSocket.sendBinary(ByteBuffer.wrap(audioData), true);
                } else {
                    log.error("[WebRTCProxy]-[audioListener] FastAPI WebSocket is not connected");
                }
            } catch(Exception ex){
                log.error("[WebRTCProxy]-[audioListener] Invalid Audio Data");
                ex.printStackTrace();
            }
        };
    }

    private void connectToFastAPI() {
        HttpClient client = HttpClient.newHttpClient();
        CompletionStage<WebSocket> webSocketCompletionStage = client.newWebSocketBuilder()
                .buildAsync(URI.create(FastAPIEndpoint + "/ws"), new WebSocketListener(namespace));

        webSocketCompletionStage.thenAccept(webSocket -> {
            fastAPIWebSocket = webSocket;
            log.info("[WebRTCProxy] Connected to FastAPI server");
        }).exceptionally(ex -> {
            log.error("[WebRTCProxy] Failed to connect to FastAPI server ", ex);
            reconnectToFastAPI();
            return null;
        });
    }

    private void reconnectToFastAPI() {
        reconnectExecutor.schedule(() -> {
            connectToFastAPI();
        }, 5, TimeUnit.SECONDS);
    }
}