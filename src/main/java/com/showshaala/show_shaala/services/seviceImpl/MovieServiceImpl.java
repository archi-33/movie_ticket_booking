package com.showshaala.show_shaala.services.seviceImpl;

import com.showshaala.show_shaala.entities.Movie;
import com.showshaala.show_shaala.payload.ServiceResponse;
import com.showshaala.show_shaala.payload.requestDto.MovieEntryDto;
import com.showshaala.show_shaala.repositories.MovieRepo;
import com.showshaala.show_shaala.services.MovieService;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl implements MovieService {

  @Autowired
  private MovieRepo movieRepo;

  @Override
  public ServiceResponse<Movie> addMovie(MovieEntryDto movieEntryDto) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate release_date = LocalDate.parse(movieEntryDto.getReleaseDate(), formatter);
    Duration movie_duration = parseDuration(movieEntryDto.getMovieDuration());
    if (movie_duration == null) {
      return new ServiceResponse<>(false, null, "Invalid duration format. Use '2h30m' format.");
    }
    Movie movie = Movie.builder().title(movieEntryDto.getTitle()).movieDuration(movie_duration)
        .releaseDate(release_date).build();
    Movie savedMovie = movieRepo.save(movie);

    return new ServiceResponse<>(true, savedMovie, "The movie is successfully saved..");

  }

  @Override
  public ServiceResponse<Movie> getMovie(Long movieId) {
    Optional<Movie> foundMovie = movieRepo.findById(movieId);
    if (foundMovie.isPresent()) {
      return new ServiceResponse<>(true, foundMovie.get(), "Movie with " + movieId + " is found");
    } else {
      return new ServiceResponse<>(false, null, "no movie with specified is found..");
    }

  }

  @Override
  public ServiceResponse<List<Movie>> getMovieList(String city) {
    List<Movie> myMovies;
    try {
      myMovies = movieRepo.findMoviesByCity(city);
    }catch (Exception e){
      return new ServiceResponse<>(false, null, e.getMessage());
    }
    return new ServiceResponse<>(true, myMovies,
        "Here is the list of movies in " + city + " city..");
  }

  public static Duration parseDuration(String input) {
    // Define a regular expression to match the format (e.g., "2h30m")
    String pattern = "^(\\d+)h(\\d+)m$";

    if (input.matches(pattern)) {
      // Extract hours and minutes from the input string
      int hours = Integer.parseInt(input.replaceAll(pattern, "$1"));
      int minutes = Integer.parseInt(input.replaceAll(pattern, "$2"));

      // Convert hours and minutes to a Duration
      return Duration.ofHours(hours).plusMinutes(minutes);
    } else {
      return null;
    }
  }

}
