package com.github.kettoleon.ti.dashboard.view;

import com.github.kettoleon.ti.dashboard.model.DashboardModel;
import com.github.kettoleon.ti.dashboard.view.util.FlashingComponent;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;

public class DateTest extends JLabel implements PropertyChangeListener {

    public DateTest(DashboardModel model) {
        model.addPropertyChangeListener(this);
        updateDate(model.getDate());
    }

    private void updateDate(LocalDate date) {
        setText(date.toString());
        FlashingComponent.makeComponentFlash(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("date")) {
            updateDate((LocalDate) evt.getNewValue());
        }
    }
}
