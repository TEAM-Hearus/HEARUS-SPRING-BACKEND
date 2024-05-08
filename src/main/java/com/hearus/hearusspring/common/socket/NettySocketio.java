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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Component
public class NettySocketio {
    private final SocketIONamespace namespace;

    @Value("FAST_API_ENDPOINT")
    private String fastAPIEndPoint;

    private final BlockingQueue<byte[]> dataQueue;

    @Autowired
    public NettySocketio(SocketIOServer server){
        this.namespace = server.addNamespace("/socketio");
        this.namespace.addConnectListener(onConnected());
        this.namespace.addDisconnectListener(onDisconnected());
        this.namespace.addEventListener("transcription", byte[].class, transcribe());
        this.dataQueue = new LinkedBlockingQueue<>();
    }

    // TODO : byte[].class 형태로 Vue에서의 요청 처리
    private DataListener<byte[]> transcribe() {
        return (client, data, ackSender) -> {
            try {
                log.info("[NettySocketio]-[onReceived]-[{}] Received Audio Data", client.getSessionId().toString());

                if (data != null) {
                    log.info(String.valueOf(data.length));
//                    byte[] audioData = data;
//                    dataQueue.offer(audioData); // Add audio data to the queue for STT processing
//
//                    // Optional: Broadcast received data to frontend clients (if needed)
//                    // namespace.getBroadcastOperations().sendEvent("stt", data);
//
//                    // Start a separate thread for STT processing
//                    new Thread(() -> {
//                        String textResult = sendAudioDataToFastAPI(audioData); // Perform STT using Whisper model
//                        if (textResult != null) {
//                            namespace.getBroadcastOperations().sendEvent("sttResult", textResult);
//                        }
//                    }).start();
                } else {
                    log.info("[NettySocketio] Unexpected data type received");
                }
            } catch (Exception e) {
                log.error("[NettySocketio]-[transcribe()]", e);
            }
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
                fastAPIEndPoint + "/",
                HttpMethod.POST,
                new HttpEntity<>(requestBody, headers),
                String.class
        );
        return "";
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