package com.showshaala.show_shaala.controllers;

import com.showshaala.show_shaala.entities.Screen;
import com.showshaala.show_shaala.entities.Show;
import com.showshaala.show_shaala.entities.Theater;
import com.showshaala.show_shaala.payload.ApiResponse;
import com.showshaala.show_shaala.payload.Error;
import com.showshaala.show_shaala.payload.ServiceResponse;
import com.showshaala.show_shaala.payload.requestDto.ScreenEntryDto;
import com.showshaala.show_shaala.payload.requestDto.TheaterEntryDto;
import com.showshaala.show_shaala.payload.responseDto.ShowResponseDto;
import com.showshaala.show_shaala.payload.responseDto.ShowSeatResponse;
import com.showshaala.show_shaala.services.ScreenService;
import com.showshaala.show_shaala.services.TheaterService;
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
public class TheaterController {

  @Autowired
  private TheaterService theaterService;
  @Autowired
  private ScreenService screenService;


  @GetMapping("/getTheaterByZipcode")
  @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
  public ResponseEntity<ApiResponse> fetchByZipcode(@RequestParam Integer zipcode) {
    ServiceResponse<List<Theater>> serviceResponse = theaterService.fetchByZipcode(zipcode);
    if(serviceResponse.getSuccess())
      return new ResponseEntity<>(
        new ApiResponse("success", serviceResponse.getData(), serviceResponse.getMessage()),
        HttpStatus.FOUND);
    return new ResponseEntity<>(new ApiResponse("failure", null, new Error(serviceResponse.getMessage())), HttpStatus.BAD_REQUEST);
  }
  @GetMapping("/getTheaterByCity")
  @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
  public ResponseEntity<ApiResponse> fetchByCity(@RequestParam String city) {
    ServiceResponse<List<Theater>> serviceResponse = theaterService.fetchByCity(city);
    if(serviceResponse.getSuccess())
      return new ResponseEntity<>(
          new ApiResponse("success", serviceResponse.getData(), serviceResponse.getMessage()),
          HttpStatus.FOUND);
    return new ResponseEntity<>(new ApiResponse("failure", null, new Error(serviceResponse.getMessage())), HttpStatus.BAD_REQUEST);
  }


  @PostMapping("/addTheater")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Secured("ROLE_ADMIN")
  ResponseEntity<ApiResponse> addTheater(@RequestBody TheaterEntryDto theaterEntryDto) {
    ServiceResponse<Theater> createdTheater = theaterService.addTheater(theaterEntryDto);
    if(createdTheater.getSuccess())
      return new ResponseEntity<>(
        new ApiResponse("success",createdTheater .getData(),
            createdTheater.getMessage()), HttpStatus.CREATED);
    return new ResponseEntity<>(new ApiResponse("failure", null, new Error(createdTheater.getMessage())), HttpStatus.BAD_REQUEST);

  }

  @GetMapping("/getTheater")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Secured("ROLE_ADMIN")
  ResponseEntity<ApiResponse> getTheater(@RequestParam Long theaterId) {
    ServiceResponse<Theater> foundTheater = theaterService.getTheater(theaterId);
    if (foundTheater.getSuccess()) {
      return new ResponseEntity<>(
          new ApiResponse("success", foundTheater.getData(), foundTheater.getMessage()),
          HttpStatus.FOUND);
    } else {
      return new ResponseEntity<>(
          (new ApiResponse("failure", null, new Error(foundTheater.getMessage()))),
          HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/addScreen")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Secured("ROLE_ADMIN")
  ResponseEntity<ApiResponse> createScreensInTheater(@RequestBody ScreenEntryDto screenEntryDto) {
    ServiceResponse<Screen> screenServiceResponse = screenService.addScreenToTheater(screenEntryDto);
    if (screenServiceResponse.getSuccess()) {
      return new ResponseEntity<>(new ApiResponse("success", screenServiceResponse.getData(),
          screenServiceResponse.getMessage()), HttpStatus.CREATED);
    } else {
      return new ResponseEntity<>(
          (new ApiResponse("failure", null, new Error(screenServiceResponse.getMessage()))),
          HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/fetchAllShowsInTheater")
  @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
  ResponseEntity<ApiResponse> fetchAllShowsInATheater(@RequestParam Long theaterId) {
    ServiceResponse<List<ShowResponseDto>> serviceResponse = theaterService.fetchAllShowsInTheater(theaterId);
    if(serviceResponse.getSuccess())
      return new ResponseEntity<>(
        new ApiResponse("success", serviceResponse.getData(), serviceResponse.getMessage()), HttpStatus.FOUND);
    return new ResponseEntity<>(new ApiResponse("failure", null, new Error(
        serviceResponse.getMessage())), HttpStatus.BAD_REQUEST);
  }

}
