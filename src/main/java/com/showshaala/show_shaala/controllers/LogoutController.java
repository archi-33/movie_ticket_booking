package com.showshaala.show_shaala.controllers;

import com.showshaala.show_shaala.payload.ApiResponse;
import com.showshaala.show_shaala.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class LogoutController {

  @Autowired
  private JwtHelper jwtHelper;

  //  @RequestMapping(value = "/app/logout", method = RequestMethod.POST)
  @PostMapping("/logout")
  public ResponseEntity<ApiResponse> logout(@RequestHeader("Authorization") String token) {
    // Extract the token (remove "Bearer ")
    token = token.substring(7);

    jwtHelper.addToBlacklist(token);

    return new ResponseEntity<>(new ApiResponse("success", null, "Successfully.. Logged OUT!!!!"),
        HttpStatus.OK);

  }


}
