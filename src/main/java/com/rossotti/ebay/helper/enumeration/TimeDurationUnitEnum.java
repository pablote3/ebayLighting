package com.rossotti.ebay.helper.enumeration;

public enum TimeDurationUnitEnum {
    YEAR ("Year"),
    MONTH ("Month"),
    DAY ("Day"),
    HOUR ("Hour"),
    CALENDAR_DAY ("Calendar Day"),
    BUSINESS_DAY ("Business Day"),
    MINUTE ("Minute"),
    SECOND ("Second"),
    MILLISECOND ("Millisecond");
    private String code;
    TimeDurationUnitEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
