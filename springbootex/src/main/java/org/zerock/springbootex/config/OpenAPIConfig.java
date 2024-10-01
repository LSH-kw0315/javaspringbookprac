package org.zerock.springbootex.config;


import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
