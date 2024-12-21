package com.ccp.service;

import com.ccp.dto.UpdateCard;
import com.ccp.model.Card;
import com.ccp.model.Customer;
import com.ccp.repository.CardRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CardServiceImpl implements CardService {

    private final CustomerService customerService;
    private final CardRepository cardRepository;

    @Autowired
    public CardServiceImpl(CustomerService customerService, CardRepository cardRepository) {
        this.customerService = customerService;
        this.cardRepository = cardRepository;
    }

    @Override
    public Card saveCard(Card card, String customerId) {
        if (cardRepository.existsById(card.getCardNumber())) {
            throw new DataIntegrityViolationException("Card already exists with card number : " + card.getCardNumber());
        }
        Customer customer = customerService.getCustomerById(customerId);
        card.setCustomer(customer);
        Card savedCard = cardRepository.save(card);
        log.info("Card saved in DB with cardNumber : {}", card.getCardNumber());
        return savedCard;
    }

    @Override
    public Card getCard(String cardNumber) {
        Optional<Card> optionalCard = cardRepository.findById(cardNumber);

        if (optionalCard.isPresent()) {
            Card card = optionalCard.get();
            log.info("Card found with cardNumber : {}", cardNumber);
            return card;
        } else {
            log.error("Card not found with cardNumber : {}", cardNumber);
            throw new NotFoundException(String.format("Card not found with cardNumber : %s", cardNumber));
        }
    }

    @Override
    public Card updateCard(String cardNumber, UpdateCard updateCard) {
        Card card = getCard(cardNumber);
        if (updateCard.getCardType() != null) {
            card.setCardType(updateCard.getCardType());
        }
        if (updateCard.getExpirationDate() != null) {
            card.setExpirationDate(updateCard.getExpirationDate());
        }
        if (updateCard.getNameOnCard() != null) {
            card.setNameOnCard(updateCard.getNameOnCard());
        }
        if (updateCard.getCvv() != null) {
            card.setCvv(updateCard.getCvv());
        }
        Card savedCard = cardRepository.save(card);
        log.info("Card updated for cardNumber : {}", cardNumber);
        return savedCard;
    }

    @Override
    public String deleteCard(String cardNumber) {
        Card card = getCard(cardNumber);
        cardRepository.deleteById(card.getCardNumber());
        log.info("Card deleted with cardNumber : {}", cardNumber);
        return String.format("Card deleted with cardNumber : %s", cardNumber);
    }
}
