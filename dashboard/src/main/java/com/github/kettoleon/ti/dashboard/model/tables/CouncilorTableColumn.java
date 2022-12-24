package com.github.kettoleon.ti.dashboard.model.tables;

import com.github.kettoleon.ti.dashboard.view.util.GameIcon;
import com.github.kettoleon.ti.dashboard.view.util.GameIcons;
import com.github.kettoleon.ti.meta.model.CouncilorType;
import com.github.kettoleon.ti.saves.Councilor;

import javax.swing.*;
import java.util.List;
import java.util.function.Function;

import static com.github.kettoleon.ti.dashboard.view.util.GameIcons.*;

public enum CouncilorTableColumn implements Column<Councilor, Object> {
    NAME("Name", String.class, Councilor::getDisplayName),
    TYPE("Type", CouncilorType.class, Councilor::getType), //TODO link to template and get name from there
    COUNTRY("Country", String.class, Councilor::getCountry),
    AGE("Age", Integer.class, Councilor::getAge),
    CP(RESOURCE_CONTROL_POINT, Integer.class, Councilor::getControlPoints),
    PER(ATTRIBUTE_PERSUASION, Integer.class, Councilor::getPersuasion),
    INV(ATTRIBUTE_INVESTIGATION, Integer.class, Councilor::getInvestigation),
    ESP(ATTRIBUTE_ESPIONAGE, Integer.class, Councilor::getEspionage),
    CMD(ATTRIBUTE_COMMAND, Integer.class, Councilor::getCommand),
    ADM(ATTRIBUTE_ADMINISTRATION, Integer.class, Councilor::getAdministration),
    SCI(ATTRIBUTE_SCIENCE, Integer.class, Councilor::getScience),
    SEC(ATTRIBUTE_SECURITY, Integer.class, Councilor::getSecurity),
    LOY(ATTRIBUTE_LOYALTY, Integer.class, Councilor::getApparentLoyalty),
    //        MON("Money", Integer.class, (c) -> ""), //TODO these get calculated from traits and attributes, https://hoodedhorse.com/wiki/Terra_Invicta/Councilors
//        INF("Influence", Integer.class, (c) -> ""),
//        OPS("Ops", Integer.class, (c) -> ""),
//        RES("Research", Integer.class, (c) -> ""),
//        BST("Boost", Integer.class, (c) -> ""),
//        MC("Mc", Integer.class, (c) -> ""),
//        EP("Ep", Integer.class, (c) -> ""),
    TRAITS("Traits", List.class, Councilor::getNonTechTraits), //We want to compare new guys against old guys as if the old guys were in a blank state
//        MISSIONS("Missions", String.class, (c) -> ""), //TODO these get calculated from Traits
    ;

    private ImageIcon icon;
    private String header;
    private Class type;
    private Function<Councilor, Object> getter;

    CouncilorTableColumn(String header, Class type, Function<Councilor, Object> getter) {
        this.header = header;
        this.type = type;
        this.getter = getter;
    }

    CouncilorTableColumn(GameIcon header, Class type, Function<Councilor, Object> getter) {
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
    public Function<Councilor, Object> getGetter() {
        return getter;
    }

    @Override
    public ImageIcon getIcon() {
        return icon;
    }
}
