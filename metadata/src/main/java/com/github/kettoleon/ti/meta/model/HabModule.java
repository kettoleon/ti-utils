package com.github.kettoleon.ti.meta.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Optional;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HabModule extends MetaObject {

    private boolean coreModule;

    private String habType;

    private boolean onePerHab;

    private boolean automated;

    private boolean allowsShipConstruction;

    private boolean allowsResupply;

    private boolean mine;

    private boolean noBuild;

    private boolean destroyed;

    private int tier;

    private String upgradesFromName;

    private String requiredProjectName;

    private int crew;

    private double power;

    //TODO is always zero, so removed for now: private int reserved;

    @JsonProperty("baseMass_tons")
    private double massTons;

    @JsonProperty("buildTime_Days")
    private double buildTimeDays;


    public Optional<Project> getRequiredProject() {
        return getReference(requiredProjectName, Project.class);
    }

    public Optional<HabModule> getUpgradesFrom() {
        return getReference(upgradesFromName, HabModule.class);
    }

    @JsonProperty("incomeMoney_month")
    private double incomeMoney;

    @JsonProperty("incomeInfluence_month")
    private double incomeInfluence;

    @JsonProperty("incomeOps_month")
    private double incomeOps;

    @JsonProperty("incomeResearch_month")
    private double incomeResearch;

    @JsonProperty("incomeProjects")
    private int incomeProjects;

    private int missionControl;

    @JsonProperty("incomeNobles_month")
    private double incomeNobles;
    @JsonProperty("incomeFissiles_month")
    private double incomeFissiles;
    @JsonProperty("incomeAntimatter_month")
    private double incomeAntimatter;
    @JsonProperty("incomeExotics_month")
    private double incomeExotics;

    private double spaceCombatValue;

    private double constructionTimeModifier;

    private double miningModifier;

    @JsonProperty("supportMaterials_month")
    private SupportMaterials supportMaterials;

    public boolean isAlienTech() {
        return "Project_AlienMasterProject".equals(requiredProjectName);
    }

    public double getBalanceMoney(){
        return getIncomeMoney()-getSupportMaterials().getMoney();
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SupportMaterials {
        private double money;
        private double boost;
        private double water;
        private double volatiles;
        private double metals;
        private double nobleMetals;
        private double fissiles;
    }

}
