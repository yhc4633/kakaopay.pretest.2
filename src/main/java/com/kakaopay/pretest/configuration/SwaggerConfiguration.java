package com.kakaopay.pretest.configuration;

import com.google.common.base.Predicates;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
@Profile({"local"})
public class SwaggerConfiguration {
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_12)
                .select()
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("Hyungcheol-Yoon","","yhc4633@hanmail.net");
        ApiInfo apiInfo = new ApiInfo(
                "Kakaopay Pretest 2",
                "Kakaopay Pretest 2. Ecotourism API Server",
                "1.0.0",
                "",
                contact,
                "",
                "",
                Collections.EMPTY_LIST);
        return apiInfo;
    }
}