package com.github.kettoleon.ti.saves;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;

public class TimeState extends StateObject {

    public TimeState(SaveFile saveFile, JsonNode node) {
        super(saveFile, node);
    }

    public static String saveFileClassName() {
        return "PavonisInteractive.TerraInvicta.TITimeState";
    }

    public int getDaysInCampaign(){
        return getInt("daysInCampaign");
    }

    public LocalDateTime getCurrentDateTime(){
        return getLocalDateTime("currentDateTime");
    }


}
