package com.showshaala.show_shaala.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

  /**
   * Creates and configures a BCrypt password encoder bean.
   *
   * @return A BCryptPasswordEncoder instance for encoding and verifying passwords.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {

    return new BCryptPasswordEncoder();

  }

  /**
   * Creates and configures an AuthenticationManager bean.
   *
   * @param builder The AuthenticationConfiguration builder for building the AuthenticationManager.
   * @return An AuthenticationManager instance for handling authentication.
   * @throws Exception If an error occurs during the configuration.
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration builder)
      throws Exception {
    return builder.getAuthenticationManager();
  }


  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

}
