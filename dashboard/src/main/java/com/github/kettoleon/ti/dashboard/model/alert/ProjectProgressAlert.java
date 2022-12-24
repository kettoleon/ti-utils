package com.github.kettoleon.ti.dashboard.model.alert;

import com.github.kettoleon.ti.dashboard.model.DashboardModel;
import com.github.kettoleon.ti.meta.model.Project;
import com.github.kettoleon.ti.saves.Faction;
import com.github.kettoleon.ti.saves.ProjectProgress;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import static com.github.kettoleon.ti.dashboard.model.alert.Severity.*;
import static com.github.kettoleon.ti.dashboard.view.util.ModelHtmlRepresentations.doubleFormat;

public class ProjectProgressAlert extends Alert {

    private int factionId;
    private Project project;

    public ProjectProgressAlert(int factionId, Project project) {
        this.factionId = factionId;
        this.project = project;
        setIcon(FontAwesomeSolid.BOMB);
    }

    public int getFactionId() {
        return factionId;
    }

    public Project getProject() {
        return project;
    }

    @Override
    public void onUpdate(DashboardModel model) {
        Faction faction = model.getFaction(factionId);
        faction.getSabotageableProjectProgress(project).ifPresentOrElse(pp -> {
            setDetails(sabotageDetails(faction, pp, project));

            if (project.isFactionObjective()) {
                setTitle("Consider sabotaging faction objective: " + project.getFriendlyName() + " (" + faction.getDisplayName() + ")");
                if (faction.isEnemy() && pp.getPercentageComplete() >= 70) {
                    setSeverity(pp.getPercentageComplete() >= 85 ? DANGER : WARNING);
                    model.getAlerts().showAlert(this);
                } else if (pp.getPercentageComplete() > 90) {
                    setSeverity(SUGGESTION);
                    model.getAlerts().showAlert(this);
                }else{
                    model.getAlerts().hideAlert(this);
                }
            }else if(faction.isEnemy() && pp.getPercentageComplete() >= 70){
                setTitle("Consider sabotaging faction project: " + project.getFriendlyName() + " (" + faction.getDisplayName() + ")");
                setSeverity(SUGGESTION);
                model.getAlerts().showAlert(this);
            }else{
                model.getAlerts().hideAlert(this);
            }


        }, () -> {
            model.getAlerts().removeAlert(this);
        });
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ProjectProgressAlert) {
            ProjectProgressAlert oth = (ProjectProgressAlert) o;
            return factionId == oth.factionId && project.getDataName().equals(oth.project.getDataName()); //TODO figure out why project.equals() does not work :'(
        }

        return false;
    }

    private static String sabotageDetails(Faction f, ProjectProgress pp, Project prj) {
        return "<html><b>" + f.getDisplayName() + "</b> are about to finish research on <b>" + prj.getFriendlyName() + "</b> <b>" + doubleFormat(Math.ceil(pp.getPercentageComplete())) + "%</b> <i>(" + doubleFormat(Math.ceil(pp.getAccumulatedResearch())) + "/" + prj.getResearchCost() + ")</i>, consider sabotaging them.</html>";
    }


}
