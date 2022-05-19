package com.cerner.talentacquisition;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
 @EnableSwagger2
// @ConfigurationProperties("api")
// @ConditionalOnProperty(name="api.swagger.enable", havingValue = "true", matchIfMissing = false)
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
		description="TA-App";
		contactEmail="cerner.net";
		contactName="cerner";
		basePackage = "com.cerner.talentacquisition";
	}
	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.regex("/.*")).build();
	}
	private List<SecurityReference> defaultAuth() {
		final AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		final AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{authorizationScope};
		return Collections.singletonList(new SecurityReference("APIKey", authorizationScopes));
	}
	@Bean
	public Docket api() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2)
				.securitySchemes(Arrays.asList(	new ApiKey("JWT bearer auth", "X-API-KEY", "header")))
				//.securitySchemes(Collections.singletonList(new ApiKey(RestSecurityFilter.HTTP_REQUEST_API_KEY, RestSecurityFilter.HTTP_REQUEST_API_KEY, "header")))
				.select()
				//.apis(RequestHandlerSelectors.basePackage(basePackage))
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())

				.build()
				//.securityContexts(Collections.singletonList(securityContext()))
				.directModelSubstitute(LocalDate.class, Date.class)
				.directModelSubstitute(LocalDateTime.class, java.util.Date.class)
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