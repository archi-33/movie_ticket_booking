package com.showshaala.show_shaala.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Error {
  /**
   * The error message describing the issue.
   */
  private String errorMessage;

//  private String uuid = UUID.randomUUID().toString();

}
