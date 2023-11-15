package com.showshaala.show_shaala.services;

import com.showshaala.show_shaala.entities.Movie;
import com.showshaala.show_shaala.payload.ServiceResponse;
import com.showshaala.show_shaala.payload.requestDto.MovieEntryDto;
import java.util.List;

public interface MovieService {

  ServiceResponse<Movie> addMovie(MovieEntryDto movieEntryDto);

  ServiceResponse<Movie> getMovie(Long movieId);

  ServiceResponse<List<Movie>> getMovieList(String city);

}
