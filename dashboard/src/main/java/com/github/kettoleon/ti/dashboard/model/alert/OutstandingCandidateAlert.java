package com.github.kettoleon.ti.dashboard.model.alert;

import com.github.kettoleon.ti.dashboard.model.DashboardModel;
import com.github.kettoleon.ti.saves.Councilor;

import static com.github.kettoleon.ti.dashboard.model.alert.Severity.SUGGESTION;
import static com.github.kettoleon.ti.dashboard.view.components.CouncilorTableCellRenderer.traitListToHtml;
import static com.github.kettoleon.ti.dashboard.view.util.GameIcons.*;
import static org.kordamp.ikonli.fontawesome5.FontAwesomeSolid.GRIN_HEARTS;

public class OutstandingCandidateAlert extends Alert {

    private final Councilor councilor;

    public OutstandingCandidateAlert(Councilor councilor) {
        this.councilor = councilor;
        setSeverity(SUGGESTION);
        setTitle("Young, quick learner candidate: " + councilor.getDisplayName() + " (" + councilor.getType().getFriendlyName() + ")");
        setIcon(GRIN_HEARTS);
        setDetails(buildCouncilorDetails(councilor));
    }

    @Override
    public void onUpdate(DashboardModel model) {
        if (!model.getAvailableCouncilors().getCouncilors().contains(councilor)) {
            model.getAlerts().removeAlert(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof OutstandingCandidateAlert) {
            OutstandingCandidateAlert oth = (OutstandingCandidateAlert) o;
            return councilor.equals(oth.councilor);
        }

        return false;
    }

    public static String buildCouncilorDetails(Councilor c) {
        StringBuffer sb = new StringBuffer();
        sb.append("<html>");
        sb.append("<table>");
        sb.append("<tr>");
        sb.append("<td>Age</td>");
        sb.append("<td>" + RESOURCE_CONTROL_POINT.toInnerHtmlString() + "</td>");
        sb.append("<td>" + ATTRIBUTE_PERSUASION.toInnerHtmlString() + "</td>");
        sb.append("<td>" + ATTRIBUTE_INVESTIGATION.toInnerHtmlString() + "</td>");
        sb.append("<td>" + ATTRIBUTE_ESPIONAGE.toInnerHtmlString() + "</td>");
        sb.append("<td>" + ATTRIBUTE_COMMAND.toInnerHtmlString() + "</td>");
        sb.append("<td>" + ATTRIBUTE_ADMINISTRATION.toInnerHtmlString() + "</td>");
        sb.append("<td>" + ATTRIBUTE_SCIENCE.toInnerHtmlString() + "</td>");
        sb.append("<td>" + ATTRIBUTE_SECURITY.toInnerHtmlString() + "</td>");
        sb.append("<td>" + ATTRIBUTE_LOYALTY.toInnerHtmlString() + "</td>");
        sb.append("<td>Traits</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td>" + c.getAge() + "</td>");
        sb.append("<td>" + c.getControlPoints() + "</td>");
        sb.append("<td>" + c.getPersuasion() + "</td>");
        sb.append("<td>" + c.getInvestigation() + "</td>");
        sb.append("<td>" + c.getEspionage() + "</td>");
        sb.append("<td>" + c.getCommand() + "</td>");
        sb.append("<td>" + c.getAdministration() + "</td>");
        sb.append("<td>" + c.getScience() + "</td>");
        sb.append("<td>" + c.getSecurity() + "</td>");
        sb.append("<td>" + c.getApparentLoyalty() + "</td>");
        sb.append("<td>" + traitListToHtml(c.getNonTechTraits()) + "</td>");
        sb.append("</tr>");
        sb.append("</table>");
        sb.append("</html>");
        return sb.toString();


    }

}
