package com.netflixclone.netflix_clone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** Clasa DTO pentru maparea detaliilor serialelor din API-ul TvMaze
 * @author Marin-Sirbu Alex-Florin
 * @version 10 Ianuarie 2026
 */
public class TvMazeShowDto {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("image")
    private TvMazeImageDto image;

    @JsonProperty("genres")
    private List<String> genres;

    @JsonProperty("summary")
    private String summary;

    @JsonProperty("premiered")
    private String premiered;

    @JsonProperty("rating")
    private RatingDto rating;

    public static class RatingDto {
        @JsonProperty("average")
        private Double average;

        public Double getAverage() {
            return average;
        }

        public void setAverage(Double average) {
            this.average = average;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TvMazeImageDto getImage() {
        return image;
    }

    public void setImage(TvMazeImageDto image) {
        this.image = image;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPremiered() {
        return premiered;
    }

    public void setPremiered(String premiered) {
        this.premiered = premiered;
    }

    public RatingDto getRating() {
        return rating;
    }

    public void setRating(RatingDto rating) {
        this.rating = rating;
    }
}