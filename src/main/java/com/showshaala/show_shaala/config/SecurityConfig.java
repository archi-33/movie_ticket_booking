package com.showshaala.show_shaala.config;

import com.showshaala.show_shaala.security.JwtAuthFilter;
import com.showshaala.show_shaala.security.JwtEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
  @Autowired
  private JwtEntryPoint point;
  @Autowired
  private JwtAuthFilter filter;

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  /**
   * Configures the security filter chain for HTTP requests.
   *
   * @param http The HttpSecurity object to configure.
   * @return The configured SecurityFilterChain.
   * @throws Exception If an error occurs during configuration.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.csrf(csrf -> csrf.disable())
        .cors(cors -> cors.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/app/signup", "/app/login").permitAll().anyRequest()
            .authenticated())
        .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  /**
   * Configures a DaoAuthenticationProvider for authenticating users.
   *
   * @return The configured DaoAuthenticationProvider.
   */
  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {

    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setUserDetailsService(userDetailsService);
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
    return daoAuthenticationProvider;

  }

  //  @Bean
//  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//    http.csrf(csrf -> csrf.disable())
//        .authorizeRequests()
//        .requestMatchers("/api/auth/login").permitAll().requestMatchers("api/users/signup").permitAll()
//        .anyRequest()
//        .authenticated()
//        .and().exceptionHandling(ex -> ex.authenticationEntryPoint(point))
//        .sessionManagement(
//            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//    http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
//    return http.build();
//  }

}
