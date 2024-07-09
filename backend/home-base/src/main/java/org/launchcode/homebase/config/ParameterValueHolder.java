package org.launchcode.homebase.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParameterValueHolder {

    private final ParameterStoreConfig parameterStoreConfig;

    private String endpoint;
    private String dbUsername;
    private String dbPassword;
    private String serpApiKey;
    private String jwtSecret;
    private String jwtExpiration;
    private String jwtRefreshTokenExpiration;

    @Autowired
    public ParameterValueHolder(ParameterStoreConfig parameterStoreConfig) {
        this.parameterStoreConfig = parameterStoreConfig;
    }

    @PostConstruct
    public void retrieveParameterValues() {
        endpoint = parameterStoreConfig.getParameterValue("/haus-wrangler/prod/aws.rds.endpoint");
        dbUsername = parameterStoreConfig.getParameterValue("/haus-wrangler/prod/aws.rds.username");
        dbPassword = parameterStoreConfig.getParameterValue("/haus-wrangler/prod/aws.rds.password");
        serpApiKey = parameterStoreConfig.getParameterValue("/haus-wrangler/prod/serp.api.key");
        jwtSecret = parameterStoreConfig.getParameterValue("/haus-wrangler/prod/jwt.secret.key");
        jwtExpiration = parameterStoreConfig.getParameterValue("/haus-wrangler/prod/jwt.expiration");
        jwtRefreshTokenExpiration = parameterStoreConfig.getParameterValue("/haus-wrangler/prod/jwt.refresh.token.expiration");
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getSerpApiKey() {
        return serpApiKey;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public String getJwtExpiration() {
        return jwtExpiration;
    }

    public String getJwtRefreshTokenExpiration() {
        return jwtRefreshTokenExpiration;
    }
}
