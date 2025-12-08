package com.netflixclone.netflix_clone.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service responsible for interacting with the YouTube Data API v3.
 * Automatically searches for and retrieves video trailer URLs based on movie titles.
 */
@Service
public class YouTubeService {

    // API Key from Google Cloud Console (YouTube Data API v3)
    // TODO: In a production environment, this should be injected via @Value from application.properties
    private final String API_KEY = "${youtube.api.key}";

    private final String SEARCH_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&q={query}&key={key}&maxResults=1";

    private final RestTemplate restTemplate;

    public YouTubeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Searches for a movie trailer on YouTube.
     * Constructs a query using the movie title, executes an API request,
     * and parses the JSON response to extract the video ID of the top result.
     *
     * @param movieTitle the title of the movie to search for
     * @return the full YouTube Embed URL (e.g., https://www.youtube.com/embed/VIDEO_ID), or null if not found
     */
    public String searchTrailer(String movieTitle) {
        try {
            // Construct the search query: "Movie Title" + " trailer"
            String query = movieTitle + " trailer";

            // Prepare the API request URL
            String url = SEARCH_URL.replace("{query}", query).replace("{key}", API_KEY);

            // Execute the HTTP GET request and retrieve the raw JSON response
            String jsonResponse = restTemplate.getForObject(url, String.class);

            // Initialize Jackson ObjectMapper to parse the JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);

            // Navigate the JSON tree structure: items[0] -> id -> videoId
            JsonNode items = root.path("items");
            if (items.isArray() && items.size() > 0) {
                String videoId = items.get(0).path("id").path("videoId").asText();
                return "https://www.youtube.com/embed/" + videoId;
            }

        } catch (Exception e) {
            System.err.println("Error occurred while searching YouTube: " + e.getMessage());
        }

        return null; // Return null if the search yields no results or an error occurs
    }
}