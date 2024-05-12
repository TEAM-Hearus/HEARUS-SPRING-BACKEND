package com.hearus.hearusspring.webrtcProxy;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

@Slf4j
public class WebSocketUtil extends WebSocketClient {
    private final SocketIOClient socketIOClient;
    public WebSocketUtil(URI serverUri, Draft protocolDraft, SocketIOClient socketIOClient) {
        super(serverUri, protocolDraft);
        this.socketIOClient = socketIOClient;
    }

    @Override
    public void onMessage(String message) {
        log.info("[WebSocketUtil] Received Messegae {}", message);
        socketIOClient.sendEvent("transitionResult", message);
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