package com.showshaala.show_shaala.repositories;

import com.showshaala.show_shaala.entities.Show;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShowRepo extends JpaRepository<Show, Long> {
  @Transactional
  @Modifying
  List<Show> findByShowDateAndMovieMovieId(LocalDate showDate, Long movieId);

  @Transactional
  @Modifying
  @Query(value = "SELECT DISTINCT(showDate) FROM shows WHERE showDate > now() AND movieId = :movieId ORDER BY showDate ", nativeQuery = true)
  List<Date> findAllByMovie(@Param("movieId") Integer movieId);

  @Transactional
  @Modifying
  @Query(value = "SELECT s.* FROM shows s " +
      "INNER JOIN screen sc ON s.screen_screen_id = sc.screen_id " +
      "INNER JOIN theater t ON sc.theater_theater_id = t.theater_id " +
      "WHERE s.show_date = :date " +
      "AND s.movie_id = :movieId " +
      "AND t.city = :city", nativeQuery = true)
  List<Show> findShowsByDateMovieAndCity(
      @Param("date") LocalDate date,
      @Param("movieId") Long movieId,
      @Param("city") String city
  );


}
