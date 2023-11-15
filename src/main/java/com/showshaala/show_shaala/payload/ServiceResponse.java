package com.showshaala.show_shaala.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponse <T>{
  /**
   * Indicates whether the service operation was successful or not.
   */
  private Boolean success;

  /**
   * The data associated with the response (e.g., user details, tokens, etc.).
   */
  private T data;

  /**
   * An optional message providing additional information about the response, typically used in case
   * of an error.
   */
  private String message;


}
