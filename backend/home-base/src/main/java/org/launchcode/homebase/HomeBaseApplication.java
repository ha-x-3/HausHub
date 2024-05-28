package org.launchcode.homebase;

import org.launchcode.homebase.config.ParameterStoreConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HomeBaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeBaseApplication.class, args);
	}

	String endpoint = ParameterStoreConfig.getParameterValue("/haus-wrangler/aws.rds.endpoint");
	String dbUsername = ParameterStoreConfig.getParameterValue("/haus-wrangler/aws.rds.username");
	String dbPassword = ParameterStoreConfig.getParameterValue("/haus-wrangler/aws.rds.password");
	String serpApiKey = ParameterStoreConfig.getParameterValue("/haus-wrangler/serp.api.key");
	String jwtSecret = ParameterStoreConfig.getParameterValue("/haus-wrangler/jwt.secret.key");
	String jwtExpiration = ParameterStoreConfig.getParameterValue("/haus-wrangler/jwt.expiration");
	String jwtRefreshTokenExpiration = ParameterStoreConfig.getParameterValue("/haus-wrangler/jwt.refresh.token.expiration");
}
