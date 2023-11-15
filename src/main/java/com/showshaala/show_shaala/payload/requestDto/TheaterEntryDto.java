package com.showshaala.show_shaala.payload.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TheaterEntryDto {

  private String name;
  private String city;
  private Integer zipcode;

}
