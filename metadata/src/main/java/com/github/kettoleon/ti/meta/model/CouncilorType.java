package com.github.kettoleon.ti.meta.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CouncilorType extends MetaObject {

    //TODO Hardcoded for now, but we could get the metadata and find which ones have all missions that use a given attribute, and max possible level, the hated would be the ones that do not fit any category
    private static final List<String> LOVED_COUNCILOR_TYPE_TEMPLATES = Arrays.asList("Evangelist", "Celebrity", "Investigator", "Inspector", "Judge", "Spy", "Officer", "Tycoon", "Scientist");
    private static final List<String> HATED_COUNCILOR_TYPE_TEMPLATES = Arrays.asList("Diplomat", "Journalist", "Rebel", "Hacker", "Politician", "Activist");

    private String dataName;
    private String friendlyName;

    private int basePersuasion;
    private int randPersuasion;
    private int baseCommand;
    private int randCommand;
    private int baseEspionage;
    private int randEspionage;
    private int baseInvestigation;
    private int randInvestigation;
    private int baseAdministration;
    private int randAdministration;
    private int baseScience;
    private int randScience;
    private int baseSecurity;
    private int randSecurity;
    private int baseLoyalty;
    private int randLoyalty;

    private List<String> missionNames = new ArrayList<>();
    private List<String> keyStat = new ArrayList<>();

    public boolean isFavorite() {
        return LOVED_COUNCILOR_TYPE_TEMPLATES.contains(dataName);
    }

    public boolean isHated() {
        return HATED_COUNCILOR_TYPE_TEMPLATES.contains(dataName);
    }

    public boolean hasMission(String missionName) {
        return missionNames.contains(missionName);
    }

    public int getTopStartingValueForStat(String stat) {
        switch (stat) {
            case "Persuasion":
                return basePersuasion + randPersuasion;
            case "Investigation":
                return baseInvestigation + randInvestigation;
            case "Espionage":
                return baseEspionage + randEspionage;
            case "Command":
                return baseCommand + randCommand;
            case "Administration":
                return baseAdministration + randAdministration;
            case "Science":
                return baseScience + randScience;
            case "Security":
                return baseSecurity + randSecurity;
            case "Loyalty":
                return baseLoyalty + randLoyalty;
            default:
                return Integer.MAX_VALUE;
        }
    }
}
