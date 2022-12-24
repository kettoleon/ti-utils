package com.github.kettoleon.ti.saves.auto;

import com.github.kettoleon.ti.saves.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AutoSaveAlerts {


    public static void main(String[] args) throws IOException {

        Path savefiles = Path.of("C:\\Users\\enrodri86\\Documents\\My Games\\TerraInvicta\\Saves");

        Files.list(savefiles).filter(p -> p.getFileName().toString().toLowerCase().contains("trainerdec"))
                .forEach(p -> {
                    System.out.println(p.getFileName());
                    buildingDeserializer(p);
                });


//        buildingDeserializer(Path.of("C:\\Users\\enrodri86\\Documents\\My Games\\TerraInvicta\\Saves\\Autosave2.gz"));


//        new AutoSaveFileWatcher(Path.of("C:\\Users\\enrodri86\\Documents\\My Games\\TerraInvicta\\Saves"), f -> {
//
//            buildingDeserializer(f);
//
//
//        }).
//
//                run();
    }

    private static void buildingDeserializer(Path f) {
//        System.out.println("========== AUTOSAVE FILE ==========");

//        SaveFile saveFile = new SaveFile(f, meta);
//
////        System.out.println("- SaveFileId: " + saveFile.getId());
//
//        System.out.println("- Time: " + saveFile.getSingletonStateObject(TimeState.class).getCurrentDateTime());
//        System.out.println("- Alien hate: " + saveFile.getPlayerFaction().getAlienHate());

//        assassinsAlert(saveFile);
//
//        orgsAlert(saveFile);
    }

//    private static void orgsAlert(SaveFile saveFile) {
//        System.out.println("Generally Interesting orgs:");
//        saveFile.getObtainableOrganisations().stream()
//                .filter(org -> org.isGenerallyInteresting())
//                .sorted(Comparator.comparing(Organisation::getTier).reversed().thenComparing(Organisation::getCostMoney).thenComparing(Organisation::getCostInfluence))
//                .forEach(System.out::println);
//        System.out.println("====================");
//        System.out.println();
//        System.out.println("Best available Security orgs:");
//        saveFile.getObtainableOrganisations().stream()
//                .filter(org -> org.getSecurity() > 0)
//                .filter(org -> org.getSecurityPerTier() >= 1)
//                .sorted(Comparator.comparing(Organisation::getSecurityPerTier).reversed().thenComparing(Organisation::getSecurity).reversed())
//                .forEach(System.out::println);
//    }

    private static void assassinsAlert(SaveFile saveFile) {
        saveFile.getFactions().values().forEach(faction -> {
            if (faction.isPlayer()) {

                List<Councilor> worryingAssassins = new ArrayList<>();
                for (Councilor intelOnCouncilor : faction.getIntelOnExternalCouncilors()) {
                    Faction councilorFaction = intelOnCouncilor.getFaction().get();
                    if (intelOnCouncilor.canAssassinate() && (councilorFaction.isServants() || councilorFaction.isProtectorate())) { //TODO autodetect which ones you are watching for
                        worryingAssassins.add(intelOnCouncilor);
                    }
                }
                if (worryingAssassins.size() > 0) {
                    System.out.println("- Known Assassin Councilors:");
                    for (Councilor assassin : worryingAssassins) {
                        System.out.println("  - " + assassin.getDisplayName() + " | " + assassin.getFaction().get().getDisplayName());
                    }
                }
            }
        });
    }
}