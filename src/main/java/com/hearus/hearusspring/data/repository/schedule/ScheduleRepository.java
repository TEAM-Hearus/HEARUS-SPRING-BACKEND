package com.hearus.hearusspring.data.repository.schedule;

import com.hearus.hearusspring.data.entitiy.schedule.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    Optional<ScheduleEntity> findById(Long id);
    Optional<ScheduleEntity> findByUserIdAndName(String userId, String name);
    boolean existsByUserIdAndName(String userId, String name);
    List<ScheduleEntity> findByUserId(String userId);
}
