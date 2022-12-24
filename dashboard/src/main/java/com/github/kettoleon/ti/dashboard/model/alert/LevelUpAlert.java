package com.github.kettoleon.ti.dashboard.model.alert;

import com.github.kettoleon.ti.dashboard.model.DashboardModel;
import com.github.kettoleon.ti.saves.Councilor;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;

import static com.github.kettoleon.ti.dashboard.model.alert.Severity.SUGGESTION;

public class LevelUpAlert extends Alert {

    private final int councilorId;

    public LevelUpAlert(int councilorId) {
        this.councilorId = councilorId;
        setSeverity(SUGGESTION);
        setIcon(FontAwesomeBrands.SUPERPOWERS);
    }

    private String buildDetails(int requiredExp, Councilor councilor) {
        return "Has enough experience (" + councilor.getExperience() + "/" + requiredExp + ") to level up." + (councilor.hasRemovableNegativeTrait() ? " Consider removing his negative traits first though." : "");
    }

    @Override
    public void onUpdate(DashboardModel model) {
        model.getHiredCouncilors().getCouncilor(councilorId).ifPresentOrElse(councilor -> {
            int requiredExp = (int) Math.ceil(20 * councilor.getExperienceMultiplier());
            if (councilor.getExperience() >= requiredExp) {
                setTitle("Level Up: " + councilor.getDisplayName());
                setDetails(buildDetails(requiredExp, councilor));
                model.getAlerts().showAlert(this);
            } else {
                model.getAlerts().hideAlert(this);
            }
        }, () -> {
            model.getAlerts().removeAlert(this);
        });

    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LevelUpAlert) {
            LevelUpAlert oth = (LevelUpAlert) o;
            return councilorId == oth.councilorId;
        }

        return false;
    }

}
