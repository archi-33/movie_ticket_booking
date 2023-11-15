package com.showshaala.show_shaala.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "theater")
public class Theater {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long theaterId;

  @Column(name = "name")
  private String name;

  @Column(name = "city", nullable = false)
  private String city;

  @Column(name = "zipcode")
  private Integer zipcode;

  @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Screen> screens= new ArrayList<>();

  public void addScreenToTheater(@NonNull Screen screen) {
    screens.add(screen);
  }

}
