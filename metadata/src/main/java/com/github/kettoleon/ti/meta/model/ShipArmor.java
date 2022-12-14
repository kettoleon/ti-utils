package com.github.kettoleon.ti.meta.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Optional;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipArmor extends MetaObject {


    //Armor points = Surface of the cylinder = 2*pi*r*h = 2*pi*(shipHullWidth/2)*shipHullLength
    //Each armor point defends against 2MJ of energy -> Kg per armor points/surface area = 2 (MJ) / durability (MJ/Kg)
    //However they seem to be doing a rounding with: Math.round((20/durability)*surface)/10

    private String requiredProjectName;

    @JsonProperty("density_kgm3")
    private double density;

    @JsonProperty("heatofVaporization_MJkg")
    private double durability;

    private String specialty;

    private BuildMaterials weightedBuildMaterials;

    public Optional<Project> getRequiredProject() {
        return getReference(requiredProjectName, Project.class);
    }


}
