package com.goodyear.gaas.demo.service.impl;

import com.goodyear.gaas.demo.config.MessageEventQueueConfig;
import com.goodyear.gaas.demo.model.EventRecord;
import com.goodyear.gaas.demo.repository.EventRecordRepository;
import com.goodyear.gaas.demo.service.MessageEventService;
import com.goodyear.gaas.demo.util.EventRecordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@EnableConfigurationProperties(MessageEventQueueConfig.class)
@RequiredArgsConstructor
@Service
public class MessageEventServiceImpl implements MessageEventService {

    private EventRecordRepository eventRecordRepository;

    private final MessageEventQueueConfig messageEventQueueConfig;

    private final StreamBridge streamBridge;

    private EventRecordUtil eventRecordUtil;

    @Override
    public void processEventRecord(EventRecord eventRecord) {
        Optional<EventRecord> eventRecordOptional = eventRecordRepository.findById(eventRecord.getId());
        if(eventRecordOptional.isPresent()){
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
            if(eventRecord.getRetryCount()<messageEventQueueConfig.getMaxRetry()){
                sendMessage(eventRecord,messageEventQueueConfig.getQueueByEventName(eventRecord.getId()));
            } else {
                eventRecord.setExecutionReach(Boolean.TRUE);
                sendMessage(eventRecord,messageEventQueueConfig.getDeadLetterQueue());
            }
            eventRecordRepository.save(eventRecord);
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
