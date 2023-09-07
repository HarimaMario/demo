package com.goodyear.gaas.demo.util.impl;

import com.goodyear.gaas.demo.constant.Constant;
import com.goodyear.gaas.demo.model.EventName;
import com.goodyear.gaas.demo.model.EventRecord;
import com.goodyear.gaas.demo.model.EventRecordPrimaryKey;
import com.goodyear.gaas.demo.util.EventRecordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventRecordUtilImpl implements EventRecordUtil {
    @Override
    public EventRecord createEventRecordFromMessage(Message<String> message){
        String notificationMessage = message.getPayload();
        com.goodyear.gaas.demo.model.EventRecord newEventRecord = new com.goodyear.gaas.demo.model.EventRecord();
        newEventRecord.setId(createEventRecordPrimaryKey(message));
        newEventRecord.setEventID(message.getHeaders().getId());
        newEventRecord.setData(notificationMessage);
        newEventRecord.setMessageHeaders(message.getHeaders());
        return newEventRecord;
    }

    @Override
    public void copyEventRecord(EventRecord copyTo, EventRecord copyFrom) {
        copyTo.setMessageHeaders(copyFrom.getMessageHeaders());
        copyTo.setData(copyFrom.getData());
        copyTo.setEventID(copyFrom.getEventID());
    }

    public EventRecordPrimaryKey createEventRecordPrimaryKey(Message<String> message){
        // Get data from headers
        String orderID = message.getHeaders().get(Constant.ORDER_ID_HEADER_KEY).toString();
        String eventName = message.getHeaders().get(Constant.EVENT_NAME_HEADER_KEY).toString();
        log.info("Message header orderId: {}", orderID);
        log.info("Message header eventName: {}", eventName);
        // created EventRecordPrimaryKey
        EventRecordPrimaryKey newEventRecordPrimaryKey = new EventRecordPrimaryKey();
        newEventRecordPrimaryKey.setOrderID(orderID);
        newEventRecordPrimaryKey.setEventName(EventName.valueOf(eventName));
        return newEventRecordPrimaryKey;
    }
}
