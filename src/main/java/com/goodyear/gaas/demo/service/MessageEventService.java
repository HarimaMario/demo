package com.goodyear.gaas.demo.service;

import com.goodyear.gaas.demo.model.EventRecord;

public interface MessageEventService {
    void processEventRecord(EventRecord eventRecord);
}
