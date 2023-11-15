package com.showshaala.show_shaala.payload.requestDto;

import com.showshaala.show_shaala.providers.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequestDto {
  private Long ticketId;
  private PaymentStatus paymentStatus;

}
