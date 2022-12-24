package com.github.kettoleon.ti.saves;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.kettoleon.ti.meta.model.Tech;

import java.util.List;

public class GlobalResearch extends StateObject {

    public GlobalResearch(SaveFile saveFile, JsonNode node) {
        super(saveFile, node);
    }

    public List<Tech> getFinishedTechs() {
        return getMetaReferenceArray("finishedTechsNames", Tech.class);
    }

    public List<TechProgress> getTechProgress(){
        return getArray(TechProgress.class, "techProgress");
    }




}
