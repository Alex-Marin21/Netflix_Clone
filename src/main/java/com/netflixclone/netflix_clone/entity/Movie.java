package com.netflixclone.netflix_clone.entity; // Verifica sa fie pachetul tau corect

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "MOVIES") // Mapam pe tabelul MOVIES din SQL Server
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Integer movieId;

    @Column(name = "tmdb_id", nullable = false, unique = true)
    private Integer tmdbId; // ID-ul filmului de la API-ul extern (TheMovieDB)

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "poster_path", length = 200)
    private String posterPath;

    @Column(name = "genres", length = 255)
    private String genres;

    @Column(name = "description", length = 4000)
    private String description;

    @Column(name = "rating")
    private Double rating; // Nota (ex: 8.5)

    @Column(name = "release_date")
    private String releaseDate;

    @Column(name = "trailer_url")
    private String trailerUrl;

    public String getTrailerUrl() { return trailerUrl; }
    public void setTrailerUrl(String trailerUrl) { this.trailerUrl = trailerUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public String getGenres() { return genres; }
    public void setGenres(String genres) { this.genres = genres; }


    public Movie() {
    }

    public Movie(Integer tmdbId, String title, String posterPath) {
        this.tmdbId = tmdbId;
        this.title = title;
        this.posterPath = posterPath;
    }

    // ==========================================
    // GETTERS & SETTERS
    // ==========================================

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public Integer getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(Integer tmdbId) {
        this.tmdbId = tmdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    // ==========================================
    // EQUALS & HASHCODE
    // ==========================================
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(tmdbId, movie.tmdbId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tmdbId);
    }
}