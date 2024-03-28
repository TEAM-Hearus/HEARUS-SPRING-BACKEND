package com.hearus.hearusspring.common.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.hearus.hearusspring.common.environment.ConfigUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOConfig {
    @Bean
    public SocketIOServer socketIOServer(ConfigUtil configUtil) {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(configUtil.getProperty("SOCKET_IO_SERVER"));
        config.setPort(Integer.getInteger(configUtil.getProperty("SOCKET_IO_PORT")));
        return new SocketIOServer(config);
    }
}
