package com.facugarbino.spacexteam.models;

import java.util.Locale;
import java.util.Random;

public class Bug extends Card {
    private String description;
    private String member;

    public Bug() {

    }

    public Bug(String description) {
        this.description = description;
        this.title = generateTitle();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    private String generateTitle() {
        int number = new Random().nextInt();
        String word = description.split(" ")[0].toLowerCase(Locale.ROOT);

        return "bug-" + word + "-" + number;
    }
}
