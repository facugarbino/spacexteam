package com.facugarbino.spacexteam.services;

import com.facugarbino.spacexteam.exceptions.TrelloApiRequestException;
import com.facugarbino.spacexteam.models.Bug;
import com.facugarbino.spacexteam.models.Card;
import com.facugarbino.spacexteam.models.Issue;
import com.facugarbino.spacexteam.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class TrelloAPIService {

    @Value("${trello.api.key}")
    private String key;
    @Value("${trello.api.accessToken}")
    private String accessToken;
    @Value("${trello.api.base.endpoint}")
    private String baseEndpoint;
    @Value("${trello.mainboard.id}")
    private String mainBoardId;
    @Value("${trello.mainboard.lists.todo}")
    private String toDoListId;
    @Value("${trello.mainboard.labels.bug}")
    private String bugLabelId;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void createIssue(Issue issue) throws UnirestException, TrelloApiRequestException {
        HttpResponse<String> response = defaultCardCreationRequest(issue, toDoListId)
                .queryString("desc", issue.getDescription())
                .asString();

        validateCardCreationResponse(response);
    }

    public void createBug(Bug bug) throws UnirestException, TrelloApiRequestException {
        bug.setMember(getRandomMember(mainBoardId));

        HttpResponse<String> response = defaultCardCreationRequest(bug, toDoListId)
                .queryString("desc", bug.getDescription())
                .queryString("idMembers", bug.getMember())
                .queryString("idLabels", bugLabelId)
                .asString();

        validateCardCreationResponse(response);
    }

    public void createTask(Task task) throws UnirestException, TrelloApiRequestException {
        HttpResponse<String> response = defaultCardCreationRequest(task, toDoListId)
                .queryString("idLabels", task.getCategory().getId())
                .asString();

        validateCardCreationResponse(response);
    }

    private void validateCardCreationResponse(HttpResponse<String> response) throws TrelloApiRequestException {
        if (response.getStatus() != HttpStatus.SC_OK) {
            throw new TrelloApiRequestException("Trello API returned code " + response.getStatus());
        }
    }

    private HttpRequest defaultCardCreationRequest(Card card, String listId) {
        return Unirest.post(baseEndpoint + "cards")
                .queryString("key", key)
                .queryString("token", accessToken)
                .queryString("idList", listId)
                .queryString("name", card.getTitle());
    }

    private String getRandomMember(String boardId) throws UnirestException {
        HttpResponse<String> response = Unirest.get(baseEndpoint + "boards/" + boardId + "/memberships")
                .queryString("key", key)
                .queryString("token", accessToken)
                .asString();

        List<String> memberIds = new ArrayList<>();
        if (response.getStatus() == HttpStatus.SC_OK) {
            try {
                ArrayNode array = (ArrayNode) objectMapper.readTree(response.getBody());
                for (int i = 0; i < array.size(); i++) {
                    ObjectNode object = (ObjectNode) array.get(i);
                    memberIds.add(object.get("idMember").asText());
                }

                return memberIds.isEmpty() ? "" : memberIds.get(new Random().nextInt(memberIds.size()));
            } catch (JsonProcessingException e) {
                return "";
            }
        }
        return "";
    }

}
