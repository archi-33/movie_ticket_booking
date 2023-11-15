package com.showshaala.show_shaala.payload.requestDto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDetailsDto {
  /**
   * id of the user whose details are to be updated
   */
  private Long id;

  /**
   * The new updated email address of the user.
   */
  @Email
  private String updatedMail;

  /**
   * The new password for the user (optional).
   */
  private String updatedPassword;

  /**
   * The updated first name of the user.
   */
  private String updatedFirstName;

  /**
   * The updated last name of the user.
   */
  private String updatedLastName;

  /**
   * The updated gender of the user.
   */
  private String updatedGender;

}
