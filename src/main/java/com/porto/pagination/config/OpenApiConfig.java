package com.porto.pagination.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

//Local
//@Configuration
//public class OpenApiConfig {
//
//	@Value("${server.port:8080}")
//	private String serverPort;
//
//	@Bean
//	public OpenAPI customOpenAPI() {
//		return new OpenAPI().info(new Info().title("Paginated API"))
//				.servers(List.of(new Server().url("http://localhost:" + serverPort).description("Development Server")));
//	}
//}

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("Paginated API").version("1.0.0").description("Spring Boot Pagination API"));
	}
}
