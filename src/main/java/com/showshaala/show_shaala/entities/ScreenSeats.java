package com.showshaala.show_shaala.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "screen_seats")
public class ScreenSeats {

  public ScreenSeats(String seatNumber, int rate) {
    this.seatNumber = seatNumber;
    this.rate = rate;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "seatNumber", nullable = false)
  private String seatNumber;

  @Column(name = "rate", nullable = false)
  private int rate;


  @ManyToOne
  @JsonIgnore
  private Screen screen;

}
