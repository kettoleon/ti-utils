package com.github.kettoleon.ti.dashboard;

import com.github.kettoleon.ti.dashboard.model.DashboardModel;
import com.github.kettoleon.ti.dashboard.model.alert.*;
import com.github.kettoleon.ti.meta.MetaRepository;
import com.github.kettoleon.ti.saves.*;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardController {

    private DashboardModel model;
    private MetaRepository meta;

    public DashboardController(DashboardModel model, MetaRepository meta) {

        this.model = model;
        this.meta = meta;
    }

    public void onSaveFileUpdated(Path f) {
        //TODO autodetect Exitsave and close too?

        SaveFile saveFile = new SaveFile(f, meta);
        model.setDate(saveFile.getCurrentDate());
        model.setHate(saveFile.getAlienFaction().getFactionHate(saveFile.getPlayerFaction()));
        model.setFactions(saveFile.getFactions());
        model.setNations(saveFile.getNationsThatCurrentlyExist());
        model.setCouncilors(saveFile.getCouncilors());
        monitorEnemyFactionResearchProgress();

        model.getAvailableCouncilors().setCouncilors(saveFile.getPlayerFaction().getAvailableCouncilors());
        model.getHiredCouncilors().setCouncilors(saveFile.getPlayerFaction().getHiredCouncilors());

        monitorCouncilorsForAlerts();

        model.getObtainableOrganisations().setOrganisations(saveFile.getPlayerFaction().getObtainableOrganisations());

        monitorOrganisationsForAlerts();


        monitorEnemyFactionAssassins(saveFile.getPlayerFaction().getIntelOnExternalCouncilors());

        monitorAvailableNationPolicies(saveFile.getPlayerFaction(), saveFile.getNationsThatCurrentlyExist(), saveFile.getFormedFederations());

        GlobalResearch gr = saveFile.getSingletonStateObject(GlobalResearch.class);

        model.setTechProgress(gr.getTechProgress());

        gr.getTechProgress().forEach(tp -> {
            model.getAlerts().addAlert(new TechProgressAlert(tp.getTech()));
        });

        System.out.println("Current date: " + model.getDate());
        System.out.println("Alien hate: " + model.getHate());
        System.out.println("Alien mostPowerfulHumanEnemy: " + saveFile.getAlienFaction().getMostPowerfulHumanEnemy());

        ProfileUtils.measure("updateAlerts", () -> model.getAlerts().updateAlerts(model));

    }

    private void monitorAvailableNationPolicies(Faction playerFaction, Collection<Nation> nations, List<Federation> federations) {
        List<Nation> playerNations = nations.stream()
                .filter(n -> n.getLastExecutiveChangeFaction().map(f -> f.getId() == playerFaction.getId()).orElse(false))
                .collect(Collectors.toList());

        playerNations.forEach(sourceNation -> {

            sourceNation.getMissingClaims().stream()
                    .flatMap(r -> r.getNation().stream())
                    .distinct()
                    .forEach(targetNation ->
                            model.getAlerts().addAlert(new NationClaimAlert(sourceNation.getId(), targetNation.getId()))
                    );
        });
    }

    private void monitorEnemyFactionAssassins(Collection<Councilor> councilors) {
        councilors.stream().filter(c -> c.getFaction().map(Faction::isEnemy).orElse(false) && c.canAssassinate()).forEach(ec -> {
            model.getAlerts().addAlert(new AssassinAlert(ec.getId()));
        });

    }


    private void monitorEnemyFactionResearchProgress() {

        for (Faction f : model.getFactions()) {
            if (!f.isPlayer()) {
                for (ProjectProgress pp : f.getCurrentProjectProgress()) {
                    pp.getProject().ifPresent(prj -> {
                        model.getAlerts().addAlert(new ProjectProgressAlert(f.getId(), prj));
                    });
                }
            }
        }
    }

    private void monitorOrganisationsForAlerts() {

        for (Organisation org : model.getObtainableOrganisations().getOrganisations()) {
            if (org.isGenerallyInteresting()) {
                model.getAlerts().addAndShowAlert(new InterestingOrganisationAlert(org));
            }
        }

    }


    private void monitorCouncilorsForAlerts() {

        for (Councilor available : model.getAvailableCouncilors().getCouncilors()) {
            if (available.getType().isFavorite() && available.hasTypeTopStat() && available.getAge() <= 40 && available.learnsQuickly()) {
                model.getAlerts().addAndShowAlert(new OutstandingCandidateAlert(available));
            }
        }

        for (Councilor hired : model.getHiredCouncilors().getCouncilors()) {
            model.getAlerts().addAlert(new LevelUpAlert(hired.getId()));
        }


    }


}