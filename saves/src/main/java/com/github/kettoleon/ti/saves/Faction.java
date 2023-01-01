package com.github.kettoleon.ti.saves;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.kettoleon.ti.meta.model.Project;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Faction extends StateObject {

    private HashMap<Integer, Double> intelIndex;
    private ArrayList<Councilor> intelOnCouncilors;

    public Faction(SaveFile saveFile, JsonNode node) {
        super(saveFile, node);
    }

    //TODO to check if another faction is an enemy in order to not warn about assassination skills of friendly agents
    //factionHate
    //WarOnFaction
    //TruceWithFaction
    //NonAggressionPact
    //habSectors
    //fleets
    //unassignedOrgs

    public List<String> getFinishedProjectNames(){
        return getStringList("finishedProjectNames");
    }

    public List<Project> getFinishedProjects(){
        return getMetaReferenceArray("finishedProjectNames", Project.class);
    }

    public String getMostPowerfulHumanEnemy() {
        return getNode().path("mostPowerfulHumanEnemy").toPrettyString() + "";
    }

    public List<ProjectProgress> getCurrentProjectProgress() {
        List<ProjectProgress> projectProgresses = new ArrayList<>();
        for (JsonNode cpp : getNode().path("currentProjectProgress")) {

            projectProgresses.add(new ProjectProgress(getSaveFile(), cpp));


        }
        return projectProgresses;
    }

    public List<Project> getSabotagedProjects() {
        return getMetaReferenceArray("sabotagedProjects", Project.class);
    }

    public double getAlienHate() {
        return getFactionHate(getSaveFile().getAlienFaction());
    }

    public double getFactionHate(Faction faction) {
        for (JsonNode fhn : getNode().path("factionHate")) {
            if (faction.getId() == fhn.path("Key").path("value").asInt()) {
                return fhn.path("Value").asDouble();
            }
        }
        return -1;
    }

    public Player getPlayer() {
        return getSaveFile().getPlayerById(getNode().path("player").path("value").asInt());
    }

    private Map<Integer, Double> getIntelForType(String type) {

        intelIndex = new HashMap<>();
        getNode().path("intel").forEach(i -> {
            int knownId = i.path("Key").path("value").asInt();
            String knownType = i.path("Key").path("$type").asText();
            if (knownType.equals(type)) {
                intelIndex.put(knownId, i.path("Value").asDouble());
            }
        });
        return intelIndex;
    }

    public List<Councilor> getIntelOnCouncilors() {
        if (intelOnCouncilors == null) {
            intelOnCouncilors = new ArrayList<>();
            getIntelForType("PavonisInteractive.TerraInvicta.TICouncilorState").forEach((k, v) -> {
                if (v > 0.1) {
//                    log.info("{} {}", saveFile.getCouncilorById(k).get().getDisplayName(), v);
                    getSaveFile().getCouncilorById(k).ifPresentOrElse(intelOnCouncilors::add, () -> log.warn("Could not link councilor intel with id: " + k));
                }
            });
        }
        return intelOnCouncilors;
    }

    public List<Councilor> getHiredCouncilors() {
        return getReferenceArray("councilors", Councilor.class).stream().sorted(Comparator.comparing(Councilor::getId)).collect(Collectors.toList());
    }

    public List<Councilor> getAvailableCouncilors() {
        return getReferenceArray("availableCouncilors", Councilor.class).stream().sorted(Comparator.comparing(Councilor::getId)).collect(Collectors.toList());
    }

    public List<Organisation> getAvailableOrganisations() {
        return getReferenceArray("availableOrgs", Organisation.class).stream().sorted(Comparator.comparing(Organisation::getTier).reversed()).collect(Collectors.toList());
    }

    public List<Organisation> getStealableOrganisations() {
        return getIntelOnExternalCouncilors().stream()
                .filter(c -> !c.isAlien())
                .flatMap(ec -> ec.getOrganisations().stream())
                .filter(org -> !org.isFactionOrganisation())
                .sorted(Comparator.comparing(Organisation::getTier).reversed())
                .collect(Collectors.toList());
    }

    public List<Organisation> getObtainableOrganisations() {
        ArrayList<Organisation> organisations = new ArrayList<>();
        organisations.addAll(getAvailableOrganisations());
        organisations.addAll(getStealableOrganisations());
        return organisations.stream()
                .filter(org -> !org.getMetadata().getRestrictedFactionTemplateNames().contains(getTemplateName()))
                .sorted(Comparator.comparing(Organisation::getTier).reversed())
                .collect(Collectors.toList());
    }

    public List<Councilor> getIntelOnExternalCouncilors() {
        return getIntelOnCouncilors().stream()
                .filter(c -> c.getFaction().isPresent())
                .filter(c -> !c.getFaction().get().isPlayer())
                .collect(Collectors.toList());
    }


    public boolean isPlayer() {
        return !getPlayer().isAI();
    }

    public boolean isServants() {
        return getTemplateName().equals("SubmitCouncil");
    }

    public boolean isProtectorate() {
        return getTemplateName().equals("AppeaseCouncil");
    }

    public boolean isAlien() {
        return getTemplateName().equals("AlienCouncil");
    }

    @Override
    public String getDisplayName() {
        String displayName = super.getDisplayName();
        return (displayName.charAt(0) + "").toUpperCase() + displayName.substring(1);
    }

    public Optional<ProjectProgress> getSabotageableProjectProgress(Project project) {
        return getSabotageableProjectProgress().stream().filter(pp -> pp.getProject().map(p -> p.equals(project)).orElse(false)).findFirst();
    }

    public List<ProjectProgress> getSabotageableProjectProgress() {
        return getCurrentProjectProgress().stream()
                .filter(pp -> pp.getProject().map(prj -> getSabotagedProjects().stream().noneMatch(sp -> sp.getDataName().equals(prj.getDataName()))).orElse(false)
                ).collect(Collectors.toList());
    }

    //TODO how to detect which factions are enemies?
    public static final List<String> ENEMIES = Arrays.asList("AppeaseCouncil", "SubmitCouncil");

    public boolean isEnemy() {
        return ENEMIES.contains(getTemplateName());
    }
}
