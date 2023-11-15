package com.showshaala.show_shaala.services.seviceImpl;

import com.showshaala.show_shaala.entities.User;
import com.showshaala.show_shaala.payload.CustomUserDetail;
import com.showshaala.show_shaala.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  /**
   * The repository for accessing User entities.
   */
  @Autowired
  private UserRepo userRepo;

  /**
   * Loads user details by their email (username).
   *
   * @param email The email (username) of the user to load.
   * @return A UserDetails object representing the loaded user.
   * @throws UsernameNotFoundException If the user is not found in the repository.
   */
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    //loading user by username(email)
    User user = userRepo.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found!!"));
    CustomUserDetail customUserDetail = new CustomUserDetail(user);
    return customUserDetail;
  }



}
