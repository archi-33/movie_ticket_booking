package com.showshaala.show_shaala.repositories;

import com.showshaala.show_shaala.entities.Theater;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TheaterRepo extends JpaRepository<Theater, Long> {

//  @Transactional
//  @Modifying
//  @Query(value = "insert into theaters(name, city, zipcode ) VALUES ( :name, :city, :zipcode )", nativeQuery = true)
//  void insertTheatreByParam(@Param("name") String name, @Param("city") String city,
//      @Param("zipcode") Integer zipcode);

  List<Theater> findAllByZipcode(Integer zipcode);

  List<Theater> findAllByCity(String city);


}
