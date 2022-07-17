package com.cerner.talentacquisition;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	private String version;
	private String title;
	private String description;
	private String basePackage;
	private String contactName;
	private String contactEmail;

	public SwaggerConfig() {
		version="v1";
		title="Talent-acquisition";
		description="Talent-Acquisition App";
		contactEmail="cerner.net";
		contactName="cerner";
		basePackage = "com.cerner.talentacquisition";
	}
	@Bean
	public Docket api() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2)
				.securitySchemes(Arrays.asList(	new ApiKey("API KEY", "X-API-KEY", "header")))
				.securityContexts(singletonList(SecurityContext.builder()
								.securityReferences(singletonList(SecurityReference.builder()
												.reference("API KEY")
												.scopes(new AuthorizationScope[0])
												.build()))
								.build()))
				.select()
				.apis(RequestHandlerSelectors.basePackage(basePackage))
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo());
		return docket;
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
			.title(title)
			.description(description)
			.version(version)
			.contact(new Contact(contactName, null, contactEmail))
			.build();
	}

}