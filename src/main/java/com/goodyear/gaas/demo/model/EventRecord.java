package com.goodyear.gaas.demo.model;


import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import org.springframework.messaging.MessageHeaders;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "event_record")
@Data
public class EventRecord {
    @EmbeddedId
    private EventRecordPrimaryKey id;
    private String data;
    private UUID eventID;
    private EventStatus status = EventStatus.INIT;
    private LocalDateTime receivedTS = LocalDateTime.now();
    private int retryCount = 0;
    private long backOffTime = 0L;
    private boolean executionReach;
    @Transient
    private MessageHeaders messageHeaders;
}
