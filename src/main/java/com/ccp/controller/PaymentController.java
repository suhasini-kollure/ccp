package com.ccp.controller;

import com.ccp.model.Card;
import com.ccp.model.Payment;
import com.ccp.service.CardService;
import com.ccp.service.PaymentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("payment")
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;
    private final CardService cardService;

    @Autowired
    public PaymentController(PaymentService paymentService, CardService cardService) {
        this.paymentService = paymentService;
        this.cardService = cardService;
    }

    @PostMapping("/process/{cardNumber}")
    public ResponseEntity<Payment> processPayment(
            @Valid @RequestBody Payment payment, @PathVariable("cardNumber") String cardNumber) {

        Card card = cardService.getCard(cardNumber);
        payment.setCard(card);

        log.info("Opening a new transaction.");
        payment.setStatus("Open");

        Payment processedPayment = paymentService.processPayment(payment);
        return new ResponseEntity<>(processedPayment, HttpStatus.OK);
    }
}
