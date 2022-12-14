package com.github.kettoleon.ti.meta.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuildMaterials {

    private double water;
    private double volatiles;
    private double metals;
    private double nobleMetals;
    private double fissiles;
    private double exotics;


}