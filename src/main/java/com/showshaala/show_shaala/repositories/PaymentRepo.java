package com.showshaala.show_shaala.repositories;

import com.showshaala.show_shaala.entities.Payment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface PaymentRepo extends JpaRepository<Payment, Long> {

  @Query(value = "select * from payment where ticket_id= :ticketId", nativeQuery = true)
  Payment findByTicketId(@Param("ticketId") Long ticketId);

}
