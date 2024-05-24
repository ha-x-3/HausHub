package org.launchcode.homebase.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class SerpApiService {

    @Value("${serpapi.api.key}")
    private String serpApiKey;

    private static final String SERP_API_URL = "https://serpapi.com/search.json";

    private final HttpClient client = HttpClient.newHttpClient();

    public JsonNode getGoogleShoppingResults(String searchQuery) {
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("engine", "google_shopping");
            parameters.put("q", URLEncoder.encode(searchQuery, StandardCharsets.UTF_8.toString()));
            parameters.put("api_key", serpApiKey);

            String apiUrl = buildSerpApiUrl(parameters);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to fetch Google Shopping results. Status code: " + response.statusCode());
            }

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error fetching Google Shopping results", e);
        }
    }

    private String buildSerpApiUrl(Map<String, String> parameters) {
        StringBuilder urlBuilder = new StringBuilder(SERP_API_URL);
        urlBuilder.append("?");

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        return urlBuilder.toString();
    }
}
