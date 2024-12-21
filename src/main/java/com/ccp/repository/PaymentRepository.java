package com.ccp.repository;

import com.ccp.model.Card;
import com.ccp.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    Optional<Payment> findByCardAndStatus(Card card, String status);

}
