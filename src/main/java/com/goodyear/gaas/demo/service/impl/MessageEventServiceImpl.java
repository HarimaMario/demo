package com.goodyear.gaas.demo.service.impl;

import com.goodyear.gaas.demo.config.MessageEventQueueConfig;
import com.goodyear.gaas.demo.model.EventRecord;
import com.goodyear.gaas.demo.model.EventStatus;
import com.goodyear.gaas.demo.repository.EventRecordRepository;
import com.goodyear.gaas.demo.service.MessageEventService;
import com.goodyear.gaas.demo.util.EventRecordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@EnableConfigurationProperties(MessageEventQueueConfig.class)
@RequiredArgsConstructor
@Service
@Slf4j
public class MessageEventServiceImpl implements MessageEventService {

    private final EventRecordRepository eventRecordRepository;

    private final MessageEventQueueConfig messageEventQueueConfig;

    private final StreamBridge streamBridge;

    private final EventRecordUtil eventRecordUtil;

    @Override
    public void processEventRecord(EventRecord eventRecord) {
        log.info("Message receive: {}",eventRecord);
        Optional<EventRecord> eventRecordOptional = eventRecordRepository.findById(eventRecord.getId());
        if(eventRecordOptional.isPresent() && eventRecordOptional.get().getStatus().equals(EventStatus.FAILED)){
            EventRecord eventRecordDB = eventRecordOptional.get();
            eventRecordUtil.copyEventRecord(eventRecordDB,eventRecord);
            processFailMessage(eventRecordDB);
        } else {
            processNewMessage(eventRecord);
        }
    }

    private void processFailMessage(EventRecord eventRecord) {
        try {
            eventRecord.setRetryCount(eventRecord.getRetryCount()+1);
            eventRecord.setBackOffTime(eventRecord.getBackOffTime()+messageEventQueueConfig.getBackOffTime());
            TimeUnit.MILLISECONDS.sleep(eventRecord.getBackOffTime());
            String destinationQueue;
            if(eventRecord.getRetryCount()<=messageEventQueueConfig.getMaxRetry()){
                destinationQueue = messageEventQueueConfig.getQueueByEventName(eventRecord.getId());
            } else {
                eventRecord.setExecutionReach(Boolean.TRUE);
                destinationQueue = messageEventQueueConfig.getDeadLetterQueue();
            }
            eventRecordRepository.save(eventRecord);
            sendMessage(eventRecord,destinationQueue);
        } catch (InterruptedException e) {
            sendMessage(eventRecord,messageEventQueueConfig.getDeadLetterQueue());
        }
    }

    private void processNewMessage(EventRecord eventRecord){
        eventRecordRepository.save(eventRecord);
        sendMessage(eventRecord,messageEventQueueConfig.getQueueByEventName(eventRecord.getId()));
    }

    private void sendMessage(EventRecord eventRecord, String queue){
        streamBridge.send(queue,buildMessage(eventRecord));
    }

    private Message<String> buildMessage(EventRecord eventRecord){
        return MessageBuilder
                .withPayload(eventRecord.getData())
                .copyHeaders(eventRecord.getMessageHeaders())
                .build();
    }

}
