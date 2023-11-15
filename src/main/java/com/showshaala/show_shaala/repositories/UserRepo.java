package com.showshaala.show_shaala.repositories;

import com.showshaala.show_shaala.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepo extends JpaRepository<User, Long> {
  /**
   * Retrieves a user by their email address.
   *
   * @param email The email address of the user to find.
   * @return An Optional containing the User entity if found, or an empty Optional if not found.
   */
  @Query(value = "SELECT * FROM user WHERE email = :email", nativeQuery = true)
  Optional<User> findByEmail(@Param("email") String email);

  Optional<User> findByUsername(String username);

}
