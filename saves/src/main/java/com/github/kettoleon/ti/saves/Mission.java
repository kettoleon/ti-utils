package com.github.kettoleon.ti.saves;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

public class Mission {
    private final SaveFile saveFile;
    private final int id;
    private final JsonNode node;

    public Mission(SaveFile saveFile, int id, JsonNode node) {

        this.saveFile = saveFile;
        this.id = id;
        this.node = node;
    }

    public Optional<Councilor> getCouncilor() {
        return saveFile.getCouncilorById(node.path("councilor").path("value").asInt());
    }

    public String getDisplayName() {
        return node.path("displayName").asText();
    }

    public String getTemplateName() {
        return node.path("templateName").asText();
    }


}
