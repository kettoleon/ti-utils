package com.github.kettoleon.ti.dashboard.model.alert;

import com.github.kettoleon.ti.dashboard.model.DashboardModel;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;

import java.time.LocalDate;
import java.time.Month;

import static com.github.kettoleon.ti.dashboard.model.alert.Severity.SUGGESTION;

public class MakeNewSaveAlert extends Alert {

    public MakeNewSaveAlert() {
        setSeverity(SUGGESTION);
        setIcon(BootstrapIcons.SAVE_FILL);
        setTitle("Consider making a save");
    }

    @Override
    public void onUpdate(DashboardModel model) {
        if (isFirstDayOfYear(model.getDate())) {
            setDetails("It is the start of the year " + model.getDate().getYear() + ", consider making an annual save.");
            model.getAlerts().showAlert(this);
        }
    }

    private static boolean isFirstDayOfYear(LocalDate date) {
        return date.getMonth().equals(Month.JANUARY) && date.getDayOfMonth() == 1;
    }
}
