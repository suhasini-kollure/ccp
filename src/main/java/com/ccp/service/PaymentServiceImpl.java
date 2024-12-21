package com.ccp.service;

import com.ccp.model.Card;
import com.ccp.model.Payment;
import com.ccp.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final CardService cardService;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, CardService cardService) {
        this.paymentRepository = paymentRepository;
        this.cardService = cardService;
    }

    @Override
    @Transactional
    public Payment processPayment(Payment payment) {
        try {
            if (!isCardExpired(payment.getCard().getCardNumber())) {
                if ("Open".equals(payment.getStatus())) {
                    log.info("Checking for existing Open transaction.");
                    Optional<Payment> existingOpenPayment = paymentRepository.findByCardAndStatus(payment.getCard(), "Open");
                    if (existingOpenPayment.isPresent() && existingOpenPayment.get().getTimestamp().isAfter(LocalDateTime.now().minusMinutes(10))) {
                        log.error("Transaction failed, One existing transaction is Open.");
                        payment.setStatus("Failure");
                        payment.setTimestamp(LocalDateTime.now());
                        return paymentRepository.save(payment);
                    }
                }
            } else {
                throw new IllegalStateException("Card is expired!!!");
            }
            payment.setStatus("Closed");
            payment.setTimestamp(LocalDateTime.now());
            log.info("Payment successful, and transaction is closed.");
            return paymentRepository.save(payment);

        } catch (Exception e) {
            log.error("Something went wrong.");
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public boolean isCardExpired(String cardNumber) {
        Card card = cardService.getCard(cardNumber);
        return card.getExpirationDate().isBefore(LocalDate.now());
    }
}
