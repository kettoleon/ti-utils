package com.github.kettoleon.ti.meta.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project extends ResearchSubject {

    @JsonProperty("AI_techRole")
    private String aiTechRole;

    public boolean isFactionObjective() {
        return aiTechRole.equals("FactionObjective");
    }
}
