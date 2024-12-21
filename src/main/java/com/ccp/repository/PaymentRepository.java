package com.ccp.repository;

import com.ccp.model.Card;
import com.ccp.model.Payment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    Optional<Payment> findByCardAndStatus(Card card, String status);
}
