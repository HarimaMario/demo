package com.goodyear.gaas.demo.model;


import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class EventRecordPrimaryKey implements Serializable {
    private String orderID;
    private EventName eventName;
}