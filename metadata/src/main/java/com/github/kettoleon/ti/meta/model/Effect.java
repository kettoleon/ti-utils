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
public class Effect extends MetaObject {

    private String instantEffect;
    private String operation;
    private double value;
    private double instantRnd;
    private String strValue;
    private String effectTarget;
    private String effectSecondaryTarget;
    private String effectDuration;
    private boolean stackable;

    @JsonProperty("duration_months")
    private int durationMonths;

    private List<String> initialFactionsStr;

    private List<String> contexts;



}
