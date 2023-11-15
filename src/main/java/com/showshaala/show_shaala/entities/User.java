package com.showshaala.show_shaala.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"),
    indexes = @Index(name = "idx_email", columnList = "email")
)
public class User {
  /**
   * Unique identifier for the user.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long userId;

  private String username;

  /**
   * The email address of the user. Must be unique.
   */
  @Column(nullable = false, name = "email")
  @Email
  private String email;

  /**
   * The password associated with the user's account.
   */
  @Column(nullable = false)
  private String password;

  /**
   * The first name of the user.
   */
  private String firstName;

  /**
   * The last name of the user.
   */

  private String lastName;

  /**
   * The gender of the user.
   */
  private String gender;


  /**
   * The role(s) associated with the user.
   */
  private String role;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Ticket> ticketList;

  public void addTicketListToUser(List<Ticket> tickets)
  {
    ticketList.addAll(tickets);
  }

//  /**
//   * Indicates whether the user is active or not.
//   */
//  private boolean active;


}
