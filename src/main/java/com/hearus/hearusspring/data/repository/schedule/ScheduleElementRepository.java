package com.hearus.hearusspring.data.repository.schedule;

import com.hearus.hearusspring.data.entitiy.schedule.ScheduleElementEntity;
import com.hearus.hearusspring.data.entitiy.schedule.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleElementRepository extends JpaRepository<ScheduleElementEntity, Long> {
    ScheduleElementEntity findByScheduleId(Long scheduleId);
    ScheduleElementEntity findFirstById(Long id);

    List<ScheduleElementEntity> findByScheduleAndDayOfWeek(ScheduleEntity schedule, String dayOfWeek);

    Optional<ScheduleElementEntity> findByScheduleAndName(ScheduleEntity schedule, String name);
}