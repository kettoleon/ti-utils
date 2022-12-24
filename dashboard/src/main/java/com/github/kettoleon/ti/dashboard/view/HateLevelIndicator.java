package com.github.kettoleon.ti.dashboard.view;

import com.github.kettoleon.ti.dashboard.model.DashboardModel;
import com.github.kettoleon.ti.dashboard.view.util.FlashingComponent;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class HateLevelIndicator extends JLabel implements PropertyChangeListener {

    public HateLevelIndicator(DashboardModel model) {
        model.addPropertyChangeListener(this);
        updateLevel(model.getHate());
    }

    private void updateLevel(double hate) {
        setText("Alien hate level: " + hate);
        if (hate < 20) {
            setForeground(new Color(101, 129, 86));
        } else if (hate < 40) {
            setForeground(new Color(162, 158, 99));
        } else if (hate < 60) {
            setForeground(new Color(253, 150, 66));
        } else if (hate < 80) {
            setForeground(new Color(194, 90, 46));
        } else {
            setForeground(new Color(197, 83, 80));
        }
        FlashingComponent.makeComponentFlash(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("hate")) {
            updateLevel((Double) evt.getNewValue());
        }
    }
}
