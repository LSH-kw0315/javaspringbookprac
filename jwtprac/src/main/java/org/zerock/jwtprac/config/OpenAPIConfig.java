package org.zerock.jwtprac.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class OpenAPIConfig {
    @Bean
    public GroupedOpenApi restApi(){
        return GroupedOpenApi.builder()
                .pathsToMatch("/api/**")//이 링크를 사용하면 이 분류에 포함
                .group("REST API")//구분할 이름
                .build();
    }

    @Bean
    public GroupedOpenApi commonApi(){
        return GroupedOpenApi.builder()
                .pathsToMatch("/**/*")
                .pathsToExclude("/api/**/*")//이 링크를 사용하면 이 분류에서 제외
                .group("Common API")
                .build();
    }

    @Bean
    public OpenAPI openAPI(){

        //세큐리티 스키마 설정
        SecurityScheme bearerAuth=new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("Authorization")
                .in(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION);


        //세큐리티 요청 설정
        SecurityRequirement addSecurityItem=new SecurityRequirement();
        addSecurityItem.addList("Authorization");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Authorization",bearerAuth))
                .addSecurityItem(addSecurityItem)
                .info(new Info().title("SpringDoc SwaggerUI example")
                        .description("Test SwaggerUI application")
                        .version("v0.0.1"));
    }
}
