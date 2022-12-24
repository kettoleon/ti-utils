package com.github.kettoleon.ti.saves;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

public class Region extends StateObject {
    public Region(SaveFile saveFile, JsonNode node) {
        super(saveFile, node);
    }

    public Optional<Nation> getNation(){
        return getReference(Nation.class, "nation");
    }
}
