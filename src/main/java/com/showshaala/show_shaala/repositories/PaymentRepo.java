package com.showshaala.show_shaala.repositories;

import com.showshaala.show_shaala.entities.Payment;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface PaymentRepo extends JpaRepository<Payment, Long> {

  @Query(value = "select * from payment where ticket_id= :ticketId", nativeQuery = true)
  Payment findByTicketId(@Param("ticketId") Long ticketId);

  @Transactional
  @Query(value = "SELECT * FROM payment WHERE ticket_id = :ticketId AND status = 'PAID'", nativeQuery = true)
  Payment findByTicketIdAndPaymentStatusPaid(@Param("ticketId") Long ticketId);

}
