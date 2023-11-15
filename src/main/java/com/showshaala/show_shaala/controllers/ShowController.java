package com.showshaala.show_shaala.controllers;

import com.showshaala.show_shaala.entities.Show;
import com.showshaala.show_shaala.payload.ApiResponse;
import com.showshaala.show_shaala.payload.Error;
import com.showshaala.show_shaala.payload.ServiceResponse;
import com.showshaala.show_shaala.payload.requestDto.ShowEntryDto;
import com.showshaala.show_shaala.payload.responseDto.ShowResponse;
import com.showshaala.show_shaala.payload.responseDto.ShowResponseDto;
import com.showshaala.show_shaala.services.ShowService;
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
public class ShowController {

  @Autowired
  ShowService showService;

  @GetMapping("/getShow")
  @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
  public ResponseEntity<ApiResponse> getShow(@RequestParam("id") Long id) {
    ServiceResponse<ShowResponseDto> foundShow = showService.getShow(id);
    if (foundShow.getSuccess()) {
      return new ResponseEntity<>(
          new ApiResponse("success", foundShow.getData(), foundShow.getMessage()),
          HttpStatus.FOUND);
    }
    return new ResponseEntity<>(
        (new ApiResponse("failure", null, new Error(foundShow.getMessage()))),
        HttpStatus.BAD_REQUEST);
  }

  @PostMapping("/addShow")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Secured("ROLE_ADMIN")
  public ResponseEntity<ApiResponse> addShow(@RequestBody ShowEntryDto showEntryDto) {
    ServiceResponse<ShowResponseDto> createdShow = showService.addShow(showEntryDto);
    if (createdShow.getSuccess()) {
      return new ResponseEntity<>(
          (new ApiResponse("success", createdShow.getData(), createdShow.getMessage())),
          HttpStatus.CREATED);
    }
    return new ResponseEntity<>(
        (new ApiResponse("failure", null, new Error(createdShow.getMessage()))),
        HttpStatus.BAD_REQUEST);
  }

  @GetMapping("/getShowsByMovies")
  @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
  ResponseEntity<ApiResponse> getShowsByMovieDateAndCity(@RequestParam String date,
      @RequestParam Long movieId, @RequestParam String city) {
    ServiceResponse<List<ShowResponse>> shows = showService.getShowsByMovieDateAndCity(date, movieId, city);
    if (shows.getSuccess()) {
      return new ResponseEntity<>((new ApiResponse("success", shows.getData(), shows.getMessage())),
          HttpStatus.FOUND);
    }
    return new ResponseEntity<>(
        (new ApiResponse("failure", null, new Error(shows.getMessage()))), HttpStatus.BAD_REQUEST);
  }

//  @PostMapping("/listOfDates")
//  @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
//  List<Date> getDateLists(@RequestParam Long movieId)
//  {
//    return showService.searchShowDatesByMovie(movieId);
//  }

}
