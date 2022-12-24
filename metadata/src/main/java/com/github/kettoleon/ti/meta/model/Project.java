package com.github.kettoleon.ti.meta.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project extends ResearchSubject {

    @JsonProperty("AI_techRole")
    private String aiTechRole;

    @JsonProperty("AI_projectRole")
    private String aiProjectRole;

    private List<String> effects;

    private boolean oneTimeGlobally;
    private boolean repeatable;
    //TODO chances and resourcesGranted

    public boolean isFactionObjective() {
        return aiTechRole.equals("FactionObjective");
    }

    public List<Effect> getEffects(){
        return getReferenceArray(effects, Effect.class);
    }

}
