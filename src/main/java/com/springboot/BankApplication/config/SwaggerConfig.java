package com.springboot.BankApplication.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myCustomConfig(){
        return  new OpenAPI().info(
                new Info().title("Bank Application APIs")
                        .description("By Durrah")
        );
    }
}
