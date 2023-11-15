package com.showshaala.show_shaala.payload.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TheaterResponseDto {

  private String name;
  private String city;
  private Integer zipcode;


}
