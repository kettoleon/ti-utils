package com.github.kettoleon.ti.meta.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.kettoleon.ti.meta.model.trait.Trait;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Org extends MetaObject {

    private boolean requiresNationality;
    private String requiredNationName;
    private List<String> requiredOwnerTraits;
    private List<String> prohibitedOwnerTraits;
    private String requiredTechName;
    private List<String> missionsGrantedNames;
    private List<String> restricted;

    public Optional<Nation> getRequiredNation() {
        return getReference(requiredNationName, Nation.class);
    }

    public List<Trait> getRequiredTraits() {
        return getReferenceArray(requiredOwnerTraits, Trait.class);
    }

    public List<Trait> getProhibitedTraits() {
        return getReferenceArray(prohibitedOwnerTraits, Trait.class);
    }

    public List<String> getRestrictedFactionTemplateNames() {
        return restricted.stream().filter(StringUtils::isNotEmpty).map(r -> r + "Council").collect(Collectors.toList());
    }

    public List<Mission> getGrantedMissions() {
        return getReferenceArray(missionsGrantedNames, Mission.class);
    }

    public boolean hasGrantedMission(String mission) {
        return getMissionsGrantedNames().contains(mission);
    }
}
