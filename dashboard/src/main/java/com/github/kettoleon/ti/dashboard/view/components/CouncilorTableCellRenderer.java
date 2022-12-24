package com.github.kettoleon.ti.dashboard.view.components;

import com.github.kettoleon.ti.dashboard.view.util.ModelHtmlRepresentations;
import com.github.kettoleon.ti.meta.MetaRepository;
import com.github.kettoleon.ti.meta.model.CouncilorType;
import com.github.kettoleon.ti.meta.model.trait.Trait;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CouncilorTableCellRenderer extends DefaultTableCellRenderer {

    public CouncilorTableCellRenderer(MetaRepository meta) {

    }

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
            }
            if (column == 1) {
                if (value instanceof CouncilorType) {
                    CouncilorType type = (CouncilorType) value;
                    jLabel.setText(type.getFriendlyName());
                    if(type.isFavorite()){
                        jLabel.setIcon(FontIcon.of(BootstrapIcons.HEART_FILL, iconSize, new Color(172, 107, 121)));
                        attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
                    }else if(type.isHated()){
                        jLabel.setIcon(FontIcon.of(BootstrapIcons.HAND_THUMBS_DOWN_FILL, iconSize, new Color(188, 144, 24)));
                        attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_EXTRA_LIGHT);
                        attributes.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
                    }else{
                        jLabel.setIcon(FontIcon.of(FontAwesomeSolid.MEH_BLANK, iconSize, new Color(82, 133, 66)));
                        attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
                    }


                }
            } else if (column == 5) {
                addStarIfOver((int) value, attributes, iconSize, jLabel, 8);
            } else if (column == 6) {
                addStarIfOver((int) value, attributes, iconSize, jLabel, 7);
            } else if (column == 7) {
                addStarIfOver((int) value, attributes, iconSize, jLabel, 6);
            } else if (column == 8) {
                addStarIfOver((int) value, attributes, iconSize, jLabel, 8);
            } else if (column == 9) {
                addStarIfOver((int) value, attributes, iconSize, jLabel, 7);
            } else if (column == 10) {
                addStarIfOver((int) value, attributes, iconSize, jLabel, 7);
            } else if (column == 11) {
                addStarIfOver((int) value, attributes, iconSize, jLabel, 6);
            } else if (column == 13) {
                if (value instanceof java.util.List) {
                    java.util.List<Trait> traits = (java.util.List<Trait>) value;
                    jLabel.setText(traitListToHtml(traits));
                }
            }
            jLabel.setFont(new Font(attributes));
        }

        return rendered;
    }

    public static String traitListToHtml(List<Trait> traits) {
        List<String> htmlTraits = new ArrayList<>();
        for (Trait t : traits) {


            htmlTraits.add(ModelHtmlRepresentations.toHtml(t));

        }
        StringBuffer sb = new StringBuffer();
        sb.append("<html>");
        sb.append(String.join(", ", htmlTraits));
        sb.append("</html>");
        return sb.toString();
    }

    private static void addStarIfOver(int value, Map attributes, int iconSize, JLabel jLabel, int i) {
        if (value >= i) {
            jLabel.setIcon(FontIcon.of(BootstrapIcons.STAR_FILL, iconSize, new Color(238, 188, 51)));
            attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        }
    }
}
