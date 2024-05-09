package com.hearus.hearusspring.common.nettySocketio;

import com.corundumstudio.socketio.SocketIONamespace;
import lombok.extern.slf4j.Slf4j;

import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletionStage;

@Slf4j
public class WebSocketListener implements WebSocket.Listener {
    private final SocketIONamespace namespace;

    public WebSocketListener(SocketIONamespace namespace) {
        this.namespace = namespace;
    }

    @Override
    public void onOpen(WebSocket webSocket) {
        webSocket.sendText("Spring Server", true);
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        log.info("[WebSocketListener] Received text from FastAPI server: {}", data);
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
        log.warn("[WebSocketListener] WebSocket connection to FastAPI server closed: {} - {}", statusCode, reason);
        return null;
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        log.error("[WebSocketListener] WebSocket error from FastAPI server", error);
    }
}