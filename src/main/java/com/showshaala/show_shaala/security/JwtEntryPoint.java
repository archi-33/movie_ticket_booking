package com.showshaala.show_shaala.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.showshaala.show_shaala.payload.ApiResponse;
import com.showshaala.show_shaala.payload.Error;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {
  @Autowired
  private JwtHelper jwtHelper;

  /**
   * Handles unauthorized access by setting the response status and providing an error message.
   *
   * @param request       The HTTP request.
   * @param response      The HTTP response.
   * @param authException The authentication exception that triggered this entry point.
   * @throws IOException      If an I/O error occurs while writing the response.
   * @throws ServletException If a servlet-related error occurs.
   */
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {

    String requestHeader = request.getHeader("Authorization");

    if (requestHeader == null) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
    } else if (requestHeader != null && requestHeader.startsWith("Bearer")) {
      String token = requestHeader.substring(7);
      try {
        jwtHelper.getUsernameFromToken(token);
      } catch (Exception e) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
      }

    } else {
      response.setStatus(HttpStatus.FORBIDDEN.value());
    }

    ApiResponse apiResponse = new ApiResponse();
    apiResponse.setStatus("error");
    apiResponse.setData(null);
    Error error = new Error();
    error.setErrorMessage("Access Denied !! " + authException.getMessage());
    apiResponse.setError(error);

    // Serialize the JSON response
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonResponse = objectMapper.writeValueAsString(apiResponse);

    // Set the response content type to JSON
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    PrintWriter writer = response.getWriter();
    writer.println(jsonResponse);


  }


}
