package com.showshaala.show_shaala.controllers;

import com.showshaala.show_shaala.payload.ApiResponse;
import com.showshaala.show_shaala.payload.Error;
import com.showshaala.show_shaala.payload.ServiceResponse;
import com.showshaala.show_shaala.payload.UserDto;
import com.showshaala.show_shaala.payload.requestDto.JwtRequest;
import com.showshaala.show_shaala.payload.requestDto.UserEntryDto;
import com.showshaala.show_shaala.security.JwtHelper;
import com.showshaala.show_shaala.services.UserService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class SigninSignUpController {
  @Autowired
  private JwtHelper jwtHelper;
  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserService userService;
  private final Bucket bucket;

  public SigninSignUpController(Bucket bucket) {
    this.bucket = bucket;
  }

  /**
   * Endpoint for creating a new user.
   *
   * @param userEntryDto The UserEntryDto object to be created.
   * @return ResponseEntity containing ApiResponse with success message or error message.
   */
  @PostMapping("/signup")
  public ResponseEntity<ApiResponse> createUser(@RequestBody UserEntryDto userEntryDto) {
    ServiceResponse<UserDto> createdUser = userService.createUser(userEntryDto);
    if (createdUser.getSuccess()) {
      return new ResponseEntity<>((new ApiResponse("success", createdUser.getData(), createdUser.getMessage())),
          HttpStatus.CREATED);
    } else {
      return new ResponseEntity<>(
          (new ApiResponse("failure", null, new Error(createdUser.getMessage()))),
          HttpStatus.BAD_REQUEST);
    }

  }

  /**
   * Endpoint for generating an authentication token based on user credentials.
   *
   * @param jwtRequest The JwtRequest object containing user email and password.
   * @return ResponseEntity containing ApiResponse with success message and generated token or error message.
   */
  @PostMapping("/login")
  public ResponseEntity<ApiResponse> logIn(@RequestBody JwtRequest jwtRequest) {

    if(bucket.tryConsume(1)) {
      doAuthenticate(jwtRequest.getEmail(), jwtRequest.getPassword());
      final UserDetails user = userDetailsService.loadUserByUsername(jwtRequest.getEmail());
      String token = jwtHelper.generateToken(user);
//    ApiResponse response = new ApiResponse();
//    response.setData(token);
      ServiceResponse<UserDto> getUser = userService.getUser(jwtRequest.getEmail(),
          jwtRequest.getPassword());
      if (getUser.getSuccess()) {
        return new ResponseEntity<>((new ApiResponse("success", token)),
            HttpStatus.CREATED);
      } else {
        return new ResponseEntity<>(
            (new ApiResponse("failure", null, new Error(getUser.getMessage()))),
            HttpStatus.BAD_REQUEST);
      }
    }else{
      return new ResponseEntity<>(new ApiResponse("failure",null, "Too many login attempts. Please try again later."), HttpStatus.TOO_MANY_REQUESTS);
    }


  }

  /**
   * Perform user authentication.
   *
   * @param username User's email.
   * @param password User's password.
   */
  public void doAuthenticate(String username, String password) {
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        username, password);
    authenticationManager.authenticate(authenticationToken);
  }

  /**
   * Exception handler for BadCredentialsException.
   *
   * @return Error message for bad credentials.
   */
  @ExceptionHandler(BadCredentialsException.class)
  public String exceptionHandler() {
    return "Bad Credentials!!!!!!!!!!!!!!";
  }

}
