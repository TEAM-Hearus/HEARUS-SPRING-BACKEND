package com.hearus.hearusspring.common.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.hearus.hearusspring.common.environment.ConfigUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOConfig {

    @Value("${cors.allowed-origins}")
    private String[] corsAllowedOrigins;

    @Bean
    public SocketIOServer socketIOServer(ConfigUtil configUtil) {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(configUtil.getProperty("SOCKET_IO_SERVER"));
        config.setPort(Integer.parseInt(configUtil.getProperty("SOCKET_IO_PORT")));

        // Set CORS
        for(String origin : corsAllowedOrigins)
            config.setOrigin(origin);

        return new SocketIOServer(config);
    }
}
