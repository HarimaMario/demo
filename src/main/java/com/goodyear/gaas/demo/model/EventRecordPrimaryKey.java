package com.goodyear.gaas.demo.model;


import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class EventRecordPrimaryKey implements Serializable {
    private String orderID;
    @Enumerated(EnumType.STRING)
    private EventName eventName;
}