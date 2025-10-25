package co.edu.usco.pw.spring_restful_swagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().info(new Info().title("API de Gestión de Usuarios").version("1.0.0").description(
				"Esta API permite gestionar usuarios, incluyendo creación, actualización y eliminación de registros.")
				.termsOfService("http://algo.com/terminos")
				.contact(new io.swagger.v3.oas.models.info.Contact().name("Diego Carvajal")
						.email("diego.carvajal@usco.edu.co").url("http://algo.com"))
				.license(new io.swagger.v3.oas.models.info.License().name("Licencia MIT")
						.url("http://opensource.org/licenses/MIT")));
	}
}
