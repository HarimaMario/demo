package com.goodyear.gaas.demo.repository;

import com.goodyear.gaas.demo.model.EventRecord;
import com.goodyear.gaas.demo.model.EventRecordPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRecordRepository extends JpaRepository<EventRecord, EventRecordPrimaryKey>{
}
