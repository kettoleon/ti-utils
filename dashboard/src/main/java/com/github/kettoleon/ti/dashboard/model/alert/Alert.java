package com.github.kettoleon.ti.dashboard.model.alert;

import lombok.Data;
import lombok.experimental.Accessors;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;

import java.awt.*;
import java.util.Objects;

@Data
@Accessors(chain = true)
public abstract class Alert implements AlertHandler {

    private Severity severity = Severity.NEUTRAL;
    private Ikon icon = BootstrapIcons.QUESTION_CIRCLE_FILL;
    private Color iconColor;
    private String title;
    private String details;

    private boolean removeOnDispose;

    @Override
    public String toString() {
        return "Alert{" +
                "category=" + severity +
                ", title='" + title + '\'' +
                ", details='" + details + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alert alert = (Alert) o;
        return severity == alert.severity && Objects.equals(title, alert.title) && Objects.equals(details, alert.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(severity, title, details);
    }
}
