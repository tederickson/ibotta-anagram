package com.ibotta.anagram.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI ibottaOpenAPI() {
        final var info = new Info();
        info.setTitle("Ibotta Dev Project");
        info.setVersion("0.1.0");
        info.setDescription("Ibotta Development Project");

        return new OpenAPI().info(info);
    }

}

