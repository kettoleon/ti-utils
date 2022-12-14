package com.github.kettoleon.ti.meta.model.trait;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.kettoleon.ti.meta.model.MetaObject;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Trait extends MetaObject {

    private String dataName;
    private String friendlyName;
    @JsonProperty("XPCost")
    private int XPCost;
    private int grouping;
    private int incomeMoney;
    private int incomeInfluence;
    private int incomeOps;
    private int incomeBoost;
    private int incomeResearch;

    private int detectionInvBonus;

    private int detectionEspBonus;
    @JsonProperty("XPModifier")
    private float XPModifier;

    private String projectDataName;

    private List<String> restrictedMissionNames = new ArrayList<>();
    private List<String> missionsGrantedNames = new ArrayList<>();
    private String restrictedLocations;

    private List<StatMod> statMods = new ArrayList<>();

    public boolean canBeRemoved() {
        return XPCost < 0;
    }

    public boolean hasRestrictions() {
        return hasRestrictedMissions() || StringUtils.isNotBlank(restrictedLocations);
    }

    private boolean hasGrantedMissions() {
        for (String restriction : missionsGrantedNames) {
            if (StringUtils.isNotBlank(restriction)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasRestrictedMissions() {
        for (String restriction : restrictedMissionNames) {
            if (StringUtils.isNotBlank(restriction)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCrippling() {
        if (isNegative()) {
            return !canBeRemoved();
        }
        if (isMixed()) {
            return hasRestrictions() && !canBeRemoved();
        }

        return false;
    }


    public boolean isPositive() {
        if(getFriendlyName().equals("Government") || getFriendlyName().equals("Transparent")){
            //TODO hate to have special cases
            return true;
        }
        return hasPositive() && !hasNegative() && !hasRestrictions();
    }

    public boolean hasPositive() {
        return incomeMoney > 0 || incomeInfluence > 0 || incomeOps > 0 || incomeBoost > 0 || incomeResearch > 0 || detectionInvBonus > 0 || detectionEspBonus > 0 || XPModifier < 0 || hasPositiveStatMod();
    }

    private boolean hasPositiveStatMod() {
        for (StatMod m : statMods) {
            if (m.isPositive()) {
                return true;
            }
        }
        return false;
    }

    private boolean hasNegativeStatMod() {
        for (StatMod m : statMods) {
            if (m.isNegative()) {
                return true;
            }
        }
        return false;
    }

    public boolean isMixed() {
        return hasPositive() && (hasNegative() || hasRestrictions());
    }

    private boolean hasNegative() {
        return incomeMoney < 0 || incomeInfluence < 0 || incomeOps < 0 || incomeBoost < 0 || incomeResearch < 0 || detectionInvBonus < 0 || detectionEspBonus < 0 || XPModifier > 0 || hasNegativeStatMod();
    }

    public boolean isNegative() {
        if (hasPositive()) {
            return false;
        }
        return hasNegative() || hasRestrictions();
    }

    public boolean isFavorite() {
        return isPositive() && (hasStatIncome("Security") || hasStatIncome("Loyalty") || XPModifier < 0 || hasGrantedMissions());
    }

    private boolean hasStatIncome(String stat) {
        for (StatMod m : statMods) {
            if (m.getStat().equals(stat)) {
                return m.isPositive();
            }
        }
        return false;
    }

    public boolean isObtainedThroughTechnology() {
        return StringUtils.isNotBlank(projectDataName);
    }



}
