package com.github.kettoleon.ti.dashboard.view.components;

import com.github.kettoleon.ti.dashboard.model.tables.OrganisationTableColumn;
import com.github.kettoleon.ti.dashboard.view.util.GameIcon;
import com.github.kettoleon.ti.dashboard.view.util.GameIcons;
import com.github.kettoleon.ti.dashboard.view.util.ModelHtmlRepresentations;
import com.github.kettoleon.ti.meta.model.Mission;
import com.github.kettoleon.ti.meta.model.trait.Trait;
import com.github.kettoleon.ti.saves.Councilor;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.kettoleon.ti.dashboard.model.tables.OrganisationTableColumn.*;
import static com.github.kettoleon.ti.dashboard.view.util.ModelHtmlRepresentations.*;
import static java.util.stream.Collectors.joining;

public class OrganisationTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component rendered = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        Map attributes = rendered.getFont().getAttributes();
        int iconSize = ((Float) attributes.get(TextAttribute.SIZE)).intValue();

        if (rendered instanceof JLabel) {
            JLabel jLabel = (JLabel) rendered;
            jLabel.setIcon(null);
            if (value instanceof Integer) {
                jLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            } else if (value instanceof Double) {
                Double dv = (Double) value;
                jLabel.setText(doubleFormat(dv));
            }
            if (isColumn(column, TIER)) {
                jLabel.setText(wrap(starsHtml((int) value)));
            } else if (isColumn(column, ASSIGNED)) {
                if (value instanceof Optional) {
                    Optional<Councilor> councilorOptional = (Optional<Councilor>) value;
                    if (councilorOptional.isEmpty()) {
                        jLabel.setText("");
                    } else {
                        Councilor c = councilorOptional.get();
                        jLabel.setText(wrap(toHtml(c)));
                    }
                }
            } else if (isColumn(column, REQUIRED_TRAITS) || isColumn(column, PROHIBITED_TRAITS)) {
                List<Trait> traits = (List<Trait>) value;
                jLabel.setText(wrap(traits.stream().map(ModelHtmlRepresentations::toHtml).collect(joining(", "))));
            } else if (isColumn(column, GRANTED)) {
                List<Mission> missions = (List<Mission>) value;
                jLabel.setText(wrap(missions.stream().map(m -> toHtml(m, true)).collect(joining(", "))));
            }
            jLabel.setFont(new Font(attributes));
        }

        return rendered;
    }

    private static boolean isColumn(int column, OrganisationTableColumn tier) {
        return OrganisationTableColumn.values()[column] == tier;
    }

    private String starsHtml(int value) {
        String stars = "";
        for (int i = 0; i < value; i++) {
            stars += GameIcons.MISC_STAR.toInnerHtmlString();
        }
        return stars;
    }
}
