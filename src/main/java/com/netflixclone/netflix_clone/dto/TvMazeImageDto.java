package com.netflixclone.netflix_clone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

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