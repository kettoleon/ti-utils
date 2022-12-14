package com.github.kettoleon.ti.meta.model.trait;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Condition {

    @JsonProperty("$type")
    private String type;

    private String strIdx;

    private String sign;

    private String strValue;
}
