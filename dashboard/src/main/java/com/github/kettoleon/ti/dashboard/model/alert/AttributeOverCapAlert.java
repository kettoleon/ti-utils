package com.github.kettoleon.ti.dashboard.model.alert;

import com.github.kettoleon.ti.dashboard.model.DashboardModel;
import com.github.kettoleon.ti.saves.Councilor;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.kettoleon.ti.dashboard.model.alert.Severity.SUGGESTION;

public class AttributeOverCapAlert extends Alert {
    private final int councilorId;

    public AttributeOverCapAlert(int councilorId) {
        this.councilorId = councilorId;
        setSeverity(SUGGESTION);
        setIcon(FontAwesomeBrands.BUROMOBELEXPERTE);
    }

    @Override
    public void onUpdate(DashboardModel model) {

        model.getHiredCouncilors().getCouncilor(councilorId).ifPresentOrElse(councilor -> {
            Map<String, Integer> attrs = councilor.getAttributesTotal();
            List<Map.Entry<String, Integer>> attrsOverCap = attrs.entrySet().stream().filter(e -> e.getValue() > 25).collect(Collectors.toList());
            if (!attrsOverCap.isEmpty()) {
                setTitle("Councilor attribute over the cap: " + councilor.getDisplayName());
                setDetails(councilor.getDisplayName() + " has the following attributes over the cap: " + attrsOverCap.stream().map(e -> e.getValue() + " " + e.getKey()).collect(Collectors.joining(", ")) + ". Consider reorganising organisations!");
                model.getAlerts().showAlert(this);
            }
        }, () -> {
            model.getAlerts().removeAlert(this);
        });
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof AttributeOverCapAlert) {
            AttributeOverCapAlert oth = (AttributeOverCapAlert) o;
            return councilorId == oth.councilorId;
        }

        return false;
    }

}
