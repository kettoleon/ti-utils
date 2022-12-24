package com.github.kettoleon.ti.meta.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class ResearchSubject extends MetaObject {

    private String techCategory;
    private int researchCost;
    private List<String> prereqs;

    public List<ResearchSubject> getPrerequisites() {
        List<ResearchSubject> prerequisites = new ArrayList<>();
        for (String rs : prereqs) {
            if (StringUtils.isNotBlank(rs)) {
                Optional<Project> prj = getReference(rs, Project.class);
                if (prj.isPresent()) {
                    prerequisites.add(prj.get());
                } else {
                    prerequisites.add(getReference(rs, Tech.class).orElseThrow(() -> new RuntimeException("Could not find prerequisite " + rs + " for " + getDataName())));
                }
            }
        }

        return prerequisites;
    }

    public long getTotalResearchNeeded() {
        return getResearchCost() + getPrerequisites().stream().mapToLong(ResearchSubject::getTotalResearchNeeded).sum();
    }


    public boolean isAlienTech() {
        return getDataName().equals("Project_AlienMasterProject");
    }
}
