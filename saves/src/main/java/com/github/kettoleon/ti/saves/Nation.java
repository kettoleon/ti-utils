package com.github.kettoleon.ti.saves;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Nation extends StateObject {
    public Nation(SaveFile saveFile, JsonNode node) {
        super(saveFile, node);
    }

    //improveRelationsCooldowns
    //historyPublicOpinion
//    historyInvestmentPoints
//            historyResearch
//    historyMiltech
//            historyMissionControl
//    historyBoost
//            historyPopulation
//    historyEducation
//            historySpaceFunding
//    historyGDP
//            historyInequality
//    historyUnrest
//            historyDemocracy
//    historyCohesion
    //federation
//controlPoints
//                        "advisingCouncilors": [],
//                                "numControlPoints": 1,
//                                "numControlPoints_unclamped": 1,
//                                "economyScore": 3.510818,
//                                "spaceFlightProgram": true,
//                                "nuclearProgram": false,
//                                "numNuclearWeapons": 0,
//                                "maxMilitaryTechLevel": 7.5,
//                                "GDP": 44952332409.5711,
//                                "inequality": 3.517621,
//                                "education": 8.674796,
//                                "democracy": 4.658445,
//                                "cohesion": 2.22397,
//                                "unrest": 7.177958,
//                                "militaryTechLevel": 3.567396,
//                                "popGrowthModifier": 0.0,
//                        "publicOpinion": {
//        "Submit": 0.01564174,
//                "Appease": 0.022485,
//                "Cooperate": 0.03128347,
//                "Exploit": 0.09800525,
//                "Escape": 0.01979657,
//                "Resist": 0.3340977,
//                "Destroy": 0.322122,
//                "Undecided": 0.1565683
//    },
    //claims
    //regions
    //lastExecutiveChange

    public LocalDateTime getLastExecutiveChangeDate() {
        return getLocalDateTime("lastExecutiveChange", "date");
    }

    public Optional<Faction> getLastExecutiveChangeFaction() {
        return getReference(Faction.class, "lastExecutiveChange", "newExecutive");
    }


    public List<Nation> getAllies() {
        return getReferenceArray("allies", Nation.class);
    }

    public List<Nation> getWars() {
        return getReferenceArray("wars", Nation.class);
    }

    public List<Nation> getRivals() {
        return getReferenceArray("rivals", Nation.class);
    }

    public List<Region> getClaims() {
        return getReferenceArray("claims", Region.class);
    }

    public List<Region> getMissingClaims() {
        return getReferenceArray("claims", Region.class).stream().filter(r -> r.getNation().map(Nation::getId).orElse(getId()) != getId()).collect(Collectors.toList());
    }

    public Optional<Federation> getFederation() {
        return getReference(Federation.class, "federation");
    }

    public boolean isExecutiveControlConsolidated() {
        return getExecutiveCooldownInDays() == 0;
    }

    public int getExecutiveCooldownInDays() {
        long cc = 180 - Duration.between(getLastExecutiveChangeDate(), getSaveFile().getCurrentDate().atStartOfDay()).toDays();

        return (int) Math.max(0, cc);
    }

    public LocalDateTime getExecutiveCooldownDate() {
        return getLastExecutiveChangeDate().plusDays(180);
    }


    public boolean isNotAlliedWith(Nation n) {
        return !isAlliedWith(n);
    }

    public boolean isAlliedWith(Nation n) {
        return getAllies().contains(n);
    }

    public boolean isRivalOrAtWarWithAny(List<Nation> members) {
        return !getRivalOrAtWarWith(members).isEmpty();
    }

    public List<Nation> getRivalOrAtWarWith(List<Nation> members) {
        List<Nation> rivalOrAtWarWith = new ArrayList<>();
        for (Nation fedNation : members) {
            if (getRivals().contains(fedNation)) {
                rivalOrAtWarWith.add(fedNation);
            }
            if (getWars().contains(fedNation)) {
                rivalOrAtWarWith.add(fedNation);
            }
        }
        return rivalOrAtWarWith;
    }


    public List<Cooldown> getDiplomaticCooldowns() {
        List<Cooldown> cooldowns = new ArrayList<>();

        getNode().path("improveRelationsCooldowns").forEach(jn -> cooldowns.add(new Cooldown(getSaveFile(), jn)));

        return cooldowns;
    }

    public Optional<Cooldown> getCooldownWith(Nation targetNation) {
        return getDiplomaticCooldowns().stream()
                .filter(dc -> dc.getNation().equals(targetNation))
                .filter(dc -> dc.getDate().isAfter(getSaveFile().getCurrentDate().atStartOfDay()))
                .findFirst();
    }

    public static class Cooldown extends StateObject {

        public Cooldown(SaveFile saveFile, JsonNode node) {
            super(saveFile, node);
        }


        public Nation getNation() {
            return getReference(Nation.class, "Key", "value").orElseThrow();
        }

        public LocalDateTime getDate() {
            if (getNode().path("Value").has("$ref")) {
                int cooldownRef = getInt("Value", "$ref");
                for (Nation n : getSaveFile().getNationsThatCurrentlyExist()) {
                    for (Cooldown c : n.getDiplomaticCooldowns()) {
                        if (c.getCooldownId() == cooldownRef) {
                            return c.getDate();
                        }
                    }
                }
            } else {
                return getLocalDateTime("Value");
            }
            throw new RuntimeException("Could not derive date for cooldown");
        }

        private int getCooldownId() {
            return getInt("Value", "$id");
        }

    }
}
