package com.showshaala.show_shaala.payload.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoleDto {

  /**
   * The email address of the user whose role is to be updated.
   */
  private String email;

  /**
   * The new role to assign to the user.
   */
  private String role;

}
