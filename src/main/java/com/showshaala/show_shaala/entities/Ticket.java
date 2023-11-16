package com.showshaala.show_shaala.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ticket")
public class Ticket {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long ticketId;

  @Column(name = "amount", nullable = false)
  private double amount;

  @CreationTimestamp
  @Column(name = "bookedAt", nullable = false)
  private Date bookedAt;

  private String ScreenName;

  @Column(columnDefinition = "boolean default false")
  private boolean cancelled;

  @ManyToOne
  @JsonIgnore
  private User user;

  @ManyToOne
  @JsonIgnore
  private Show show;

  @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<ShowSeats> seatList;

  @OneToOne(cascade = CascadeType.ALL, mappedBy = "ticket")
  private Payment payment;

  public void addShowSeatsToTicket(List<ShowSeats> showSeats) {
    seatList.addAll(showSeats);
  }

}
