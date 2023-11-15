package com.showshaala.show_shaala.payload.responseDto;

import lombok.Data;

@Data
public class JwtResponse {

  /**
   * The JWT token generated upon successful authentication.
   */
  private String token;

}
