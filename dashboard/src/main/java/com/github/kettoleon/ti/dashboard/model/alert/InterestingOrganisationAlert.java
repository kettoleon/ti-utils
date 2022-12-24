package com.github.kettoleon.ti.dashboard.model.alert;

import com.github.kettoleon.ti.dashboard.model.DashboardModel;
import com.github.kettoleon.ti.dashboard.view.util.ModelHtmlRepresentations;
import com.github.kettoleon.ti.saves.Organisation;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import static com.github.kettoleon.ti.dashboard.model.alert.Severity.SUGGESTION;
import static com.github.kettoleon.ti.dashboard.view.util.ModelHtmlRepresentations.toHtml;
import static com.github.kettoleon.ti.dashboard.view.util.ModelHtmlRepresentations.wrap;

public class InterestingOrganisationAlert extends Alert {

    private final Organisation organisation;

    public InterestingOrganisationAlert(Organisation organisation) {
        this.organisation = organisation;
        setSeverity(SUGGESTION);
        setTitle("Interesting organisation: " + organisation.getDisplayName());
        setIcon(FontAwesomeSolid.GRIN_HEARTS);
        setDetails(wrap(toHtml(organisation)));
    }

    @Override
    public void onUpdate(DashboardModel model) {
        if (!model.getObtainableOrganisations().getOrganisations().contains(organisation)) {
            model.getAlerts().removeAlert(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof InterestingOrganisationAlert) {
            InterestingOrganisationAlert oth = (InterestingOrganisationAlert) o;
            return organisation.equals(oth.organisation);
        }

        return false;
    }

}
