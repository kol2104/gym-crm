package com.epam.gymcrm.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.configuration.SpringDocUIConfiguration;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration;
import org.springdoc.webmvc.ui.SwaggerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({SpringDocConfiguration.class, SpringDocWebMvcConfiguration.class, SwaggerConfig.class,
        SpringDocConfigProperties.class, SwaggerUiConfigProperties.class,
        SwaggerUiOAuthProperties.class, SpringDocUIConfiguration.class})
public class SwaggerConfiguration {

    @Bean
    public GroupedOpenApi dafault() {
        return GroupedOpenApi.builder()
                .group("all")
                .packagesToScan("com.epam.gymcrm")
                .pathsToMatch("/swagger-ui").build();
    }
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info().title("Gym CRM")
                        .description("Gym CRM service")
                        .version("v1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
