package com.showshaala.show_shaala.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiResponse {
  /**
   * The status of the API response (e.g., "success" or "failure").
   */
  private String status;

  /**
   * The data associated with the response (e.g., user details, etc.).
   */
  private Object data;

  /**
   * An authentication token.
   */
  private Object token;

  /**
   * Error details in case of a failure response.
   */
  private Error error;

  private String message;

  public ApiResponse(String status, Object data, Error error) {
    this.status = status;
    this.data = data;
    this.error = error;
  }

  public ApiResponse(String status, Object data, String message){
    this.status = status;
    this.data = data;
    this.message = message;
  }

  // Constructor for "success," "token"
  public ApiResponse(String status, Object token) {
    this.status = status;
    this.token = token;
  }

}
