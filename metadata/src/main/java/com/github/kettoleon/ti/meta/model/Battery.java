package com.github.kettoleon.ti.meta.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Optional;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Battery extends MetaObject {

    private String requiredProjectName;

    @JsonProperty("energyCapacity_GJ")
    private double capacity;

    @JsonProperty("rechargeRate_GJs")
    private double rechargeRate;

    @JsonProperty("mass_tons")
    private double mass;

    private BuildMaterials weightedBuildMaterials;

    public Optional<Project> getRequiredProject() {
        return getReference(requiredProjectName, Project.class);
    }


}
