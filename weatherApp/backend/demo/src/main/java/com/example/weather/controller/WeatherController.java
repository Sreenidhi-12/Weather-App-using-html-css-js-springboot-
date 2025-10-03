package com.example.weather.controller;

import com.example.weather.dto.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class WeatherController {

    @Value("${openweathermap.api.key}")
    private String apiKey; // from application.properties

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/weather")
    public ResponseEntity<?> getWeather(@RequestParam String city) {
        if (city == null || city.isBlank()) {
            return ResponseEntity.badRequest()
                    .body("{\"error\": \"City parameter is required\"}");
        }

        String url = "https://api.openweathermap.org/data/2.5/weather?q="
                + city + "&appid=" + apiKey + "&units=metric";

        try {
            WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);

            if (response == null || response.getMain() == null || response.getWeather() == null || response.getWeather().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"error\": \"No data found for city: " + city + "\"}");
            }

            var result = new java.util.HashMap<String, Object>();
            result.put("city", response.getName());
            result.put("temp", response.getMain().getTemp());
            result.put("description", response.getWeather().get(0).getDescription());

            return ResponseEntity.ok(result);

        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Failed to fetch weather: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Unexpected error occurred\"}");
        }
    }
}
