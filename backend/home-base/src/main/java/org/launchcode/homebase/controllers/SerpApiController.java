package org.launchcode.homebase.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.launchcode.homebase.service.SerpApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:5173/", "http://localhost:8081"})
@RequestMapping("/api")
public class SerpApiController {

    @Autowired
    private final SerpApiService serpApiService;

    public SerpApiController(SerpApiService serpApiService) {
        this.serpApiService = serpApiService;
    }

    @GetMapping("/search")
    public JsonNode searchForFilter(@RequestParam String filterSize) {
        return serpApiService.getGoogleShoppingResults(filterSize);
    }

}
