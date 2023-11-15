package com.showshaala.show_shaala.repositories;

import com.showshaala.show_shaala.entities.Movie;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovieRepo extends JpaRepository<Movie, Long> {
  @Transactional
  @Modifying
  @Query(value = "SELECT DISTINCT m.* FROM movie m " +
      "INNER JOIN shows s ON m.movie_id = s.movie_id " +
      "INNER JOIN screen sc ON s.screen_screen_id = sc.screen_id " +
      "INNER JOIN theater t ON sc.theater_theater_id = t.theater_id " +
      "WHERE t.city = :city " +
      "AND s.show_date > NOW()", nativeQuery = true)
  List<Movie> findMoviesByCity(@Param("city") String city);

}
