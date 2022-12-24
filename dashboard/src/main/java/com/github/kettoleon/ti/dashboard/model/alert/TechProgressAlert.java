package com.github.kettoleon.ti.dashboard.model.alert;

import com.github.kettoleon.ti.dashboard.model.DashboardModel;
import com.github.kettoleon.ti.meta.model.Project;
import com.github.kettoleon.ti.meta.model.Tech;
import com.github.kettoleon.ti.saves.Faction;
import com.github.kettoleon.ti.saves.ProjectProgress;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import static com.github.kettoleon.ti.dashboard.model.alert.Severity.*;
import static com.github.kettoleon.ti.dashboard.view.util.ModelHtmlRepresentations.doubleFormat;

public class TechProgressAlert extends Alert {

    private Tech tech;

    public TechProgressAlert(Tech tech) {
        this.tech = tech;
        setIcon(FontAwesomeSolid.FLAG_CHECKERED);
    }

    @Override
    public void onUpdate(DashboardModel model) {
        model.getTechProgress(tech).ifPresentOrElse(tp -> {
            if (!tp.getWinningFaction().isPlayer()) {
                setSeverity(tp.getWinningFaction().isEnemy() ? DANGER : WARNING);
                setTitle("Global tech slot in danger");
                setDetails(tp.getWinningFaction().getDisplayName() + " are winning the global tech research slot for " + tp.getTech().getFriendlyName() + "!");
                model.getAlerts().showAlert(this);
            } else if (tp.winningFactionCantLose()) {
                setSeverity(SOLVED);
                setTitle("Global tech slot secured");
                setDetails("You are already winning the tech slot for " + tp.getTech().getFriendlyName() + ", you can focus your research elsewhere if you want.");
                model.getAlerts().showAlert(this);
            }
        }, () -> {
            model.getAlerts().removeAlert(this);
        });

    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TechProgressAlert) {
            TechProgressAlert oth = (TechProgressAlert) o;
            return tech.equals(oth.tech);
        }

        return false;
    }

}
