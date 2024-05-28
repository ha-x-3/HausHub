package org.launchcode.homebase.config;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParameterStoreConfig {

    private static final SsmClient ssmClient = SsmClient.builder()
            .region(Region.US_EAST_2)
            .build();

    public static String getParameterValue(String parameterName) {
        GetParameterRequest getParameterRequest = GetParameterRequest.builder()
                .name(parameterName)
                .withDecryption(true)
                .build();

        GetParameterResponse getParameterResponse = ssmClient.getParameter(getParameterRequest);
        return getParameterResponse.parameter().value();
    }
}