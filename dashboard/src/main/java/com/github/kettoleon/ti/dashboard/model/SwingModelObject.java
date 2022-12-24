package com.github.kettoleon.ti.dashboard.model;

import javax.swing.event.SwingPropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;

public class SwingModelObject {

    protected final SwingPropertyChangeSupport pcs;

    public SwingModelObject(SwingPropertyChangeSupport pcs) {
        this.pcs = pcs;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

}
