package com.showshaala.show_shaala.repositories;

import com.showshaala.show_shaala.entities.Ticket;
import com.showshaala.show_shaala.providers.PaymentStatus;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepo extends JpaRepository<Ticket, Long> {

//  @Transactional
//  @Modifying
//  @Query(value = "SELECT t.* FROM ticket t " +
//      "INNER JOIN payment p ON t.ticket_id = p.ticket_id " +
//      "WHERE t.user_user_id = :userId " +
//      "AND p.status = :status", nativeQuery = true)
//  List<Ticket> findAllByUserId(@Param("userId") Long userId, @Param("status")PaymentStatus status);

  @Transactional
  @Modifying
  @Query(value = "select * from ticket where user_user_id = :userId", nativeQuery = true)
  List<Ticket> findAllByUserId(@Param("userId") Long userId);

}
