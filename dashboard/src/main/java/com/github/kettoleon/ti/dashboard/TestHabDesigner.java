package com.github.kettoleon.ti.dashboard;

import com.github.kettoleon.ti.meta.MetaRepository;
import com.github.kettoleon.ti.meta.model.HabModule;
import com.github.kettoleon.ti.meta.model.Project;
import com.github.kettoleon.ti.saves.SaveFile;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

public class TestHabDesigner {

    public static void main(String[] args) {

        MetaRepository mr = new MetaRepository(Path.of(args[0]));
        SaveFile save = new SaveFile(Path.of(args[1]),mr);

        List<Project> finishedProjects = save.getPlayerFaction().getFinishedProjects();

        mr.getAll(HabModule.class).stream()
                .filter(hm -> !hm.isDestroyed())
                .filter(hm -> hm.getTier() == 3)
                .filter(hm -> hm.getRequiredProject().map(finishedProjects::contains).orElse(true))
                .sorted(Comparator.comparing(HabModule::getBalanceMoney).reversed())
                .forEach( hm -> System.out.println(hm.getFriendlyName() + ": "+hm.getRequiredProjectName()));


        //TODO options:
        //Tier Level
        //Hab or Station
        //Maximize Station: [Diff. research | Money | ShipBuilding | Antimatter | Mc] | Hab: [Population]
        //Balanced or not
        //Money [ on | off ]
        // with nanofacturing (0, 1, or max), with geriatrics (enabled or disabled), with space resort (enabled or disabled)
        //Power [ on | off ]
        // Select orbit or power source (option for urgent build?)  [ Solar | Fission | Fusion ]
        //Water and volatiles [ on | off ]
        //Defense Num battlestations (min, max), num marine barracks (min, max)
        //Mc balancing [ on | off ]


//
//        int maxSlots = 20;
//
//        for (int i = 1; i <= maxSlots; i++) {
//            ComputeHab computeStation = new ComputeHab(i, 2);
//            computeStation.computeUntilStable();
//            if (computeStation.totalSlots() > maxSlots) {
//                break;
//            }
//
//            System.out.println(computeStation);
//        }

    }

    public static class ComputeHab extends ComputeStation {

        protected double colonyCoreWaterAndVolatilesCost = 3.6;
        protected double colonyCoreMoneyCost = 21;
        protected double colonyCoreMcCost = 5;

        protected double miningComplexMoneyCost = 61.7;
        protected double miningComplexMcCost = 3;
        protected double miningComplexEnergyCost = 154;
        protected double miningComplexWaterAndVolatilesCost = 10.8;

        protected double commandCenterMcCompensation = 2;
        protected int commandCentersNeeded = (int) Math.ceil((colonyCoreMcCost + miningComplexMcCost) / commandCenterMcCompensation);
        protected double commandCenterMoneyCost = 32.1;
        protected double commandCenterWaterAndVolatilesCost = 7.3;
        protected double commandCenterEnergyCost = 120;

        protected int nanofactoriesNeeded = 0;
        protected double nanofacturingMoneyCompensation = 300;
        protected double nanofacturingWaterAndVolatilesCost = 14.4;
        protected double nanofacturingEnergyCost = 120;
        protected double moneySurplus;

        public ComputeHab(int numBarracks, int numBattlestations) {
            super(0, 0, 0, numBarracks, numBattlestations);
        }

        protected int compute() {

            double waterAndVolatilesNeeded =
                    colonyCoreWaterAndVolatilesCost +
                            miningComplexWaterAndVolatilesCost +
                            commandCentersNeeded * commandCenterWaterAndVolatilesCost +
                            numBattlestations * battlestationsMaxWaterOrVolatilesCost +
                            numBarracks * marineBarracksMaxWaterOrVolatilesCost +
                            //now variables
                            reactorsNeeded * reactorSupportMaxWaterOrVolatiles +
                            nanofactoriesNeeded * nanofacturingWaterAndVolatilesCost;

            agricultureModulesNeeded = (int) Math.ceil(waterAndVolatilesNeeded / agricultureWaterAndVolatilesCompensation);
            double moneyNeeded = colonyCoreMoneyCost +
                    miningComplexMoneyCost +
                    commandCentersNeeded * commandCenterMoneyCost +
                    numBattlestations * battlestationsMoneyCost +
                    numBarracks * marineBarracksMoneyCost +
                    reactorsNeeded * reactorMoneyCost;
            nanofactoriesNeeded = (int) Math.ceil(moneyNeeded / nanofacturingMoneyCompensation);

            double energyNeeded = miningComplexEnergyCost +
                    commandCentersNeeded * commandCenterEnergyCost +
                    numBattlestations * battlestationsEnergyCost +
                    numBarracks * marineBarracksEnergyCost +
                    agricultureModulesNeeded * agricultureSupportEnergy +
                    nanofactoriesNeeded * nanofacturingEnergyCost;
            reactorsNeeded = (int) Math.ceil(energyNeeded / reactorEnergyCompensation);
            powerSurplus = (int) (reactorsNeeded * reactorEnergyCompensation - energyNeeded);
            farmsSurplus = (int) (agricultureModulesNeeded * agricultureWaterAndVolatilesCompensation - waterAndVolatilesNeeded);
            moneySurplus = (int) (nanofactoriesNeeded * nanofacturingMoneyCompensation - moneyNeeded);

            return totalSlots();
        }

