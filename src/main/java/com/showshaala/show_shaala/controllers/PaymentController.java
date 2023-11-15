package com.showshaala.show_shaala.controllers;

import com.showshaala.show_shaala.entities.Payment;
import com.showshaala.show_shaala.payload.ApiResponse;
import com.showshaala.show_shaala.payload.Error;
import com.showshaala.show_shaala.payload.ServiceResponse;
import com.showshaala.show_shaala.payload.requestDto.PaymentRequestDto;
import com.showshaala.show_shaala.payload.responseDto.BookingResponseDto;
import com.showshaala.show_shaala.providers.PaymentStatus;
import com.showshaala.show_shaala.services.PaymentService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
@EnableMethodSecurity
public class PaymentController {
  @Autowired
  private PaymentService paymentService;

  @PostMapping("/pay")
  @PreAuthorize("hasRole('ROLE_USER')")
  ResponseEntity<ApiResponse> pay(@RequestBody PaymentRequestDto paymentRequestDto, Principal principal){
    if(paymentRequestDto.getPaymentStatus()==PaymentStatus.FAIL){
      return new ResponseEntity<>(new ApiResponse("failure", null, new Error("please pay first to confirm ticket")), HttpStatus.PAYMENT_REQUIRED);
    }

    ServiceResponse<BookingResponseDto> pay = paymentService.pay(paymentRequestDto.getTicketId(),paymentRequestDto.getPaymentStatus(),principal);
    if(pay.getSuccess()){
      return new ResponseEntity<>(new ApiResponse("success", pay.getData(), pay.getMessage()),
          HttpStatus.CREATED);
    }
    return new ResponseEntity<>(new ApiResponse("failure", pay.getData(), new Error(pay.getMessage())), HttpStatus.BAD_REQUEST);
  }

}
