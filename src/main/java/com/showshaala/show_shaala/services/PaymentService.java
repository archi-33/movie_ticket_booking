package com.showshaala.show_shaala.services;

import com.showshaala.show_shaala.entities.Ticket;
import com.showshaala.show_shaala.entities.User;
import com.showshaala.show_shaala.payload.ServiceResponse;
import com.showshaala.show_shaala.payload.responseDto.BookingResponseDto;
import com.showshaala.show_shaala.providers.PaymentStatus;
import java.security.Principal;

public interface PaymentService {

  ServiceResponse<BookingResponseDto> pay(Long ticketId, double amount, Principal principal);
  ServiceResponse<?> processRefund(Ticket ticket);
  ServiceResponse<?> cancelBooking(Long id, Principal principal);

}
