package com.hearus.hearusspring.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//요청 서버 도메인 설정
@OpenAPIDefinition(
        servers = {
                @Server(url = "https://hearus-spring-be.shop:8080", description = "개발 서버"),
                @Server(url = "http://localhost:8080", description = "로컬 서버")
        })
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        
        //Authorization 설정
        SecurityScheme apiKey = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
                .scheme("bearer")
                .bearerFormat("JWT");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("Bearer Token");

        //api 문서 설정
        Info info = new Info()
                .title("Hearus Spring Backend API")
                .version("1.0")
                .description("API for Hearus");

                
        return new OpenAPI()
                .info(info)
                .components(new Components().addSecuritySchemes("Bearer Token", apiKey))
                .addSecurityItem(securityRequirement);
    }

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            // 모든 Spring Data REST 엔드포인트 제거
            openApi.getPaths().entrySet().removeIf(entry -> !entry.getKey().startsWith("/api"));// 필요에 따라 조건 수정
        };
    }
}
