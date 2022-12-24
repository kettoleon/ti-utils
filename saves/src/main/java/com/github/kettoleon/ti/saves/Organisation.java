package com.github.kettoleon.ti.saves;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.kettoleon.ti.meta.model.Org;
import com.github.kettoleon.ti.meta.model.trait.Trait;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.function.Supplier;

import static java.util.stream.Collectors.joining;

public class Organisation extends StateObject {

    //TODO read from template
    private static final List<String> factionSpaceOrgs = Arrays.asList(
            "Asterion Stellar Systems",
            "Gannet Exploration",
            "New Epoch",
            "Orbital Ventures",
            "Shiva Rocketry",
            "Sahar Launch Systems",
            "Spaceguard Section"
    );

    private static final List<String> factionCouncilOrgs = Arrays.asList(
            "The Executioners",
            "Project Exodus Engineers",
            "Academy Envoys",
            "Pherocyte Deployment Team",
            "Reeducation Team",
            "Special Activities Section",
            "The Shield of the Devoted"
    );

    private static final List<String> factionVictoryOrgs = Arrays.asList(
            "Bifrost Command Staff",
            "Janus Section",
            "Mission to the Hydra",
            "The Omega Solution",
            "The Pherocyte Masters",
            "The Protectorate Authority",
            "The Sword of the Devoted"
    );

    public Organisation(SaveFile saveFile, JsonNode node) {
        super(saveFile, node);
    }


    public Org getMetadata() {
        return getMetadata(Org.class);
    }

    public Optional<Councilor> getAssignedCouncilor() {
        return getReference(Councilor.class, "assignedCouncilor");
    }

    public boolean isFactionOrganisation() {
        return factionCouncilOrgs.contains(getDisplayName()) || factionVictoryOrgs.contains(getDisplayName()) || factionSpaceOrgs.contains(getDisplayName());
    }

    public int getTier() {
        return getInt("tier");
    }

    public double getCostMoney() {
        return getDouble("costMoney");
    }

    public double getCostInfluence() {
        return getDouble("costInfluence");
    }

    public double getCostOps() {
        return getDouble("costOps");
    }

    public double getCostBoost() {
        return getDouble("costBoost");
    }

    public double getIncomeMoneyMonth() {
        return getDouble("incomeMoney_month");
    }

    public double getIncomeInfluenceMonth() {
        return getDouble("incomeInfluence_month");
    }

    public double getIncomeOpsMonth() {
        return getDouble("incomeOps_month");
    }

    public double getIncomeBoostMonth() {
        return getDouble("incomeBoost_month");
    }

    public double getIncomeMissionControl() {
        return getDouble("incomeMissionControl");
    }

    public double getIncomeResearchMonth() {
        return getDouble("incomeResearch_month");
    }

    public int getProjectCapacityGranted() {
        return getInt("projectCapacityGranted");
    }

    public int getPersuasion() {
        return getInt("persuasion");
    }

    public int getCommand() {
        return getInt("command");
    }

    public int getInvestigation() {
        return getInt("investigation");
    }

    public int getEspionage() {
        return getInt("espionage");
    }

    public int getAdministration() {
        return getInt("administration");
    }

    public int getScience() {
        return getInt("science");
    }

    public int getSecurity() {
        return getInt("security");
    }

    public double getEconomyBonus() {
        return getDouble("economyBonus");
    }

    public double getWelfareBonus() {
        return getDouble("welfareBonus");
    }

    public double getKnowledgeBonus() {
        return getDouble("knowledgeBonus");
    }

    public double getUnityBonus() {
        return getDouble("unityBonus");
    }

    public double getMilitaryBonus() {
        return getDouble("militaryBonus");
    }

    public double getSpoilsBonus() {
        return getDouble("spoilsBonus");
    }

    public double getSpaceDevBonus() {
        return getDouble("spaceDevBonus");
    }

    public double getSpaceflightBonus() {
        return getDouble("spaceflightBonus");
    }

    public double getMiningBonus() {
        return getDouble("miningBonus");
    }

    public double getXPModifier() {
        return getDouble("XPModifier");
    }

    public double getPersuasionPerTier() {
        return getPersuasion() / getTier();
    }

    public double getInvestigationPerTier() {
        return getInvestigation() / getTier();
    }

    public double getEspionagePerTier() {
        return getEspionage() / getTier();
    }

    public double getCommandPerTier() {
        return getCommand() / getTier();
    }

    public double getAdministrationPerTier() {
        return getAdministration() / getTier();
    }

    public double getSciencePerTier() {
        return getScience() / getTier();
    }

    public double getSecurityPerTier() {
        return getSecurity() / getTier();
    }

    public double getMiningBonusPerTier() {
        return getMiningBonus() / getTier();
    }

    public double getProjectCapacityPerTier() {
        return getProjectCapacityGranted() / getTier();
    }


    public Optional<Faction> getFactionOrbit() {
        return getReference(Faction.class, "factionOrbit");
    }

    public boolean isGenerallyInteresting() {
        return !hasNegativeIncome() && doublesSomePerTier();
    }

    private boolean doublesSomePerTier() {
        return getSecurityPerTier() >= 2 || getAdministrationPerTier() >= 2 || getCommandPerTier() >= 2 || getEspionagePerTier() >= 2 || getInvestigationPerTier() >= 2 || getPersuasionPerTier() >= 2 || getSciencePerTier() >= 2 || getProjectCapacityPerTier() >= 2;
    }

    public boolean hasNegativeIncome() {
        return getIncomeMoneyMonth() < 0 || getIncomeBoostMonth() < 0 || getIncomeInfluenceMonth() < 0 || getIncomeOpsMonth() < 0 || getIncomeResearchMonth() < 0 || getIncomeMissionControl() < 0;
    }

    public boolean isShittyOrg() {
        return getSecurityPerTier() < 1 && getAdministrationPerTier() < 1 && getCommandPerTier() < 1 && getEspionagePerTier() < 1 && getInvestigationPerTier() < 1 && getPersuasionPerTier() < 1 && getSciencePerTier() < 1 && getProjectCapacityPerTier() < 1;

    }


    public Map<String, Integer> getGrantedAttributes() {
        Map<String, Integer> grantedAttributes = new HashMap<>();

        addAttributeIfNeeded(grantedAttributes, this::getPersuasion,"Persuasion");
        addAttributeIfNeeded(grantedAttributes, this::getInvestigation,"Investigation");
        addAttributeIfNeeded(grantedAttributes, this::getEspionage,"Espionage");
        addAttributeIfNeeded(grantedAttributes, this::getCommand,"Command");
        addAttributeIfNeeded(grantedAttributes, this::getAdministration,"Administration");
        addAttributeIfNeeded(grantedAttributes, this::getScience,"Science");
        addAttributeIfNeeded(grantedAttributes, this::getSecurity,"Security");

        return grantedAttributes;
    }

    private void addAttributeIfNeeded(Map<String, Integer> grantedAttributes, Supplier<Integer> accessor, String attrName) {
        int value = accessor.get();
        if (value > 0) {
            grantedAttributes.put(attrName, value);
        }
    }
}
