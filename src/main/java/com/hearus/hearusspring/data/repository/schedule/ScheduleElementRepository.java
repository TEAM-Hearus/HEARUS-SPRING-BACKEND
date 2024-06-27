package com.hearus.hearusspring.data.repository.schedule;

import com.hearus.hearusspring.data.entitiy.schedule.ScheduleElementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleElementRepository extends JpaRepository<ScheduleElementEntity, Long> {
    List<ScheduleElementEntity> findByScheduleId(Long scheduleId);
}