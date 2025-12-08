package com.netflixclone.netflix_clone.repository;

import com.netflixclone.netflix_clone.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for the Movie entity.
 * Handles database interactions including CRUD operations, search, and custom native queries.
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    /**
     * Finds a movie by its external API identifier (TMDB/TVMaze ID).
     * Used primarily during the import process to prevent duplicate entries.
     *
     * @param tmdbId the external ID of the movie
     * @return an Optional containing the movie if found, or empty otherwise
     */
    Optional<Movie> findByTmdbId(Integer tmdbId);

    /**
     * Performs a case-insensitive search for movies containing the specified title substring.
     *
     * @param title the search query string
     * @return a list of movies matching the search criteria
     */
    List<Movie> findByTitleContainingIgnoreCase(String title);

    /**
     * Retrieves all movies associated with a specific genre.
     *
     * @param genre the genre to filter by (e.g., "Action", "Drama")
     * @return a list of movies containing the specified genre tag
     */
    List<Movie> findByGenresContaining(String genre);

    /**
     * Retrieves a random selection of 4 movies that share a specific genre,
     * excluding the movie currently being viewed.
     * <p>
     * This method uses a native SQL query specific to MS SQL Server, utilizing
     * the {@code NEWID()} function to randomize the result set order.
     *
     * @param genre     the genre string to match against
     * @param excludeId the ID of the current movie to ensure it doesn't appear in its own recommendations
     * @return a list of up to 4 similar movies ordered randomly
     */
    @Query(value = "SELECT TOP 4 * FROM MOVIES WHERE genres LIKE %:genre% AND movie_id != :excludeId ORDER BY NEWID()", nativeQuery = true)
    List<Movie> findSimilarRandom(@Param("genre") String genre, @Param("excludeId") Integer excludeId);
}