package com.showshaala.show_shaala.controllers;

import com.showshaala.show_shaala.payload.ApiResponse;
import com.showshaala.show_shaala.payload.Error;
import com.showshaala.show_shaala.payload.ServiceResponse;
import com.showshaala.show_shaala.payload.requestDto.BookingRequestDto;
import com.showshaala.show_shaala.services.seviceImpl.TicketServiceImpl;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
@EnableMethodSecurity
public class TicketController {

  @Autowired
  TicketServiceImpl ticketService;


  @PostMapping("/ticketBooking")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<ApiResponse> bookTicket(@RequestBody BookingRequestDto bookingRequestDto,
      Principal principal) {
    ServiceResponse<?> bookingResponse = ticketService.bookTicket(bookingRequestDto, principal);
    if (bookingResponse.getSuccess()) {
      return new ResponseEntity<>(
          new ApiResponse("success", bookingResponse.getData(), bookingResponse.getMessage()),
          HttpStatus.PAYMENT_REQUIRED);
    }
    return new ResponseEntity<>(
        (new ApiResponse("failure", null, new Error(bookingResponse.getMessage()))),
        HttpStatus.BAD_REQUEST);

  }

  @GetMapping("/bookings")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<ApiResponse> bookingHistory(Principal principal) {
    ServiceResponse<?> bookingResponse = ticketService.bookingHistory(principal);
    if (bookingResponse.getSuccess()) {
      return new ResponseEntity<>(
          new ApiResponse("success", bookingResponse.getData(), bookingResponse.getMessage()),
          HttpStatus.FOUND);
    }
    return new ResponseEntity<>(
        (new ApiResponse("failure", null, new Error(bookingResponse.getMessage()))),
        HttpStatus.BAD_REQUEST);

  }



//  @GetMapping("/getTicket")
//  @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
//  public ResponseEntity<Ticket> getTickets(@RequestParam("id") int id) {
//    return new ResponseEntity<>(ticketService.getTicket(id), HttpStatus.FOUND);
//  }

}
