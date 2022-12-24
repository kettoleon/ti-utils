package com.github.kettoleon.ti.dashboard.model.tables;

import com.github.kettoleon.ti.dashboard.ProfileUtils;
import com.github.kettoleon.ti.dashboard.model.DashboardModel;
import com.github.kettoleon.ti.dashboard.model.alert.Alert;
import com.github.kettoleon.ti.dashboard.model.alert.AlienHateAlert;
import com.github.kettoleon.ti.dashboard.model.alert.MakeNewSaveAlert;
import com.github.kettoleon.ti.dashboard.model.alert.ProjectProgressAlert;

import javax.swing.table.AbstractTableModel;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class AlertTableModel extends AbstractTableModel {

    private final List<Alert> shownAlerts = new CopyOnWriteArrayList<>();
    private final List<Alert> hiddenAlerts = new CopyOnWriteArrayList<>();

    @Override
    public String getColumnName(int column) {
        return "Alert";
    }

    @Override
    public int getRowCount() {
        return shownAlerts.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return shownAlerts.get(rowIndex);
    }

    public AlertTableModel() {
        hiddenAlerts.add(new MakeNewSaveAlert());
        hiddenAlerts.add(new AlienHateAlert());
    }

    /**
     * Alerts are being added hidden. Call {@link #updateAlerts(DashboardModel)} to make them appear, hide, or removed.
     *
     * @param alert
     */
    public void addAlert(Alert alert) {
        synchronized (this) {
            if (shownAlerts.contains(alert) || hiddenAlerts.contains(alert)) {
                return;
            }
            if (alert instanceof ProjectProgressAlert) {
                ProjectProgressAlert projectProgressAlert = (ProjectProgressAlert) alert;
                System.out.println("Adding project progress alert for: " + projectProgressAlert.getFactionId() + ":" + projectProgressAlert.getProject().getDataName());
            }
            hiddenAlerts.add(alert);
        }
    }

    public void addAndShowAlert(Alert alert) {
        synchronized (this) {
            if (shownAlerts.contains(alert) || hiddenAlerts.contains(alert)) {
                return;
            }

            hiddenAlerts.add(alert);
        }
        showAlert(alert);
    }

    public void hideAlert(Alert alert) {
        int idx = getIdx(alert);
        synchronized (this) {
            if (!hiddenAlerts.contains(alert)) {
                shownAlerts.remove(alert);
                hiddenAlerts.add(alert);
            }
        }
        fireTableDataChanged();
//        fireTableRowsDeleted(idx, idx);
    }

    public void showAlert(Alert alert) {
        synchronized (this) {
            if (!shownAlerts.contains(alert)) {
                shownAlerts.add(alert);

                List<Alert> sorted = shownAlerts.stream().sorted(Comparator.comparing(Alert::getSeverity)).collect(Collectors.toList());
                shownAlerts.clear();
                shownAlerts.addAll(sorted);
                hiddenAlerts.remove(alert);
            }
        }
        fireTableDataChanged();
    }

    private int getIdx(Alert alert) {
        int idx = 0;
        for (int i = 0; i < shownAlerts.size(); i++) {
            if (shownAlerts.get(i).equals(alert)) {
                idx = i;
                break;
            }
        }
        return idx;
    }

    public void removeAlert(Alert remove) {
//        if (remove instanceof ProjectProgressAlert) {
//            ProjectProgressAlert projectProgressAlert = (ProjectProgressAlert) remove;
//            System.out.println("Removing project progress alert for: " + projectProgressAlert.getFactionId() + ":" + projectProgressAlert.getProject().getDataName());
//        }
        hiddenAlerts.remove(remove);
        if (shownAlerts.contains(remove)) {
            int idx = getIdx(remove);
            shownAlerts.remove(remove);
            fireTableRowsDeleted(idx, idx);
        }
    }

    public void updateAlerts(DashboardModel model) {
        ProfileUtils.measure("Updating " + shownAlerts.size() + " shown alerts", () -> {
            for (Alert alert : shownAlerts) {
                ProfileUtils.measure("Updating " + alert.getClass().getSimpleName(), () -> {
                    alert.onUpdate(model);
                });
            }
        });

        ProfileUtils.measure("Updating " + hiddenAlerts.size() + " hidden alerts", () -> {
            for (Alert alert : hiddenAlerts) {
                ProfileUtils.measure("Updating " + alert.getClass().getSimpleName(), () -> {
                    alert.onUpdate(model);
                });
            }
        });

        Map<String, Integer> hiddenAlertCount = new HashMap<>();

        for (Alert alert : hiddenAlerts) {
            String cn = alert.getClass().getSimpleName();
            int c = hiddenAlertCount.computeIfAbsent(cn, s -> 0);
            hiddenAlertCount.put(cn, c + 1);
        }
        hiddenAlertCount.forEach((k, v) -> System.out.println(k + ": " + v));

    }


}
