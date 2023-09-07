package com.goodyear.gaas.demo.config;

import com.goodyear.gaas.demo.service.MessageEventService;
import com.goodyear.gaas.demo.util.EventRecordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Configuration
public class MessageEventConsumerConfig {

    private EventRecordUtil eventRecordUtil;

    private MessageEventService messageEventService;

    @Bean
    public Consumer<Message<String>> processMessageEvent() {
        return message -> messageEventService.processEventRecord(eventRecordUtil.createEventRecordFromMessage(message));
    }
}