package com.showshaala.show_shaala.controllers;

import com.showshaala.show_shaala.entities.Movie;
import com.showshaala.show_shaala.payload.ApiResponse;
import com.showshaala.show_shaala.payload.Error;
import com.showshaala.show_shaala.payload.ServiceResponse;
import com.showshaala.show_shaala.payload.requestDto.MovieEntryDto;
import com.showshaala.show_shaala.services.MovieService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
@EnableMethodSecurity
public class MovieController {
  @Autowired
  MovieService movieService;

  @GetMapping("/getMovie")
  @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
  public ResponseEntity<ApiResponse> getMovie(@RequestParam("id") Long movieId) {
    ServiceResponse<Movie> foundMovie = movieService.getMovie(movieId);
    if(foundMovie.getSuccess())
      return new ResponseEntity<>(new ApiResponse("success", foundMovie.getData(), foundMovie.getMessage()), HttpStatus.FOUND);
    else
      return new ResponseEntity<>((new ApiResponse("failure",null, new Error(foundMovie.getMessage()))), HttpStatus.BAD_REQUEST);
  }

  @PostMapping("/addMovie")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Secured("ROLE_ADMIN")
  public ResponseEntity<ApiResponse> addMovie(@RequestBody MovieEntryDto movieEntryDto) {
    ServiceResponse<Movie> createdMovie = movieService.addMovie(movieEntryDto);
    if(createdMovie.getSuccess())
      return new ResponseEntity<>(new ApiResponse("success",createdMovie.getData(),createdMovie.getMessage()), HttpStatus.CREATED);
    else
      return new ResponseEntity<>((new ApiResponse("failure",null, new Error(createdMovie.getMessage()))), HttpStatus.BAD_REQUEST);
  }

  @GetMapping("/listOfMovies")
  @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
  public ResponseEntity<ApiResponse> getMovieList(@RequestParam String city){
    ServiceResponse<List<Movie>> serviceResponse= movieService.getMovieList(city);
    if(serviceResponse.getSuccess())
      return new ResponseEntity<>(new ApiResponse("success", serviceResponse.getData(),serviceResponse.getMessage()), HttpStatus.FOUND);
    return new ResponseEntity<>(new ApiResponse("failure", null, new Error(serviceResponse.getMessage())), HttpStatus.BAD_REQUEST);
  }

}
