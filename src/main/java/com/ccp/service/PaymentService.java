package com.ccp.service;

import com.ccp.model.Payment;
import java.util.List;

public interface PaymentService {

    Payment processPayment(Payment payment);

    boolean isCardExpired(String cardNumber);

    Payment getTransaction(String transactionId);

    List<Payment> getAllTransactionsOfCard(String cardNumber);

    List<Payment> getAllTransactionsOfCustomer(String customerId);
}
