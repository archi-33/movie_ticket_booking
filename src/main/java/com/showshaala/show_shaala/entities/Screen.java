package com.showshaala.show_shaala.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "screen")
public class Screen {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "screenId")
  private Long screenId;

  @Column(name = "screenName")
  private String screenName;

  @Column(name = "totalNoOfSeats")
  private Integer totalNoOfSeats;

  @ManyToOne
  @JsonIgnore
  private Theater theater;

  @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<ScreenSeats> screenSeats = new ArrayList<>();


  @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Show> shows= new ArrayList<>();

  public void addScreenSeatsToScreen(List<ScreenSeats> seats) {
    setScreenSeats(seats);
  }

  public void addShowToScreen(Show show) {
    shows.add(show);
  }

}
