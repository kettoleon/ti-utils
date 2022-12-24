package com.github.kettoleon.ti.dashboard.view.components;

import com.github.kettoleon.ti.dashboard.model.alert.Alert;
import com.github.kettoleon.ti.dashboard.model.alert.Severity;
import com.github.kettoleon.ti.dashboard.view.util.DarculaColorPalette;
import org.kordamp.ikonli.ionicons4.Ionicons4IOS;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.Map;

import static com.github.kettoleon.ti.dashboard.view.util.DarculaColorPalette.GRAY_PALE;

public class AlertPanelBuilder {
    private JLabel iconLabel;
    private JButton discardButton;
    private JLabel descriptionLabel;
    private JLabel titleLabel;
    private JPanel panel;
    private Alert alert;

    public void setAlert(Alert alert) {
        this.alert = alert;

        iconLabel.setIcon(FontIcon.of(alert.getIcon(), 32, deriveIconColor()));
        iconLabel.setText(null);
        discardButton.setIcon(FontIcon.of(Ionicons4IOS.CLOSE, 12, GRAY_PALE));
        discardButton.setText(null);
        Map fontAttr = titleLabel.getFont().getAttributes();
        fontAttr.put(TextAttribute.SIZE, ((float) fontAttr.get(TextAttribute.SIZE)) * 1.5f);
        titleLabel.setFont(Font.getFont(fontAttr));
        titleLabel.setText(alert.getTitle());
        descriptionLabel.setText(alert.getDetails());

        panel.invalidate();
        panel.repaint();
    }

    private Color deriveIconColor() {
        if (alert.getIconColor() != null) {
            return alert.getIconColor();
        }
        if (alert.getSeverity().equals(Severity.DANGER)) {
            return DarculaColorPalette.RED_NOTIFICATION;
        } else if (alert.getSeverity().equals(Severity.WARNING)) {
            return DarculaColorPalette.ORANGE_NOTIFICATION;
        } else if (alert.getSeverity().equals(Severity.SUGGESTION)) {
            return DarculaColorPalette.BLUE_NOTIFICATION;
        } else if (alert.getSeverity().equals(Severity.SOLVED)) {
            return DarculaColorPalette.GREEN_NOTIFICATION;
        }
        return GRAY_PALE;
    }

    public Component getPanel(Alert alert) {
        setAlert(alert);
        return panel;
    }
}
