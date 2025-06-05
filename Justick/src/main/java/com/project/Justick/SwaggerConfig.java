package com.project.Justick;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .servers(List.of(new Server().url("/justick_spring"))); // ★ 핵심 라인
    }

    private Info apiInfo() {
        return new Info()
                .title("Justick_API")
                .description("Let's practice Swagger UI")
                .version("1.0.0");
    }
}
