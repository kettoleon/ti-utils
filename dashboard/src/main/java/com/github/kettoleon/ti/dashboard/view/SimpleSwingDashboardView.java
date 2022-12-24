package com.github.kettoleon.ti.dashboard.view;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.github.kettoleon.ti.dashboard.model.DashboardModel;
import com.github.kettoleon.ti.dashboard.view.components.MainDashboard;
import com.github.kettoleon.ti.meta.MetaRepository;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.Map;

public class SimpleSwingDashboardView implements DashboardView {
    private DashboardModel model;
    private MetaRepository meta;

    public SimpleSwingDashboardView(DashboardModel model, MetaRepository meta) {

        this.model = model;
        this.meta = meta;
    }

    @Override
    public void start() {

        SwingUtilities.invokeLater(() -> {
            FlatDarculaLaf.setup();
            Map attributes = new JLabel().getFont().getAttributes();
            attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
            attributes.put(TextAttribute.SIZE, 16);
            setUIFont(new FontUIResource(new Font(attributes)));

            JFrame dashboard = new JFrame("Dashboard - Terra Invicta");
            dashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            dashboard.setMinimumSize(new Dimension(800, 600));
            dashboard.getContentPane().setLayout(new BorderLayout());
//            dashboard.getMenuBar().add(new Menu("Hello Menu"));
            MainDashboard mainDashboard = new MainDashboard(model, meta);
            dashboard.setContentPane(mainDashboard.getPanel());
            dashboard.setExtendedState(dashboard.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            dashboard.setVisible(true);

        });

    }

    public static void setUIFont (javax.swing.plaf.FontUIResource f){
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get (key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put (key, f);
        }
    }
}
