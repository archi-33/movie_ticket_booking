package com.showshaala.show_shaala.payload.requestDto;

import lombok.Data;

@Data
public class JwtRequest {
  /**
   * The email address of the user.
   */
  private String email;

  /**
   * The password associated with the user's account.
   */
  private String password;

}
