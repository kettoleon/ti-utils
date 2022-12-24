package com.github.kettoleon.ti.dashboard.model.alert;

import com.github.kettoleon.ti.dashboard.model.DashboardModel;
import com.github.kettoleon.ti.saves.Councilor;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import static com.github.kettoleon.ti.dashboard.model.alert.Severity.WARNING;

public class AssassinAlert extends Alert {

    private final int councilorId;

    public AssassinAlert(int councilorId) {
        this.councilorId = councilorId;
        setSeverity(WARNING);
        setIcon(FontAwesomeSolid.SKULL_CROSSBONES);

    }

    @Override
    public void onUpdate(DashboardModel model) {
        Councilor councilor = model.getCouncilor(councilorId);
        if (councilor == null || !councilor.canAssassinate() || councilor.getFaction().isEmpty() || !councilor.getFaction().get().isEnemy()) { //TODO if we assassin it instead of making it resign, what happens to the saveModel? is the councilor removed totally?
            model.getAlerts().removeAlert(this);
        } else {
            setTitle("Enemy hired assassin: " + councilor.getDisplayName() + " (" + councilor.getFaction().orElseThrow().getDisplayName() + ")");
            setDetails(assassinDetails(councilor));
            model.getAlerts().showAlert(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof AssassinAlert) {
            AssassinAlert oth = (AssassinAlert) o;
            return councilorId == oth.councilorId;
        }

        return false;
    }

    private static String assassinDetails(Councilor ec) {
        return ec.getDisplayName() + " can assassinate, be careful, or neutralize " + ec.getHimOrHer() + "." + ((!ec.isAssassinType() && ec.hasAssassinOrg()) ? " Actually, we can just do a hostile takeover mission against " + ec.getHimOrHer() + "." : "");
    }

}
