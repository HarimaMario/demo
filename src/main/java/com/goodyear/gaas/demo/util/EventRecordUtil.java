package com.goodyear.gaas.demo.util;

import com.goodyear.gaas.demo.model.EventRecord;
import org.springframework.messaging.Message;

public interface EventRecordUtil {
    EventRecord createEventRecordFromMessage(Message<String> message);

    void copyEventRecord(EventRecord copyTo, EventRecord copyFrom);
}
