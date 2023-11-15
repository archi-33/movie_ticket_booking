package com.showshaala.show_shaala.payload.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowEntryDto {

  private String showDate;
  private String startTime;
  private String endTime;
  private Long movieId;
  private Long screenId;

}
