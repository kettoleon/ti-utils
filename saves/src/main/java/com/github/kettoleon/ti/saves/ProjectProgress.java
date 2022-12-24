package com.github.kettoleon.ti.saves;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.kettoleon.ti.meta.model.Project;

import java.util.Optional;

public class ProjectProgress extends StateObject {
    public ProjectProgress(SaveFile saveFile, JsonNode node) {
        super(saveFile, node);
    }

    public Optional<Project> getProject() {
        return getMetaReference("projectTemplateName", Project.class);
    }

    public double getAccumulatedResearch() {
        return getDouble("accumulatedResearch");
    }

    public double getPercentageComplete(){
        return (getAccumulatedResearch() / getProject().orElseThrow().getResearchCost()) * 100.0;
    }


}
