package com.github.kettoleon.ti.dashboard.view.util;

import com.github.kettoleon.ti.meta.model.Mission;
import com.github.kettoleon.ti.meta.model.trait.Trait;
import com.github.kettoleon.ti.saves.Councilor;
import com.github.kettoleon.ti.saves.Nation;
import com.github.kettoleon.ti.saves.Organisation;
import com.github.kettoleon.ti.saves.Region;

import static com.github.kettoleon.ti.dashboard.view.util.DarculaColorPalette.*;
import static com.github.kettoleon.ti.dashboard.view.util.GameIcons.*;
import static java.util.stream.Collectors.joining;

public class ModelHtmlRepresentations {

    public static String toHtml(Trait t) {

        String htmlTrait = t.getFriendlyName();

        if (t.isCrippling()) {
            htmlTrait = "<s>" + htmlTrait + "</s>";
        } else if (t.isFavorite()) {
            htmlTrait = "<b><u>" + htmlTrait + "</u></b>";
        }

        if (t.isNegative()) {
            htmlTrait = "<font color=\"" + toHtmlColor(RED_FONT) + "\">" + htmlTrait + "</font>";
        } else if (t.isMixed()) {
            htmlTrait = "<font color=\"" + toHtmlColor(PURPLE_FONT) + "\"><i>" + htmlTrait + "</i></font>";
        } else if (t.isPositive()) {
            htmlTrait = "<font color=\"" + toHtmlColor(GREEN_FONT) + "\">" + htmlTrait + "</font>";
        }
        return htmlTrait;

    }

    public static String toHtml(Mission m, boolean showName) {
        return GameIcon.getMissionIcon(m.getDataName()).toInnerHtmlString() + (showName ? " " + m.getFriendlyName() : "");
    }

    public static String toHtml(Councilor c) {
        return GameIcon.getFactionIcon(c.getFaction().get().getTemplateName()).toInnerHtmlString() + " "
                + c.getDisplayName()
                + c.getLocation().flatMap(Region::getNation).map(Nation::getDisplayName).map(n -> " <i>(" + n + ")</i>").orElse("");
    }


    public static String toHtml(Organisation o) {
        StringBuilder html = new StringBuilder();

        for (int i = 0; i < o.getTier(); i++) {
            html.append(MISC_STAR.toInnerHtmlString());
        }
        html.append("  " + o.getDisplayName());
        showIfNeeded(html, RESOURCE_CURRENCY, o.getIncomeMoneyMonth());
        showIfNeeded(html, RESOURCE_INFLUENCE, o.getIncomeInfluenceMonth());
        showIfNeeded(html, RESOURCE_OPS, o.getIncomeOpsMonth());
        showIfNeeded(html, RESOURCE_BOOST, o.getIncomeBoostMonth());
        showIfNeeded(html, RESOURCE_MISSION_CONTROL, o.getIncomeMissionControl());
        showIfNeeded(html, RESOURCE_RESEARCH, o.getIncomeResearchMonth());
        showIfNeeded(html, RESOURCE_PROJECTS, o.getProjectCapacityGranted());
        showIfNeeded(html, ATTRIBUTE_PERSUASION, o.getPersuasion());
        showIfNeeded(html, ATTRIBUTE_INVESTIGATION, o.getInvestigation());
        showIfNeeded(html, ATTRIBUTE_ESPIONAGE, o.getEspionage());
        showIfNeeded(html, ATTRIBUTE_COMMAND, o.getCommand());
        showIfNeeded(html, ATTRIBUTE_ADMINISTRATION, o.getAdministration());
        showIfNeeded(html, ATTRIBUTE_SCIENCE, o.getScience());
        showIfNeeded(html, ATTRIBUTE_SECURITY, o.getSecurity());

        if (!o.getMetadata().getMissionsGrantedNames().isEmpty()) {
            html.append("<br/>Missions provided: ");
            html.append(o.getMetadata().getGrantedMissions().stream().map(m -> toHtml(m, true)).collect(joining(", ")));
        }

        if (!o.getMetadata().getRequiredTraits().isEmpty()) {
            html.append("<br/>Councilor must have: ");
            html.append(o.getMetadata().getRequiredTraits().stream().map(ModelHtmlRepresentations::toHtml).collect(joining(", ")));
        }
        if (!o.getMetadata().getProhibitedTraits().isEmpty()) {
            html.append("<br/>Councilor must NOT have: ");
            html.append(o.getMetadata().getProhibitedTraits().stream().map(ModelHtmlRepresentations::toHtml).collect(joining(", ")));
        }

        o.getAssignedCouncilor().ifPresent(c -> {
            html.append("<br/>We can take it from: " + toHtml(c));
        });
        return html.toString();
    }

    private static void showIfNeeded(StringBuilder html, GameIcon icon, double value) {
        if (value != 0) {
            html.append(" ");
            html.append(value < 0 ? "-" : "+");
            html.append(icon.toInnerHtmlString());
            html.append(doubleFormat(Math.abs(value)));
        }
    }

    public static String doubleFormat(double d) {
        if (d == (long) d)
            return String.format("%d", (long) d);
        else
            return String.format("%s", d);
    }


    public static String wrap(String innerHtml) {
        return "<html>" + innerHtml + "</html>";
    }
}
