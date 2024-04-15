package com.persons.finder.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@EnableSwagger2
@Configuration
public class ApiDocConfig {

    private static final Set<String> APPLICATION_JSON = new HashSet<>(Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));

    private ApiInfo getApiInfo() {
        return new ApiInfo("Persons Finder",
                "Persons Finder API",
                "1.0.0",
                "",
                new Contact(null, null, null), null, null,
                Collections.singletonList(null));
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.persons.finder.presentation"))
                .paths(PathSelectors.any()).build()
                .apiInfo(this.getApiInfo())
                .consumes(APPLICATION_JSON)
                .produces(APPLICATION_JSON)
                .genericModelSubstitutes(Optional.class);
    }

}
