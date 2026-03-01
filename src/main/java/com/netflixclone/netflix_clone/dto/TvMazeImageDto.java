package com.netflixclone.netflix_clone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Clasa DTO pentru maparea imaginilor din API-ul TvMaze
 * @author Marin-Sirbu Alex-Florin
 * @version 10 Ianuarie 2026
 */
public class TvMazeImageDto {
    @JsonProperty("original")
    private String originalUrl;

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }
}