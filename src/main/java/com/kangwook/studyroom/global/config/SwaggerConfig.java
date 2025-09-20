package com.kangwook.studyroom.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY) // APIKEY 타입
                .in(SecurityScheme.In.HEADER)     // 헤더에서 가져옴
                .name("Authorization");           // 헤더 이름

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("Authorization");

        return new OpenAPI()
                .info(apiInfo())
                .addSecurityItem(securityRequirement)
                .schemaRequirement("Authorization", securityScheme);
    }

    private Info apiInfo() {
        return new Info()
                .title("스터디룸 예약 API")
                .description("스터디룸 예약 API 설명서,테스트")
                .version("1.0.0");
    }
}
