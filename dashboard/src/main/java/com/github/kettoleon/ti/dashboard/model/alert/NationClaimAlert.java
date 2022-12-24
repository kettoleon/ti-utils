package com.github.kettoleon.ti.dashboard.model.alert;

import com.github.kettoleon.ti.dashboard.model.DashboardModel;
import com.github.kettoleon.ti.saves.Councilor;
import com.github.kettoleon.ti.saves.Nation;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.github.kettoleon.ti.dashboard.model.alert.Severity.SUGGESTION;
import static com.github.kettoleon.ti.dashboard.model.alert.Severity.WARNING;

public class NationClaimAlert extends Alert {


    private final int sourceNationId;
    private final int targetNationId;
    private String lastStatus = "none";

    public NationClaimAlert(int sourceNationId, int targetNationId) {
        this.sourceNationId = sourceNationId;
        this.targetNationId = targetNationId;
    }

    @Override
    public void onUpdate(DashboardModel model) {
        Nation sourceNation = model.getNation(sourceNationId);
        Nation targetNation = model.getNation(targetNationId);
        //TODO nations can only be unified if one has claims on the target's capital region!
        if (sourceNation.getMissingClaims().stream().flatMap(region -> region.getNation().stream()).noneMatch(Predicate.isEqual(targetNation))) {
            model.getAlerts().removeAlert(this);
        } else {
            if (!sourceNation.isAlliedWith(targetNation)) {
                setSeverity(SUGGESTION);
                setTitle("New claim: " + targetNation.getDisplayName());
                setIcon(BootstrapIcons.LIGHTBULB_FILL);
                setDetails(
                        sourceNation.getDisplayName() + " has claims on " + targetNation.getDisplayName() + ". If you ally them, you might be able to federate and unify them later."
                );
                if (!lastStatus.equals("couldAlly")) {
//                    model.getAlerts().showAlert(this); //TODO disabled for now, I don't see much value to it
                }
                lastStatus = "couldAlly";
            } else {
                sourceNation.getFederation().ifPresentOrElse(fed -> {
                    if (targetNation.isRivalOrAtWarWithAny(fed.getMembers())) {
                        setSeverity(WARNING);
                        setTitle("Allied claim with incompatible diplomacy for federation: " + targetNation.getDisplayName());
                        setIcon(FontAwesomeSolid.HANDSHAKE_SLASH);
                        setDetails(
                                targetNation.getDisplayName() + " could be joining " + fed.getDisplayName() + ", however is still rival or at war with " + sourceNation.getRivalOrAtWarWith(fed.getMembers())
                        );
                        if (!lastStatus.equals("hasFederationRivals")) {
                            model.getAlerts().showAlert(this);
                        }
                        lastStatus = "hasFederationRivals";
                    } else if (targetNation.getFederation().isEmpty() && sourceNation.isExecutiveControlConsolidated() && targetNation.isExecutiveControlConsolidated()) {
                        setSeverity(SUGGESTION);
                        setTitle("Allied can join federation: " + targetNation.getDisplayName());
                        setIcon(FontAwesomeSolid.HANDSHAKE);
                        setDetails(
                                targetNation.getDisplayName() + " can finally join " + fed.getDisplayName() + "."
                        );
                        if (!lastStatus.equals("canJoinFederation")) {
                            model.getAlerts().showAlert(this);
                        }
                        lastStatus = "canJoinFederation";
                    } else if (fed.getMembers().contains(targetNation)) {
                        if (sourceNation.isExecutiveControlConsolidated() && targetNation.isExecutiveControlConsolidated() && sourceNation.getCooldownWith(targetNation).isEmpty()) {
                            //TODO assuming: claims to target nation's capital region, controll of all CPs in target nation
                            setSeverity(SUGGESTION);
                            setTitle("Federated member can be integrated: " + targetNation.getDisplayName());
                            setIcon(FontAwesomeSolid.HANDSHAKE);
                            setDetails(
                                    targetNation.getDisplayName() + " can finally be integrated into " + fed.getDisplayName() + "."
                            );
                            if (!lastStatus.equals("canBeIntegrated")) {
                                model.getAlerts().showAlert(this);
                            }
                            lastStatus = "canBeIntegrated";
                        } else {
                            model.getAlerts().hideAlert(this);
                        }
                    }
                }, () -> {
                    if (targetNation.getFederation().isEmpty() && sourceNation.isExecutiveControlConsolidated() && targetNation.isExecutiveControlConsolidated()) {
                        setSeverity(SUGGESTION);
                        setTitle("Allied can join federation: " + targetNation.getDisplayName());
                        setIcon(FontAwesomeSolid.HANDSHAKE);
                        setDetails(
                                targetNation.getDisplayName() + " can finally join " + sourceNation.getDisplayName() + "."
                        );
                        if (!lastStatus.equals("canJoinFederation")) {
                            model.getAlerts().showAlert(this);
                        }
                        lastStatus = "canJoinFederation";
                    }
                });
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof NationClaimAlert) {
            NationClaimAlert oth = (NationClaimAlert) o;
            return sourceNationId == oth.sourceNationId && targetNationId == oth.targetNationId;
        }

        return false;
    }


}
