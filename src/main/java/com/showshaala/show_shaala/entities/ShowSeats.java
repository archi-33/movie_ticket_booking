package com.showshaala.show_shaala.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.showshaala.show_shaala.providers.BookingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
@Table(name = "show_seats")
public class ShowSeats {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "seatNumber", nullable = false)
  private String seatNumber;

  @Column(name = "rate", nullable = false)
  private double rate;

//  @Column(name = "is_Booked", columnDefinition = "bit(1) default 0", nullable = false)
//  private boolean is_booked;

  @Enumerated(EnumType.STRING)
  private BookingStatus status;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "lockedAt")
  private Date lockedAt;

  @ManyToOne
  @JsonIgnore
  private Show show;

  @ManyToOne
  @JsonIgnore
  private Ticket ticket;

  public void lockSeats() {

    BookingStatus stat = getStatus();
    if (stat == BookingStatus.LOCKED) {
      log.info("seats are already temporarily locked");
    } else {
      setStatus(BookingStatus.LOCKED);
      setLockedAt(Date.from(new Date().toInstant()));

    }


  }


  public boolean isLockExpired() {
//    if (getStatus() == BookingStatus.LOCKED) {

      Date currentDateTime = new Date();
      long currentTimeMillis = currentDateTime.getTime();

      long lockExpirationMillis = lockedAt.getTime() + (100 * 1000);

      return currentTimeMillis > lockExpirationMillis;


  }
  public void setAvailable(){
    this.setStatus(BookingStatus.FREE);


  }


}
