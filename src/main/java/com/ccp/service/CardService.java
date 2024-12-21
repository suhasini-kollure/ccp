package com.ccp.service;

import com.ccp.dto.UpdateCard;
import com.ccp.model.Card;

public interface CardService {

    Card saveCard(Card card, String customerId);

    Card getCard(String cardNumber);

    Card updateCard(String cardNumber, UpdateCard updateCard);

    String deleteCard(String cardNumber);
}
