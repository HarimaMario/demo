package com.goodyear.gaas.demo.model;

public enum EventName {
    FULLFILMENT("fulfillment"),
    BRINGG("bringg"),
    VOL("vol"),
    EGAIN("egain"),
    MAILGUN("mailgun"),
    EDW_DEA("edw-dea"),
    PI_MIDDLEWARE("pi-middleware"),
    WD_INTEGRATION("wd-integration"),
    GAAS_DB("gaas-db");

    private String eventName;

    EventName(String eventName) {
        this.eventName = eventName;
    }
}
