package com.asitc.pgmicroservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Value("${appVersion}")
    private String appVersion;

    @Bean
    public Docket postgresqlApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(this.apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.asitc.pgmicroservice.controller"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("PostgreSQL API").license("Apache License Version 2.0")
                .version(this.appVersion).build();
    }

}
