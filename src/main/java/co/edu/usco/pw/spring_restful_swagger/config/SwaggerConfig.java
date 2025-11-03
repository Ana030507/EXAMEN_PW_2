package co.edu.usco.pw.spring_restful_swagger.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema Académico - API")
                        .version("1.0")
                        .description("Documentación de la API con roles y autenticación."))
                // 🔐 Añadimos esquema de seguridad tipo basic
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("basicAuth", new SecurityScheme()
                                .type(Type.HTTP)
                                .scheme("basic")
                                .in(In.HEADER)
                                .name("Authorization")));
    }
}
