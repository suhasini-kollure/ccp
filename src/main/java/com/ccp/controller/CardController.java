package com.ccp.controller;

import com.ccp.dto.UpdateCard;
import com.ccp.model.Card;
import com.ccp.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("card")
@Tag(name = "Card Controller", description = "Controller for card operations")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @Operation(summary = "Save Card", description = "Endpoint to save new card")
    @PostMapping("/save/{customerId}")
    public ResponseEntity<Card> saveCard(@Valid @RequestBody Card card, @PathVariable("customerId") String customerId) {
        Card savedCard = cardService.saveCard(card, customerId);
        return new ResponseEntity<>(savedCard, HttpStatus.OK);
    }

    @Operation(summary = "Get Card", description = "Endpoint to get a already existing card")
    @GetMapping("/get/{cardNumber}")
    public ResponseEntity<Card> getCard(@PathVariable("cardNumber") String cardNumber) {
        Card card = cardService.getCard(cardNumber);
        return new ResponseEntity<>(card, HttpStatus.OK);
    }

    @Operation(summary = "Update Card", description = "Endpoint to update a card")
    @PatchMapping("/update/{cardNumber}")
    public ResponseEntity<Card> updateCard(
            @PathVariable("cardNumber") String cardNumber, @Valid @RequestBody UpdateCard updateCard) {
        Card card = cardService.updateCard(cardNumber, updateCard);
        return new ResponseEntity<>(card, HttpStatus.OK);
    }

    @Operation(summary = "Delete Card", description = "Endpoint to delete a card")
    @DeleteMapping("/delete/{cardNumber}")
    public ResponseEntity<String> deleteCard(@PathVariable("cardNumber") String cardNumber) {
        String deletedCard = cardService.deleteCard(cardNumber);
        return new ResponseEntity<>(deletedCard, HttpStatus.OK);
    }
}
