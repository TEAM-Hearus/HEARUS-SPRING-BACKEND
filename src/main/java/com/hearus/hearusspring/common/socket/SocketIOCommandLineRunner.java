package com.hearus.hearusspring.common.socket;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SocketIOCommandLineRunner implements CommandLineRunner {

    private final SocketIOServer server;

    @Autowired
    public SocketIOCommandLineRunner(SocketIOServer server) {
        log.info("[SocketIOCommandLineRunner] SocketIOServer Initialized");
        this.server = server;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("[SocketIOCommandLineRunner] SocketIOServer Running");
        server.start();
    }
}