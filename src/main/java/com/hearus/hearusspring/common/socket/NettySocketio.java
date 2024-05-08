package com.hearus.hearusspring.common.socket;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.hearus.hearusspring.data.dto.SocketMsgDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class NettySocketio {
    private final SocketIONamespace namespace;

    @Autowired
    public NettySocketio(SocketIOServer server){
        this.namespace = server.addNamespace("/socketio");
        this.namespace.addConnectListener(onConnected());
        this.namespace.addDisconnectListener(onDisconnected());
        this.namespace.addEventListener("transcription", SocketMsgDTO.class, transcribe());
    }

    private DataListener<SocketMsgDTO> transcribe() {
        return (client, data, ackSender) -> {
            log.info("[NettySocketio]-[onReceived]-[{}] Received Audio Data", client.getSessionId().toString());

            byte[] audioData = data.getAudioData();

            new Thread(() -> {
                String textResult = sendAudioDataToFastAPI(audioData);

                // 3. FastAPI로부터 응답된 텍스트를 새로운 SocketMsgDTO 객체에 담아 전송
                SocketMsgDTO responseDTO = new SocketMsgDTO();
                responseDTO.setMessage(textResult);
                namespace.getBroadcastOperations().sendEvent("stt", responseDTO);
            }).start();
            namespace.getBroadcastOperations().sendEvent("stt", data);
        };
    }

    private String sendAudioDataToFastAPI(byte[] audioData) {
        // 1. RestTemplate 인스턴스 생성
        RestTemplate restTemplate = new RestTemplate();

        // 2. 요청 헤더 설정 (옵션)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        // 3. 요청 본문 설정
        byte[] requestBody = audioData;

        // 4. POST 요청 전송 및 응답 객체 받기
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "<FASTAPI_ENDPOINT_URL>",
                HttpMethod.POST,
                new HttpEntity<>(requestBody, headers),
                String.class
        );

        // 5. 응답 결과 파싱 및 텍스트 추출
        // TODO : FIX Jackson JSON Parsing
        try {
            JSONObjectNode responseJSON = new JSONObject(responseEntity.getBody());
            String text = responseJSON.getString("text");
            return text;
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }


    private ConnectListener onConnected() {
        return client -> {
            HandshakeData handshakeData = client.getHandshakeData();
            log.info("[NettySocketio]-[onConnected]-[{}] Connected to Socket Module through '{}'", client.getSessionId().toString(), handshakeData.getUrl());
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            log.info("[NettySocketio]-[onDisconnected]-[{}] Disconnected from Socket Module.", client.getSessionId().toString());
        };
    }
}