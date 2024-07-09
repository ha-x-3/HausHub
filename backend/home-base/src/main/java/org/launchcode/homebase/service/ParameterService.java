package org.launchcode.homebase.service;

import org.launchcode.homebase.config.ParameterValueHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParameterService {
    private final ParameterValueHolder parameterValueHolder;

    @Autowired
    public ParameterService(ParameterValueHolder parameterValueHolder) {
        this.parameterValueHolder = parameterValueHolder;
    }

    public void retrieveParameters() {
        String endpoint = parameterValueHolder.getEndpoint();
        String dbUsername = parameterValueHolder.getDbUsername();
        String dbPassword = parameterValueHolder.getDbPassword();
        String serpApiKey = parameterValueHolder.getSerpApiKey();
        String jwtSecret = parameterValueHolder.getJwtSecret();
        String jwtExpiration = parameterValueHolder.getJwtExpiration();
        String jwtRefreshTokenExpiration = parameterValueHolder.getJwtRefreshTokenExpiration();

    }
}
