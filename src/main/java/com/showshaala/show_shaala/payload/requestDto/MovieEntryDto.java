package com.showshaala.show_shaala.payload.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieEntryDto {
  private String title;

  private String movieDuration;

  private String releaseDate;


}
