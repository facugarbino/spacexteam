package com.facugarbino.spacexteam.services;

import com.facugarbino.spacexteam.exceptions.InvalidRequestException;
import com.facugarbino.spacexteam.exceptions.TrelloApiRequestException;
import com.facugarbino.spacexteam.models.*;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;

@Service
public class CardService {

    private TrelloAPIService trelloAPIService;

    @Autowired
    public void setTrelloAPIService(TrelloAPIService trelloAPIService) {
        this.trelloAPIService = trelloAPIService;
    }

    public void createCardFromRequest(CardCreationRequest request) throws InvalidRequestException, URISyntaxException, UnirestException, TrelloApiRequestException {
        switch (request.getType()) {
            case "bug":
                checkDescription(request.getDescription());
                this.trelloAPIService.createBug((new Bug(request.getDescription())));
                break;
            case "issue":
                checkTitle(request.getTitle());
                checkDescription(request.getDescription());
                this.trelloAPIService.createIssue(new Issue(request.getTitle(), request.getDescription()));
                break;
            case "task":
                checkTitle(request.getTitle());
                TaskCategory category = checkCategory(request.getCategory());
                this.trelloAPIService.createTask(new Task(request.getTitle(), category));
                break;
            default:
                throw new InvalidRequestException("invalid_type");
        }
    }

    private void checkTitle(String title) throws InvalidRequestException {
        if (title == null || title.equals("")) {
            throw new InvalidRequestException("missing_title");
        }
    }

    private void checkDescription(String description) throws InvalidRequestException {
        if (description == null || description.equals("")) {
            throw new InvalidRequestException("missing_description");
        }
    }

    private TaskCategory checkCategory(String category) throws InvalidRequestException {
        if (category == null) {
            throw new InvalidRequestException("missing_category");
        }
        TaskCategory actualCategory = TaskCategory.getFromLabel(category);
        if (actualCategory == null) {
            throw new InvalidRequestException("invalid_category");
        }
        return actualCategory;
    }

}
