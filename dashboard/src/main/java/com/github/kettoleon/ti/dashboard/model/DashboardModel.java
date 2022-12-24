package com.github.kettoleon.ti.dashboard.model;

import com.github.kettoleon.ti.dashboard.model.tables.AlertTableModel;
import com.github.kettoleon.ti.dashboard.model.tables.CouncilorTableModel;
import com.github.kettoleon.ti.dashboard.model.tables.OrganisationTableModel;
import com.github.kettoleon.ti.meta.model.Tech;
import com.github.kettoleon.ti.saves.Councilor;
import com.github.kettoleon.ti.saves.Faction;
import com.github.kettoleon.ti.saves.Nation;
import com.github.kettoleon.ti.saves.TechProgress;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.swing.event.SwingPropertyChangeSupport;
import java.beans.*;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;

@Accessors(fluent = true)
@Setter(AccessLevel.PRIVATE)
public class DashboardModel {

    private final SwingPropertyChangeSupport pcs = new SwingPropertyChangeSupport(this);
    private LocalDate date = LocalDate.EPOCH;

    private double hate = 0.0;

    private CouncilorTableModel availableCouncilors = new CouncilorTableModel();
    private CouncilorTableModel hiredCouncilors = new CouncilorTableModel();
    private OrganisationTableModel obtainableOrganisations = new OrganisationTableModel();

    private AlertTableModel alerts = new AlertTableModel();

    private Map<Integer, Faction> factions = new HashMap<>();
    private Map<Integer, Nation> nations = new HashMap<>();
    private Map<Integer, Councilor> councilors = new HashMap<>();
    private List<TechProgress> techProgress;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        update("date", this.date, date, this::date);
    }

    public double getHate() {
        return hate;
    }

    public void setHate(double hate) {
        update("hate", this.hate, hate, this::hate);
    }

    protected <T> void update(String fieldName, T oldValue, T newValue, Consumer<T> setter) {
        setter.accept(newValue);
        pcs.firePropertyChange(fieldName, oldValue, newValue);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public CouncilorTableModel getAvailableCouncilors() {
        return availableCouncilors;
    }

    public CouncilorTableModel getHiredCouncilors() {
        return hiredCouncilors;
    }

    public AlertTableModel getAlerts() {
        return alerts;
    }

    public OrganisationTableModel getObtainableOrganisations() {
        return obtainableOrganisations;
    }

    public void setFactions(Map<Integer, Faction> factions) {
        this.factions = factions;
    }

    public void setCouncilors(Map<Integer, Councilor> councilors) {
        this.councilors = councilors;
    }

    public Faction getFaction(int factionId) {
        return factions.get(factionId);
    }

    public Collection<Faction> getFactions() {
        return factions.values();
    }

    public Nation getNation(int nationId) {
        return nations.get(nationId);
    }

    public void setNations(List<Nation> nationsThatCurrentlyExist) {
        HashMap<Integer, Nation> newNations = new HashMap<>();
        nationsThatCurrentlyExist.forEach(n -> newNations.put(n.getId(), n));
        nations = newNations;
    }

    public Councilor getCouncilor(int councilorId) {
        return councilors.get(councilorId);
    }

    public void setTechProgress(List<TechProgress> techProgress) {

        this.techProgress = techProgress;
    }

    public List<TechProgress> getTechProgress() {
        return techProgress;
    }

    public Optional<TechProgress> getTechProgress(Tech tech) {
        return techProgress.stream().filter(tp -> tp.getTech().equals(tech)).findFirst();
    }
}
