package com.github.kettoleon.ti.saves;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.kettoleon.ti.meta.model.CouncilorType;
import com.github.kettoleon.ti.meta.model.trait.StatMod;
import com.github.kettoleon.ti.meta.model.trait.Trait;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
public class Councilor extends StateObject {

    public Councilor(SaveFile saveFile, JsonNode node) {
        super(saveFile, node);
    }

    //faction
    //agentForFaction

    public Optional<Faction> getFaction() {
        return getReference(Faction.class, "faction");
    }

    public boolean canAssassinate() {
        return isAssassinType() || hasAssassinOrg();
    }

    public String getGender() {
        return getString("gender");
    }

    public String getHimOrHer() {
        return getGender().equals("Male") ? "him" : "her";
    }

    public String getHeOrShe() {
        return getGender().equals("Female") ? "she" : "he";
    }

    public boolean isAssassinType() {
        return getType().hasMission("Assassinate");
    }

    public boolean hasAssassinOrg() {
        return getOrganisations().stream().anyMatch(org -> org.getMetadata().hasGrantedMission("Assassinate"));
    }


    public String getCountry() {
        return getReference(Region.class, "homeRegion").flatMap(Region::getNation).map(Nation::getDisplayName).orElse("404");
    }

    public boolean isAlien() {
        return getType().getDataName().equals("Alien");

    }

    public boolean hasEverBeenAvailable() {
        return getBoolean("everBeenAvailable");
    }

    public boolean hasRecruitDate() {
        return getNode().hasNonNull("recruitDate");
    }

    public int getAge() {
        return Period.between(getBirthday(), getSaveFile().getCurrentDate()).getYears();
    }

    public LocalDate getBirthday() {
        return getLocalDateTime("dateBorn").toLocalDate();
    }

    public int getPersuasion() {
        return computeStat("Persuasion");
    }

    private int computeStat(String stat) {
        return Math.max(0, getInt("attributes", stat) + getStatModifiers(stat));
    }

    private int getStatModifiers(String stat) {
        int mod = 0;
        for (Trait trait : getTraits()) {
            for (StatMod sm : trait.getStatMods()) {
                if (sm.getStat().equals(stat) && sm.getOperation().equals("Additive") && !sm.hasCondition()) {
                    mod += sm.getStrValueAsInt();
                }
            }
        }
        for (Organisation organisation : getOrganisations()) {
            Map<String, Integer> attributes = organisation.getGrantedAttributes();
            for (Map.Entry<String, Integer> sm : attributes.entrySet()) {
                if (sm.getKey().equals(stat)) {
                    mod += sm.getValue();
                }
            }
        }
        return mod;
    }

    public List<Trait> getTraits() {
        return getMetaReferenceArray("traitTemplateNames", Trait.class);
    }

    public List<Trait> getNonTechTraits() {
        return getMetaReferenceArray("traitTemplateNames", Trait.class).stream().filter(trait -> !trait.isObtainedThroughTechnology()).collect(Collectors.toList());
    }

    public int getEspionage() {
        return computeStat("Espionage");
    }

    public int getCommand() {
        return computeStat("Command");
    }

    public int getInvestigation() {
        return computeStat("Investigation");
    }

    public int getScience() {
        return computeStat("Science");
    }

    public int getAdministration() {
        return computeStat("Administration");
    }

    public int getSecurity() {
        return computeStat("Security");
    }

    public int getLoyalty() {
        return computeStat("Loyalty");
    }

    public int getApparentLoyalty() {
        return computeStat("ApparentLoyalty");
    }

    public int getControlPoints() {
        return getPersuasion() + getCommand() + getAdministration();
    }


    public CouncilorType getType() {
        return getMetaReference("typeTemplateName", CouncilorType.class).orElseThrow();
    }

    public boolean hasTypeTopStat() {
        List<String> keyStat = getType().getKeyStat();
        for (String stat : keyStat) {
            int max = getType().getTopStartingValueForStat(stat);
            int current = getStat(stat);
            if (current >= max) {
                return true;
            }
        }
        return false;
    }

    private int getStat(String stat) {
        switch (stat) {
            case "Persuasion":
                return getPersuasion();
            case "Investigation":
                return getInvestigation();
            case "Espionage":
                return getEspionage();
            case "Command":
                return getCommand();
            case "Administration":
                return getAdministration();
            case "Science":
                return getScience();
            case "Security":
                return getSecurity();
            case "Loyalty":
                return getLoyalty();
            case "ApparentLoyalty":
                return getApparentLoyalty();
            default:
                return 0;
        }
    }

    public List<Organisation> getOrganisations() {
        return getReferenceArray("orgs", Organisation.class);
    }

    public Optional<Region> getLocation() {
        return getReference(Region.class, "location");
    }

    public int getExperience() {
        return getInt("XP");
    }

    public double getExperienceMultiplier() {
        double modifier = 1;
        for (Trait trait : getTraits()) {
            modifier += trait.getXPModifier();
        }
        return modifier;
    }

    public boolean hasRemovableNegativeTrait() {
        for (Trait t : getTraits()) {
            if (t.isNegative() && t.canBeRemoved()) {
                return true;
            }
        }
        return false;
    }

    public Map<String, Integer> getAttributesTotal() {
        Map<String, Integer> attrs = new HashMap<>();

        addAttribute(attrs, this::getPersuasion, "Persuasion");
        addAttribute(attrs, this::getInvestigation, "Investigation");
        addAttribute(attrs, this::getEspionage, "Espionage");
        addAttribute(attrs, this::getCommand, "Command");
        addAttribute(attrs, this::getAdministration, "Administration");
        addAttribute(attrs, this::getScience, "Science");
        addAttribute(attrs, this::getSecurity, "Security");

        return attrs;
    }

    private void addAttribute(Map<String, Integer> grantedAttributes, Supplier<Integer> accessor, String attrName) {
        grantedAttributes.put(attrName, accessor.get());
    }

    public boolean learnsQuickly() {
        return getTraits().stream().anyMatch(t -> t.getXPModifier() < 0);
    }
}
