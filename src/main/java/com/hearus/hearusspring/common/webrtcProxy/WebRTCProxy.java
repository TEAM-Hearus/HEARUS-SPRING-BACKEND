package com.hearus.hearusspring.common.webrtcProxy;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.hearus.hearusspring.common.environment.ConfigUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Base64;

@Slf4j
@Component
public class WebRTCProxy {

    private final String FastAPIEndpoint;
    private final SocketIOServer server;
    private final SocketIONamespace namespace;
    private WebSocketSession fastAPIWebSocketSession;

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

    // TODO : Connect WebSocket to FastAPI
    private void connectFastAPI(){
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

                if (fastAPIWebSocketSession != null && fastAPIWebSocketSession.isOpen()) {
                    // Decode Base64 string to byte array
                    byte[] decodedBytes = Base64.getDecoder().decode(audioData);

                    // Wrap decoded bytes in ByteBuffer
                    ByteBuffer byteBuffer = ByteBuffer.wrap(decodedBytes);

                    log.info("[WebRTCProxy]-[Socketio] Forwarding audio data to FastAPI server [{}]", decodedBytes.length);
                    // Send binary data using fastAPIWebSocketSession
                    //fastAPIWebSocketSession.sendMessage(new ByteBufferMessageConverter().toMessage(byteBuffer, null));
                } else {
                    log.error("[WebRTCProxy]-[Socketio] FastAPI WebSocket is not connected");
                }
            } catch (Exception ex) {
                log.error("[WebRTCProxy]-[Socketio] Exception", ex);
            }
        };
    }
}