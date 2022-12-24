package com.github.kettoleon.ti.dashboard.view.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FlashingComponent {
    public static void makeComponentFlash(final JComponent component) {
        final int interval = 400; // interval in milliseconds
        final int numFlashes = 3; // number of times to flash the component
        final Timer timer = new Timer(interval, null);

        timer.addActionListener(new ActionListener() {
            int count = 0;
            Color background = component.getBackground();
            boolean opacity = component.isOpaque();

            public void actionPerformed(ActionEvent e) {
                if (count % 2 == 0) {
                    component.setBackground(new Color(197, 83, 80));
                    component.setOpaque(true);
                } else {
                    component.setBackground(background);
                    component.setOpaque(opacity);
                }
                component.repaint();
                count++;
                if (count == numFlashes * 2) {
                    component.setBackground(background);
                    component.setOpaque(opacity);
                    timer.stop();
                }
            }
        });

        timer.start();
    }
}
