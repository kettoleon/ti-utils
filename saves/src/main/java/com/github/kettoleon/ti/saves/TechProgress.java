package com.github.kettoleon.ti.saves;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.kettoleon.ti.meta.model.Tech;

import java.util.Map;

public class TechProgress extends StateObject {

    public TechProgress(SaveFile saveFile, JsonNode node) {
        super(saveFile, node);
    }

    public Tech getTech() {
        return getMetaReference("techTemplateName", Tech.class).orElseThrow();
    }

    public double getAccumulatedResearch() {
        return getDouble("accumulatedResearch");
    }


    public Map<Faction, Double> getFactionContributions() {
        return getReferenceMap(Faction.class, Double.class, "factionContributions");


    }


    public Faction getWinningFaction() {
        Faction winning = null;
        double winningValue = 0;
        for (Map.Entry<Faction, Double> contrib : getFactionContributions().entrySet()) {
            if (contrib.getValue() > winningValue) {
                winning = contrib.getKey();
                winningValue = contrib.getValue();
            }
        }
        return winning;
    }

    public boolean winningFactionCantLose() {
        return getRemainingResearchForCompletion() < getDistanceFromWinnerContributionToSecond();
    }

    public double getDistanceFromWinnerContributionToSecond() {
        return getMaxContribution() - getSecondMaxContribution();
    }

    private double getSecondMaxContribution() {
        Faction wf = getWinningFaction();
        return getFactionContributions().entrySet().stream().filter(e -> !e.getKey().equals(wf)).mapToDouble(Map.Entry::getValue).max().getAsDouble();
    }

    private double getMaxContribution() {
        return getFactionContributions().values().stream().mapToDouble(Double::doubleValue).max().getAsDouble();
    }

    public double getRemainingResearchForCompletion() {
        return getTech().getResearchCost() - getAccumulatedResearch();
    }
}
