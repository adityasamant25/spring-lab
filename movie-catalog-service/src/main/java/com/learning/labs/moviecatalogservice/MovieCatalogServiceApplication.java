package com.learning.labs.moviecatalogservice;

import java.io.InputStream;
import java.security.KeyStore;
import java.util.Collections;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@EnableHystrixDashboard
public class MovieCatalogServiceApplication {
	
	Logger logger = LoggerFactory.getLogger(MovieCatalogServiceApplication.class);

	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		KeyStore keystore;
		HttpComponentsClientHttpRequestFactory requestFactory = null;
		
		try {
			keystore = KeyStore.getInstance("jks");
			ClassPathResource classPathResource = new ClassPathResource("movie-catalog-service.jks");
			InputStream inputStream = classPathResource.getInputStream();
			keystore.load(inputStream, "trustme".toCharArray());
			
			SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(new SSLContextBuilder()
					.loadTrustMaterial(null, new TrustSelfSignedStrategy())
					.loadKeyMaterial(keystore, "trustme".toCharArray()).build(), NoopHostnameVerifier.INSTANCE);
			
			HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).
					setMaxConnTotal(5).setMaxConnPerRoute(5).build();
			
					
			requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
			requestFactory.setReadTimeout(3000);
			requestFactory.setConnectTimeout(3000);
			restTemplate.setRequestFactory(requestFactory);
		}
		catch (Exception ex) {
			logger.error("Exception occurred while creating restTemplate");
		}
		return restTemplate;
	}

	@Bean
	public WebClient.Builder getWebClientBuilder() {
		return WebClient.builder();
	}

	public static void main(String[] args) {
		SpringApplication.run(MovieCatalogServiceApplication.class, args);
	}

	@Bean
	public Docket swaggerConfiguration() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.learning.labs")).build().apiInfo(apiDetails());
	}

	private ApiInfo apiDetails() {
		return new ApiInfo("Movie Catalog Service API", "APIs to fetch a catalog of movies", "1.0", "Free to use",
				new Contact("Aditya Samant", "http://spring-labs.com", "a@b.com"), "API License",
				"http://spring-labs.com", Collections.emptyList());
	}

}
