package com.github.kettoleon.ti.dashboard.model.tables;

import com.github.kettoleon.ti.dashboard.view.util.GameIcon;
import com.github.kettoleon.ti.meta.model.CouncilorType;
import com.github.kettoleon.ti.meta.model.Nation;
import com.github.kettoleon.ti.meta.model.Org;
import com.github.kettoleon.ti.saves.Organisation;

import javax.swing.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.github.kettoleon.ti.dashboard.view.util.GameIcons.*;

public enum OrganisationTableColumn implements Column<Organisation, Object> {
    TIER("Tier", Integer.class, Organisation::getTier),
    NAME("Name", String.class, Organisation::getDisplayName),
    ASSIGNED("Councilor", CouncilorType.class, Organisation::getAssignedCouncilor),
    NATION("Country", String.class, o -> Optional.ofNullable(o.getMetadata()).flatMap(Org::getRequiredNation).map(Nation::getFriendlyName).orElse("")),
    REQUIRED_TRAITS("Required Traits", List.class, o -> o.getMetadata().getRequiredTraits()),
    PROHIBITED_TRAITS("Prohibited Traits", List.class, o -> o.getMetadata().getProhibitedTraits()),
    MON(RESOURCE_CURRENCY, Integer.class, Organisation::getIncomeMoneyMonth),
    INF(RESOURCE_INFLUENCE, Integer.class, Organisation::getIncomeInfluenceMonth),
    OPS(RESOURCE_OPS, Integer.class, Organisation::getIncomeOpsMonth),
    BST(RESOURCE_BOOST, Integer.class, Organisation::getIncomeBoostMonth),
    MC(RESOURCE_MISSION_CONTROL, Integer.class, Organisation::getIncomeMissionControl),
    RSC(RESOURCE_RESEARCH, Integer.class, Organisation::getIncomeResearchMonth),
    PC(RESOURCE_PROJECTS, Integer.class, Organisation::getProjectCapacityGranted),
    PER(ATTRIBUTE_PERSUASION, Integer.class, Organisation::getPersuasion),
    INV(ATTRIBUTE_INVESTIGATION, Integer.class, Organisation::getInvestigation),
    ESP(ATTRIBUTE_ESPIONAGE, Integer.class, Organisation::getEspionage),
    CMD(ATTRIBUTE_COMMAND, Integer.class, Organisation::getCommand),
    ADM(ATTRIBUTE_ADMINISTRATION, Integer.class, Organisation::getAdministration),
    SCI(ATTRIBUTE_SCIENCE, Integer.class, Organisation::getScience),
    SEC(ATTRIBUTE_SECURITY, Integer.class, Organisation::getSecurity),
    GRANTED("Granted Missions", List.class, o -> o.getMetadata().getGrantedMissions()),
    ;

    private ImageIcon icon;
    private String header;
    private Class type;
    private Function<Organisation, Object> getter;

    OrganisationTableColumn(String header, Class type, Function<Organisation, Object> getter) {
        this.header = header;
        this.type = type;
        this.getter = getter;
    }

    OrganisationTableColumn(GameIcon header, Class type, Function<Organisation, Object> getter) {
        this.header = name();
        this.icon = header.getIcon();
        this.type = type;
        this.getter = getter;
    }


    @Override
    public String getHeader() {
        return header;
    }

    @Override
    public Class getType() {
        return type;
    }

    @Override
    public Function<Organisation, Object> getGetter() {
        return getter;
    }

    @Override
    public ImageIcon getIcon() {
        return icon;
    }
}
