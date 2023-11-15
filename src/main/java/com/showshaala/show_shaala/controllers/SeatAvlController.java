package com.showshaala.show_shaala.controllers;

import com.showshaala.show_shaala.payload.ApiResponse;
import com.showshaala.show_shaala.payload.Error;
import com.showshaala.show_shaala.payload.ServiceResponse;
import com.showshaala.show_shaala.payload.responseDto.ShowSeatResponse;
import com.showshaala.show_shaala.services.SeatAvlService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
@EnableMethodSecurity
public class SeatAvlController {
  @Autowired
  private SeatAvlService seatAvlService;

  @GetMapping("/listOfSeats")
  @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
  ResponseEntity<ApiResponse> getListOfSeatsInAShow(@RequestParam Long showId)
  {
    ServiceResponse<List<ShowSeatResponse>> foundList = seatAvlService.getAvailableSeats(showId);
    if(foundList.getSuccess()){
      return new ResponseEntity<>(
          (new ApiResponse("success", foundList.getData(), foundList.getMessage())), HttpStatus.FOUND);
    }
    return new ResponseEntity<>(
        (new ApiResponse("failure", null, new Error(foundList.getMessage()))),
        HttpStatus.BAD_REQUEST);
  }

}
