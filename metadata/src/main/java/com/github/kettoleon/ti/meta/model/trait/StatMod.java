package com.github.kettoleon.ti.meta.model.trait;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatMod {

    private String stat;
    private String operation;
    private String strValue;

    private Condition condition;

    public int getStrValueAsInt() {
        if (StringUtils.isNumeric(strValue.replace("-", ""))) {
            return Integer.parseInt(strValue);
        }
        return 0;
    }

    public boolean isPositive() {
        if (StringUtils.isNumeric(strValue)) {
            return Integer.parseInt(strValue) > 0;
        } else if (strValue.equals("Loyalty")) {
            return true;
        }
        return false;
    }

    public boolean isNegative() {
        if (StringUtils.isNumeric(strValue.replace("-", ""))) {
            return Integer.parseInt(strValue) < 0;
        }
        return false;
    }

    public boolean hasCondition() {
        if (condition != null) {
            return StringUtils.isNotBlank(condition.getType());
        }
        return false;
    }
}
