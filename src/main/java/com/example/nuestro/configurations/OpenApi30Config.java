package com.example.nuestro.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//https://www.baeldung.com/openapi-jwt-authentication
@Configuration
@OpenAPIDefinition(info = @Info(title = "Nuestro", version = "v1" ))
//@SecurityScheme(
//        name = "Bearer Authentication",
//        type = SecuritySchemeType.HTTP,
//        bearerFormat = "JWT",
//        scheme = "bearer",
//        description = "A JWT token is required to access this API..."
//)
public class OpenApi30Config implements WebMvcConfigurer
{
    @Bean
    public OpenAPI customizeOpenAPI() {
        final String securitySchemeName = "Bearer Authentication";
        var securitySchemesItem= new  io.swagger.v3.oas.models.security.SecurityScheme()
                .name(securitySchemeName)
                .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .description("A JWT token is required to access this API...")
                .bearerFormat("JWT");
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                securitySchemesItem));
    }

    //Used to redirect permanent urls
    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/swagger-ui/index.html");
        // use setStatusCode(HttpStatus.XYZ) for any custom status code if required, e.g. MOVED_PERMANENTLY
        registry.addRedirectViewController("/swagger", "/swagger-ui/index.html");

    }
}