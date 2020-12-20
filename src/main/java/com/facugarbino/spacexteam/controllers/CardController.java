package com.facugarbino.spacexteam.controllers;

import com.facugarbino.spacexteam.exceptions.InvalidRequestException;
import com.facugarbino.spacexteam.exceptions.TrelloApiRequestException;
import com.facugarbino.spacexteam.models.*;
import com.facugarbino.spacexteam.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CardController {
    private CardService cardService;


    @Autowired
    public void setCardService(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/")
    ResponseEntity<ApiResponse> addNewCard(@RequestBody CardCreationRequest request) {
        try {
            cardService.createCardFromRequest(request);
        } catch (InvalidRequestException | TrelloApiRequestException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ApiResponse("ok"), HttpStatus.OK);
    }

}