package com.showshaala.show_shaala.payload.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntryDto {

  private String firstName;
  private String lastName;
  private String gender;
  private String username;
  private String email;
  private String password;

}
