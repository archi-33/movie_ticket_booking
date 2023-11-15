package com.showshaala.show_shaala.payload.requestDto;

import com.showshaala.show_shaala.payload.responseDto.ShowSeatResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequestDto {

  private List<ShowSeatResponse> seatList;
  private Long showId;


}
