package com.showshaala.show_shaala.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "shows")
public class Show {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long showId;

  @Column(name = "showDate", columnDefinition = "DATE", nullable = false)
  private LocalDate showDate;

  @Column(name = "startTime", columnDefinition = "TIME", nullable = false)
  private LocalTime startTime;

  @Column(name = "endTime", columnDefinition = "TIME", nullable = false)
  private LocalTime endTime;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @CreatedDate
  @Column(name = "createdOn")
  private Date createdOn;

  @UpdateTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @LastModifiedDate
  @Column(name = "updatedAt")
  private Date updatedAt;

  @ManyToOne
  @JoinColumn(name = "movieId", referencedColumnName = "movieId")
  private Movie movie;

  @OneToMany(mappedBy = "show", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Ticket> ticketList;

  @OneToMany(mappedBy = "show", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JsonIgnore
  private List<ShowSeats> showSeatList;

  @ManyToOne
  @JsonIgnore
  private Screen screen;

  public void addTicketToShow(Ticket ticket) {
    ticketList.add(ticket);
  }

  public void addShowSeatsToShow(List<ShowSeats> showSeats) {
    showSeatList.addAll(showSeats);
  }


}
