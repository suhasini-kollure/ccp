package com.ccp.controller;

import com.ccp.dto.DateFilter;
import com.ccp.model.Card;
import com.ccp.model.Customer;
import com.ccp.model.Payment;
import com.ccp.service.CardService;
import com.ccp.service.CustomerService;
import com.ccp.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("payment")
@Slf4j
@Tag(name = "Payment Controller", description = "Controller operations for payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final CardService cardService;
    private final CustomerService customerService;

    @Autowired
    public PaymentController(PaymentService paymentService, CardService cardService, CustomerService customerService) {
        this.paymentService = paymentService;
        this.cardService = cardService;
        this.customerService = customerService;
    }

    @Operation(summary = "Process Payment", description = "Endpoint to process a payment by cardNumber")
    @PostMapping("/process/{cardNumber}")
    public ResponseEntity<Payment> processPayment(
            @Valid @RequestBody Payment payment, @PathVariable("cardNumber") String cardNumber) {

        Card card = cardService.getCard(cardNumber);
        payment.setCard(card);

        Customer customer = customerService.getCustomerByCardNumber(cardNumber);
        payment.setCustomer(customer);

        log.info("Opening a new transaction.");
        payment.setStatus("Open");

        Payment processedPayment = paymentService.processPayment(payment);
        return new ResponseEntity<>(processedPayment, HttpStatus.OK);
    }

    @Operation(summary = "Get Transaction", description = "Endpoint to get transaction by transactionId")
    @GetMapping("/getTransaction/{transactionId}")
    public ResponseEntity<Payment> getTransaction(@PathVariable("transactionId") String transactionId) {
        Payment transaction = paymentService.getTransaction(transactionId);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @Operation(summary = "Get All Transactions of Card", description = "Endpoint to get all the transactions of card")
    @GetMapping("/getAllTransactionsOfCard/{cardNumber}")
    public ResponseEntity<List<Payment>> getAllTransactionOfCard(@PathVariable("cardNumber") String cardNumber) {
        List<Payment> allTransactionsOfCard = paymentService.getAllTransactionsOfCard(cardNumber);
        return new ResponseEntity<>(allTransactionsOfCard, HttpStatus.OK);
    }

    @Operation(
            summary = "Get All Transactions of Customer",
            description = "Endpoint to get all the transactions of Customer")
    @GetMapping("/getAllTransactionsOfCustomer/{customerId}")
    public ResponseEntity<List<Payment>> getAllTransactionsOfCustomer(@PathVariable("customerId") String customerId) {
        List<Payment> allTransactionsOfCustomer = paymentService.getAllTransactionsOfCustomer(customerId);
        return new ResponseEntity<>(allTransactionsOfCustomer, HttpStatus.OK);
    }

    @Operation(
            summary = "Get All Specific Transactions of Card",
            description = "Endpoint to get all the specific transactions of card")
    @GetMapping("/getAllSpecificTransactionsOfCard/{cardNumber}")
    public ResponseEntity<List<Payment>> getAllSpecificTransactionsOfCard(
            @PathVariable("cardNumber") String cardNumber, @Valid @RequestBody DateFilter dateFilter) {
        List<Payment> allSpecificTransactionsOfCard =
                paymentService.getAllSpecificTransactionsOfCard(cardNumber, dateFilter);
        return new ResponseEntity<>(allSpecificTransactionsOfCard, HttpStatus.OK);
    }

    @Operation(
            summary = "Get All Specific Transactions of Customer",
            description = "Endpoint to get all the specific transaction of Customer")
    @GetMapping("/getAllSpecificTransactionsOfCustomer/{customerId}")
    public ResponseEntity<List<Payment>> getAllSpecificTransactionsOfCustomer(
            @PathVariable("customerId") String customerId, @Valid @RequestBody DateFilter dateFilter) {
        List<Payment> allSpecificTransactionsOfCustomer =
                paymentService.getAllSpecificTransactionsOfCustomer(customerId, dateFilter);
        return new ResponseEntity<>(allSpecificTransactionsOfCustomer, HttpStatus.OK);
    }
}
