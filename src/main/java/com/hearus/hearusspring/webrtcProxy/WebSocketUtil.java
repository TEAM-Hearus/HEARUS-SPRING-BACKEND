package com.hearus.hearusspring.webrtcProxy;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.hearus.hearusspring.data.dao.LectureDAO;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;

@Slf4j
public class WebSocketUtil extends WebSocketClient {

    private final LectureDAO lectureDAO;
    private final SocketIOClient socketIOClient;
    private final String lectureId;
    public WebSocketUtil(LectureDAO lectureDAO, URI serverUri, Draft protocolDraft, SocketIOClient socketIOClient, String lectureId) {
        super(serverUri, protocolDraft);
        this.lectureDAO = lectureDAO;
        this.socketIOClient = socketIOClient;
        this.lectureId = lectureId;
    }

    @Override
    public void onMessage(String message) {
        log.info("[WebSocketUtil]-[onMessage] Received Messegae {}", message);

        try {
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();

            String transcritionResult = jsonObject.get("transcritionResult").getAsString();

            socketIOClient.sendEvent("transitionResult", transcritionResult);
            // lectureDAO.putScript(lectureId, transcritionResult);
        } catch (JsonSyntaxException e) {
            log.error("[WebSocketUtil]-[onMessage] Failed to parse JSON message: {}", message, e);
        } catch (Exception e) {
            log.error("[WebSocketUtil]-[onMessage] Exception : {}", message, e);
        }
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