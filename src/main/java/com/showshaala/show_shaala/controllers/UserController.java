package com.showshaala.show_shaala.controllers;

import com.showshaala.show_shaala.entities.User;
import com.showshaala.show_shaala.payload.ApiResponse;
import com.showshaala.show_shaala.payload.Error;
import com.showshaala.show_shaala.payload.ServiceResponse;
import com.showshaala.show_shaala.payload.UserDto;
import com.showshaala.show_shaala.payload.requestDto.UpdateRoleDto;
import com.showshaala.show_shaala.payload.requestDto.UpdateUserDetailsDto;
import com.showshaala.show_shaala.services.UserService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
@EnableMethodSecurity
public class UserController {

  @Autowired
  private UserService userService;

  /**
   * Endpoint for granting access to a user with the 'ROLE_ADMIN' role.
   *
   * @param updateRoleDto The UpdateRoleDto containing user email and role.
   * @param principal     The Principal object representing the currently authenticated user.
   * @return ResponseEntity containing ApiResponse with success message or error message.
   */
  @PutMapping("/admin/giveAccess")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Secured("ROLE_ADMIN")
  public ResponseEntity<ApiResponse> giveAccessToUser(@RequestBody UpdateRoleDto updateRoleDto,
      Principal principal) {
    ServiceResponse<User> getUser = userService.giveAccess(updateRoleDto.getEmail(),
        updateRoleDto.getRole(), principal);
    if (getUser.getSuccess()) {
      return new ResponseEntity<>(
          (new ApiResponse("success", new UserDto(getUser.getData()), getUser.getMessage())),
          HttpStatus.OK);
    } else {
      return new ResponseEntity<>(
          (new ApiResponse("failure", null, new Error(getUser.getMessage()))),
          HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Endpoint for loading all users, accessible only to users with the 'ROLE_ADMIN' role.
   *
   * @return List of UserDto objects representing all users.
   */
  @GetMapping("/admin/fetchAllUsers")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Secured("ROLE_ADMIN")
  public List<UserDto> loadUsers() {
    return userService.loadAll();
  }


  /**
   * Endpoint for updating the details of the currently authenticated user with the 'ROLE_USER'
   * role.
   *
   * @param updateUserDetailsDto The UpdateUserDetailsDto containing updated user details.
   * @param principal            The Principal object representing the currently authenticated
   *                             user.
   * @return ResponseEntity containing ApiResponse with success message or error message.
   */
  @PutMapping("/updateOwnDetails")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<ApiResponse> updateUserDetail(
      @RequestBody UpdateUserDetailsDto updateUserDetailsDto, Principal principal) {
    ServiceResponse<UserDto> getUser = userService.update(updateUserDetailsDto, principal);
    if (getUser.getSuccess()) {
      return new ResponseEntity<>(
          (new ApiResponse("success", getUser.getData(), getUser.getMessage())),
          HttpStatus.OK);
    } else {
      return new ResponseEntity<>(
          (new ApiResponse("failure", null, new Error(getUser.getMessage()))),
          HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Endpoint for updating the details of any user by the currently authenticated user with the
   * 'ROLE_ADMIN' role.
   *
   * @param updateUserDetailsDto The UpdateUserDetailsDto containing user details to be updated.
   * @param principal            The Principal object representing the currently authenticated
   *                             user.
   * @return ResponseEntity containing ApiResponse with success message or error message.
   */
  @PutMapping("/admin/update")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<ApiResponse> updateAnyUserDetail(
      @RequestBody UpdateUserDetailsDto updateUserDetailsDto, Principal principal) {
    ServiceResponse<UserDto> getUser = userService.updateAnyUser(updateUserDetailsDto.getId(),
        updateUserDetailsDto, principal);
    if (getUser.getSuccess()) {
      return new ResponseEntity<>(
          (new ApiResponse("success", getUser.getData(), getUser.getMessage())), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(
          (new ApiResponse("failure", null, new Error(getUser.getMessage()))),
          HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Endpoint for retrieving the name of the currently authenticated user with the 'ROLE_USER'
   * role.
   *
   * @param principal The Principal object representing the currently authenticated user.
   * @return The name of the authenticated user.
   */
  @GetMapping("/getName")
  @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
  public String giveName(Principal principal) {
    return principal.getName();
  }

//  @ExceptionHandler
//@ExceptionHandler(AuthenticationException.class)
//public Exception exceptionHandler() {
//  return new Exc
//}

//  @PostMapping("/signin")
//  public ResponseEntity<ApiResponse> signin(@RequestBody LoginDto loginDto) {
//    OutputResponse<UserDto> user = userService.getUser(loginDto.getEmail(), loginDto.getPassword());
//    if(user.getSuccess()) {
//      return new ResponseEntity<>((new ApiResponse("success", user.getData(),null)), HttpStatus.OK);
//    }else
//      return new ResponseEntity<>((new ApiResponse("error",null, user.getMessage())), HttpStatus.BAD_REQUEST);
//  }

}
