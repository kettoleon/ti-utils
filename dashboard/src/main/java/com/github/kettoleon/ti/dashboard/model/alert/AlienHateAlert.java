package com.github.kettoleon.ti.dashboard.model.alert;

import com.github.kettoleon.ti.dashboard.model.DashboardModel;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;

import static com.github.kettoleon.ti.dashboard.model.alert.Severity.WARNING;

public class AlienHateAlert extends Alert {

    private double previousHate = 0;

    @Override
    public void onUpdate(DashboardModel model) {
        double newHate = model.getHate();
        if (previousHate != 0) {
            if (previousHate > newHate) {
                //Good news
                this.setSeverity(Severity.SOLVED)
                        .setIcon(BootstrapIcons.ARROW_DOWN_CIRCLE_FILL)
                        .setTitle("Alien hate decreased")
                        .setDetails("Alien hate went down from " + previousHate + " to " + newHate);
                model.getAlerts().showAlert(this);
            } else if (previousHate < newHate) {
                //Bad news
                if (newHate > 80) {
                    this.setSeverity(Severity.DANGER)
                            .setIcon(BootstrapIcons.EXCLAMATION_TRIANGLE_FILL)
                            .setTitle("Alien hate increased!")
                            .setDetails("Alien hate went up from " + previousHate + " to " + newHate);
                } else {
                    this.setSeverity(WARNING)
                            .setIcon(BootstrapIcons.EXCLAMATION_CIRCLE_FILL)
                            .setTitle("Alien hate increased!")
                            .setDetails("Alien hate went up from " + previousHate + " to " + newHate);
                }
                model.getAlerts().showAlert(this);
            } else {
                model.getAlerts().hideAlert(this);
            }
        }
        previousHate = newHate;
    }
}
