package com.github.kettoleon.ti.saves;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public class Federation extends StateObject {
    public Federation(SaveFile saveFile, JsonNode node) {
        super(saveFile, node);
    }

    public List<Nation> getMembers() {
        return getReferenceArray("members", Nation.class);
    }
}