        @Override
        public void computeUntilStable() {
            compute();
            int prevModules = compute();
            int newModules = compute();
            while (prevModules > newModules) {
                prevModules = newModules;
                newModules = compute();
            }
        }

        @Override
        protected int totalSlots() {
            return 1 + commandCentersNeeded + numBattlestations + numBarracks + agricultureModulesNeeded + nanofactoriesNeeded + reactorsNeeded;
        }

        public String toString() {
            StringWriter out = new StringWriter();
            PrintWriter sb = new PrintWriter(out);
            sb.println("==================");
            sb.println("Mining complex: 1");
            sb.println("Command Centers: " + commandCentersNeeded);
            sb.println("Farms: " + agricultureModulesNeeded);
            sb.println("Reactors: " + reactorsNeeded);
            sb.println("Marine Barracks: " + numBarracks);
            sb.println("Battlestations: " + numBattlestations);
            sb.println("Nanofactories: " + nanofactoriesNeeded);
            sb.println("Power surplus: " + powerSurplus);
            sb.println("Farm surplus: " + farmsSurplus);
            sb.println("Money surplus: " + moneySurplus);
            sb.println("Total: " + totalSlots());
            return out.toString();
        }

    }

    public static class ComputeStation {

        protected final int numModules;
        protected final double moduleEnergyCost;
        protected final double moduleMaxWaterOrVolatilesCost;

        protected double agricultureSupportEnergy = 120;
        protected double agricultureWaterAndVolatilesCompensation = 87;
        protected double reactorEnergyCompensation = 450;
        protected double reactorSupportMaxWaterOrVolatiles = 17.3;

        protected double reactorMoneyCost = 62.1;

        protected int numBarracks;
        protected double marineBarracksEnergyCost = 60;
        protected double marineBarracksMoneyCost = 36.3;
        protected double marineBarracksMaxWaterOrVolatilesCost = 24.9;

        protected int numBattlestations;
        protected double battlestationsEnergyCost = 240;
        protected double battlestationsMoneyCost = 30.6;
        protected double battlestationsMaxWaterOrVolatilesCost = 5.2;


        protected int agricultureModulesNeeded;
        protected int reactorsNeeded;
        protected int powerSurplus;
        protected int farmsSurplus;

        public ComputeStation(int numModules, double moduleEnergyCost, double moduleMaxWaterOrVolatilesCost, int numBarracks, int numBattlestations) {
            this.numModules = numModules;
            this.moduleEnergyCost = moduleEnergyCost;
            this.moduleMaxWaterOrVolatilesCost = moduleMaxWaterOrVolatilesCost;
            this.numBarracks = numBarracks;
            this.numBattlestations = numBattlestations;
        }


        public void computeUntilStable() {
            int prevModules = compute();
            int newModules = compute();
            while (prevModules > newModules) {
                prevModules = newModules;
                newModules = compute();
            }
        }


        protected int compute() {

            double waterAndVolatilesNeeded = moduleMaxWaterOrVolatilesCost * numModules + reactorSupportMaxWaterOrVolatiles * reactorsNeeded + numBattlestations * battlestationsMaxWaterOrVolatilesCost + numBarracks * marineBarracksMaxWaterOrVolatilesCost;
            agricultureModulesNeeded = (int) Math.ceil(waterAndVolatilesNeeded / agricultureWaterAndVolatilesCompensation);
            double energyNeeded = moduleEnergyCost * numModules + agricultureModulesNeeded * agricultureSupportEnergy + numBattlestations * battlestationsEnergyCost + numBarracks * marineBarracksEnergyCost;
            reactorsNeeded = (int) Math.ceil(energyNeeded / reactorEnergyCompensation);
            powerSurplus = (int) (reactorsNeeded * reactorEnergyCompensation - energyNeeded);
            farmsSurplus = (int) (agricultureModulesNeeded * agricultureWaterAndVolatilesCompensation - waterAndVolatilesNeeded);

            return totalSlots();
        }

        protected int totalSlots() {
            return numModules + agricultureModulesNeeded + reactorsNeeded + numBattlestations + numBarracks;
        }

        @Override
        public String toString() {
            StringWriter out = new StringWriter();
            PrintWriter sb = new PrintWriter(out);
            sb.println("==================");
            sb.println("Modules: " + numModules);
            sb.println("Farms: " + agricultureModulesNeeded);
            sb.println("Reactors: " + reactorsNeeded);
            sb.println("Marine Barracks: " + numBarracks);
            sb.println("Defense arrays: " + numBattlestations);
            sb.println("Power surplus: " + powerSurplus);
            sb.println("Farm surplus: " + farmsSurplus);
            sb.println("Total: " + totalSlots());
            return out.toString();
        }
    }
}
