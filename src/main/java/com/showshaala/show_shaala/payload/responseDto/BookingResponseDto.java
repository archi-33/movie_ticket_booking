package com.showshaala.show_shaala.payload.responseDto;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingResponseDto {

  private Long ticketId;
  private double amount;
  private TheaterResponseDto theaterResponseDto;
  private String ScreenName;
  private ShowResponseDto showResponseDto;
  private List<ShowSeatResponse> seatList;
  private Date bookedAt;
  private boolean cancelled;

}
