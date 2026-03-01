package com.netflixclone.netflix_clone.repository;

import com.netflixclone.netflix_clone.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/** Interfata Repository pentru gestionarea operatiilor cu baza de date pentru filme
 * @author Marin-Sirbu Alex-Florin
 * @version 10 Ianuarie 2026
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    Optional<Movie> findByTmdbId(Integer tmdbId);

    List<Movie> findByTitleContainingIgnoreCase(String title);

    List<Movie> findByGenresContaining(String genre);

    /**
     * Returneaza 4 filme aleatorii din acelasi gen, excluzand filmul curent.
     * Foloseste NEWID() specific MS SQL Server pentru randomizare.
     */
    @Query(value = "SELECT TOP 4 * FROM MOVIES WHERE genres LIKE %:genre% AND movie_id != :excludeId ORDER BY NEWID()", nativeQuery = true)
    List<Movie> findSimilarRandom(@Param("genre") String genre, @Param("excludeId") Integer excludeId);
}