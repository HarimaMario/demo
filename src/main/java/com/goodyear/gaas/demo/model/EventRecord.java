package com.goodyear.gaas.demo.model;


import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.messaging.MessageHeaders;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_record")
@EqualsAndHashCode
@ToString(exclude = "data")
@Data
public class EventRecord {
    @EmbeddedId
    private EventRecordPrimaryKey id;
    @Column(length = 2000)
    private String data;
    @Column(length = 1000)
    private String eventID;
    @Enumerated(EnumType.STRING)
    private EventStatus status = EventStatus.INIT;
    private LocalDateTime receivedTS = LocalDateTime.now();
    private int retryCount = 0;
    private long backOffTime = 0;
    private boolean executionReach = Boolean.FALSE  ;
    @Transient
    private MessageHeaders messageHeaders;
}
