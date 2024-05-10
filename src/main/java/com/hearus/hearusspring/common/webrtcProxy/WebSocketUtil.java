package com.hearus.hearusspring.common.webrtcProxy;

import com.corundumstudio.socketio.SocketIONamespace;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

@Slf4j
public class WebSocketUtil extends WebSocketClient {
    private final SocketIONamespace namespace;
    public WebSocketUtil(URI serverUri, Draft protocolDraft, SocketIONamespace namespace) {
        super(serverUri, protocolDraft);
        this.namespace = namespace;
    }

    @Override
    public void onMessage(String message) {
        log.info("[WebSocketUtil] Received Messegae {}", message);
    }

    @Override
    public void onOpen( ServerHandshake handshake ) {
        log.info("[WebSocketUtil] WebSocket connection Opened");
    }

    @Override
    public void onClose( int code, String reason, boolean remote ) {
        log.info("[WebSocketUtil] WebSocket connection Closed");
    }

    @Override
    public void onError( Exception ex ) {
        ex.printStackTrace();
    }

}