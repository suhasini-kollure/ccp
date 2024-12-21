package com.ccp.service;

import com.ccp.model.Payment;

public interface PaymentService {

    Payment processPayment(Payment payment);

    boolean isCardExpired(String cardNumber);

}
