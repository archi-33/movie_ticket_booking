package com.showshaala.show_shaala.services;

import com.showshaala.show_shaala.payload.ServiceResponse;
import com.showshaala.show_shaala.payload.responseDto.BookingResponseDto;
import com.showshaala.show_shaala.providers.PaymentStatus;
import java.security.Principal;

public interface PaymentService {

  ServiceResponse<BookingResponseDto> pay(Long ticketId, PaymentStatus status, Principal principal);

}
