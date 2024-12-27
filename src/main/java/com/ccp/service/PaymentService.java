package com.ccp.service;

import com.ccp.dto.DateFilter;
import com.ccp.model.Payment;
import java.util.List;

public interface PaymentService {

    Payment processPayment(Payment payment);

    Payment getTransaction(String transactionId);

    List<Payment> getAllTransactionsOfCard(String cardNumber);

    List<Payment> getAllTransactionsOfCustomer(String customerId);

    List<Payment> getAllSpecificTransactionsOfCard(String cardNumber, DateFilter dateFilter);

    List<Payment> getAllSpecificTransactionsOfCustomer(String customerId, DateFilter dateFilter);
}
