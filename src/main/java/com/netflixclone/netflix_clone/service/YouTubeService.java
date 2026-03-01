package com.netflixclone.netflix_clone.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/** Clasa Service pentru interactiunea cu API-ul YouTube Data v3
 * @author Marin-Sirbu Alex-Florin
 * @version 10 Ianuarie 2026
 */
@Service
public class YouTubeService {

    private final String API_KEY = "AIzaSyAen5eEPyOTj_nAfxFCrW7Szc1e8XwGHxs";
    private final String SEARCH_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&q={query}&key={key}&maxResults=1";

    private final RestTemplate restTemplate;

    public YouTubeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String searchTrailer(String movieTitle) {
        try {
            String query = movieTitle + " trailer";
            String url = SEARCH_URL.replace("{query}", query).replace("{key}", API_KEY);

            String jsonResponse = restTemplate.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);

            JsonNode items = root.path("items");
            if (items.isArray() && items.size() > 0) {
                String videoId = items.get(0).path("id").path("videoId").asText();
                return "https://www.youtube.com/embed/" + videoId;
            }

        } catch (Exception e) {
            System.err.println("Error occurred while searching YouTube: " + e.getMessage());
        }

        return null;
    }
}