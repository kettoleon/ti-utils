package com.github.kettoleon.ti.meta.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Optional;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Radiator extends MetaObject {


    private String requiredProjectName;

    @JsonProperty("specificMass_2s_kgm2")
    private double specificMass;

    @JsonProperty("specificPower_2s_KWkg")
    private double specificPower;

    @JsonProperty("operatingTemp_K")
    private double operatingTemperature;

    private double emissivity;

    private int vulnerability;
    private Boolean collector;



    private BuildMaterials weightedBuildMaterials;

    public Optional<Project> getRequiredProject() {
        return getReference(requiredProjectName, Project.class);
    }


}
