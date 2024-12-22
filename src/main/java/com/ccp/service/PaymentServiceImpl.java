package com.ccp.service;

import com.ccp.dto.DateFilter;
import com.ccp.model.Card;
import com.ccp.model.Payment;
import com.ccp.repository.PaymentRepository;
import jakarta.ws.rs.NotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

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
                    Optional<Payment> existingOpenPayment =
                            paymentRepository.findByCardAndStatus(payment.getCard(), "Open");
                    if (existingOpenPayment.isPresent()
                            && existingOpenPayment
                                    .get()
                                    .getTimestamp()
                                    .isAfter(LocalDateTime.now().minusMinutes(10))) {
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

    @Override
    public Payment getTransaction(String transactionId) {
        Optional<Payment> payment = paymentRepository.findById(transactionId);
        if (payment.isPresent()) {
            log.info("Transaction found with transactionId : {}", transactionId);
            return payment.get();
        } else {
            log.error("Transaction not found with transactionId : {}", transactionId);
            throw new NotFoundException(String.format("Transaction not found with transactionId : %s", transactionId));
        }
    }

    @Override
    public List<Payment> getAllTransactionsOfCard(String cardNumber) {
        List<Optional<Payment>> optionalPayments = paymentRepository.findByCardCardNumber(cardNumber);
        if (optionalPayments.isEmpty()) {
            log.error("Transactions not found with cardNumber : {}", cardNumber);
            throw new NotFoundException(String.format("Transactions not found with cardNumber : %s", cardNumber));
        } else {
            List<Payment> payments = optionalPayments.stream()
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
            log.info("Transactions found for cardNumber : {}", cardNumber);
            return payments;
        }
    }

    @Override
    public List<Payment> getAllTransactionsOfCustomer(String customerId) {
        List<Optional<Payment>> optionalPayments = paymentRepository.findByCustomerCustomerId(customerId);
        if (optionalPayments.isEmpty()) {
            log.error("Transactions not found with customerId : {}", customerId);
            throw new NotFoundException(String.format("Transactions not found with customerId : %s", customerId));
        } else {
            List<Payment> payments = optionalPayments.stream()
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
            log.info("Transactions found for customerId : {}", customerId);
            return payments;
        }
    }

    @Override
    public List<Payment> getAllSpecificTransactionsOfCard(String cardNumber, DateFilter dateFilter) {
        List<Optional<Payment>> optionalPayments = paymentRepository.findByCardCardNumberAndTimestampBetween(
                cardNumber, dateFilter.getFromDateTime(), dateFilter.getToDateTime());
        if (optionalPayments.isEmpty()) {
            log.error(
                    "Transactions not found with cardNumber {}, from {} to {}",
                    cardNumber,
                    dateFilter.getFromDateTime(),
                    dateFilter.getToDateTime());
            throw new NotFoundException(String.format(
                    "Transactions not found with cardNumber %s, from %s to %s",
                    cardNumber, dateFilter.getFromDateTime(), dateFilter.getToDateTime()));
        } else {
            List<Payment> payments = optionalPayments.stream()
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
            log.info(
                    "Transactions found for cardNumber {}, from {} to {}",
                    cardNumber,
                    dateFilter.getFromDateTime(),
                    dateFilter.getToDateTime());
            return payments;
        }
    }

    @Override
    public List<Payment> getAllSpecificTransactionsOfCustomer(String customerId, DateFilter dateFilter) {
        List<Optional<Payment>> optionalPayments = paymentRepository.findByCustomerCustomerIdAndTimestampBetween(
                customerId, dateFilter.getFromDateTime(), dateFilter.getToDateTime());
        if (optionalPayments.isEmpty()) {
            log.error(
                    "Transactions not found with customerId {}, from {} to {}",
                    customerId,
                    dateFilter.getFromDateTime(),
                    dateFilter.getToDateTime());
            throw new NotFoundException(String.format(
                    "Transactions not found with customerId %s, from %s to %s",
                    customerId, dateFilter.getFromDateTime(), dateFilter.getToDateTime()));
        } else {
            List<Payment> payments = optionalPayments.stream()
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
            log.info(
                    "Transactions found for customerId {}, from {} to {}",
                    customerId,
                    dateFilter.getFromDateTime(),
                    dateFilter.getToDateTime());
            return payments;
        }
    }
}
