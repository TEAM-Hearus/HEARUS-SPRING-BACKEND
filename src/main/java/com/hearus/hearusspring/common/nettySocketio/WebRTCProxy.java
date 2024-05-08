package com.hearus.hearusspring.common.nettySocketio;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final SocketIOServer server;
    private final SocketIONamespace namespace;
    private WebSocket fastAPIWebSocket;
    private final ScheduledExecutorService reconnectExecutor;

    @Autowired
    public WebRTCProxy(SocketIOServer server) {
        this.server = server;
        this.namespace = server.addNamespace("/webrtc");
        this.namespace.addEventListener("transcription", byte[].class, audioListener());
        this.reconnectExecutor = Executors.newSingleThreadScheduledExecutor();

        connectToFastAPI();
    }

    private DataListener<byte[]> audioListener() {
        return (client, audioData, ackSender) -> {
            if (fastAPIWebSocket != null && !fastAPIWebSocket.isInputClosed()) {
                log.info("[WebRTCProxy] Forwarding audio data to FastAPI server");
                fastAPIWebSocket.sendBinary(ByteBuffer.wrap(audioData), true);
            } else {
                log.error("[WebRTCProxy] FastAPI WebSocket is not connected");
            }
        };
    }

    private void connectToFastAPI() {
        HttpClient client = HttpClient.newHttpClient();
        CompletionStage<WebSocket> webSocketCompletionStage = client.newWebSocketBuilder()
                .buildAsync(URI.create("ws://your-fastapi-server.com/websocket"), new WebSocketListener());

        webSocketCompletionStage.thenAccept(webSocket -> {
            fastAPIWebSocket = webSocket;
            log.info("[WebRTCProxy] Connected to FastAPI server");
        }).exceptionally(ex -> {
            log.error("[WebRTCProxy] Failed to connect to FastAPI server", ex);
            reconnectToFastAPI();
            return null;
        });
    }

    private void reconnectToFastAPI() {
        reconnectExecutor.schedule(() -> {
            connectToFastAPI();
        }, 5, TimeUnit.SECONDS);
    }

    private class WebSocketListener implements WebSocket.Listener {
        @Override
        public void onOpen(WebSocket webSocket) {
            webSocket.sendText("Connected from Spring Server", true);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            log.info("[WebRTCProxy] Received text from FastAPI server: {}", data);
            namespace.getBroadcastOperations().sendEvent("sttResult", data.toString());
            return null;
        }

        @Override
        public CompletionStage<?> onPing(WebSocket webSocket, ByteBuffer message) {
            webSocket.request(1);
            return null;
        }

        @Override
        public CompletionStage<?> onPong(WebSocket webSocket, ByteBuffer message) {
            webSocket.request(1);
            return null;
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            log.warn("[WebRTCProxy] WebSocket connection to FastAPI server closed: {} - {}", statusCode, reason);
            return null;
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            log.error("[WebRTCProxy] WebSocket error from FastAPI server", error);
        }
    }
}