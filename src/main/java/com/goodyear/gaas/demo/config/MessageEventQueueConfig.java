package com.goodyear.gaas.demo.config;

import com.goodyear.gaas.demo.model.EventName;
import com.goodyear.gaas.demo.model.EventRecordPrimaryKey;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "message-event")
public class MessageEventQueueConfig {
    private Map<EventName, String> eventNameQueueMap;
    private int backOffTime;
    private int maxRetry;
    private String deadLetterQueue;
    public String getQueueByEventName(EventRecordPrimaryKey eventRecordPrimaryKey) {
        return eventNameQueueMap.get(eventRecordPrimaryKey.getEventName());
    }
}
