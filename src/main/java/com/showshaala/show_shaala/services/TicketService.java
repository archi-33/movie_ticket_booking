package com.showshaala.show_shaala.services;

import com.showshaala.show_shaala.entities.Ticket;
import com.showshaala.show_shaala.payload.ServiceResponse;
import com.showshaala.show_shaala.payload.requestDto.BookingRequestDto;
import com.showshaala.show_shaala.payload.responseDto.BookingResponseDto;
import java.security.Principal;
import java.util.List;

public interface TicketService {

  ServiceResponse<?> bookTicket(BookingRequestDto bookingRequestDto, Principal principal);
  ServiceResponse<List<BookingResponseDto>> bookingHistory(Principal principal);

//  ServiceResponse<?> cancelBooking(Long id, Principal principal);

//  Ticket getTicket(int id);

}
