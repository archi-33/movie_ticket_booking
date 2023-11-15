package com.showshaala.show_shaala.payload.responseDto;

import com.showshaala.show_shaala.entities.Movie;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowResponseDto {

  private Long showId;
  private LocalDate showDate;
  private LocalTime startTime;
  private LocalTime endTime;
  private Movie movie;

}
