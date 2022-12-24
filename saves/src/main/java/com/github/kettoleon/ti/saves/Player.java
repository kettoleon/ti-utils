package com.github.kettoleon.ti.saves;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

public class Player {

    private SaveFile saveFile;
    private int id;
    private JsonNode node;

    public Player(SaveFile saveFile, int id, JsonNode node) {
        this.saveFile = saveFile;
        this.id = id;
        this.node = node;
    }

    public int getId() {
        return id;
    }

    public boolean isAI() {
        return node.path("isAI").asBoolean();
    }

    public Optional<Faction> getFaction() {
        return saveFile.getFactionById(node.path("faction").path("value").asInt());
    }

}
