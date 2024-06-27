package com.hearus.hearusspring.data.dao.impl;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dao.ScheduleDAO;
import com.hearus.hearusspring.data.entitiy.UserEntity;
import com.hearus.hearusspring.data.entitiy.schedule.ScheduleEntity;
import com.hearus.hearusspring.data.repository.UserRepository;
import com.hearus.hearusspring.data.repository.schedule.ScheduleElementRepository;
import com.hearus.hearusspring.data.repository.schedule.ScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ScheduleDAOImpl implements ScheduleDAO {
    ScheduleRepository scheduleRepository;
    ScheduleElementRepository scheduleElementRepository;
    UserRepository userRepository;

    @Autowired
    public ScheduleDAOImpl(ScheduleRepository scheduleRepository, ScheduleElementRepository scheduleElementRepository, UserRepository userRepository) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleElementRepository = scheduleElementRepository;
        this.userRepository = userRepository;
    }
    @Override
    // 메서드 내부의 데이터베이스 작업이 하나의 트랜잭션으로 처리될 수 있도록 한다
    // Spring Boot를 사용하는 경우, 별도의 설정 없이 @EnableTransactionManagement가 자동으로 활성화
    @Transactional
    public CommonResponse addSchedule(ScheduleEntity scheduleEntity) {
        // 동일한 사용자에 대해 같은 이름의 Schedule을 생성하는 것은 불가함
        if(scheduleRepository.existsByUserIdAndName(scheduleEntity.getUser().getId(), scheduleEntity.getName())){
            log.info("[ScheduleDAO]-[addSchedule] {} 회원 ScheduleName 중복 {}", scheduleEntity.getUser().getId(), scheduleEntity.getName());
            return new CommonResponse(false, HttpStatus.CONFLICT,"ScheduleName Already Exists");
        }

        log.info("[ScheduleDAO]-[addSchedule] 새로운 ScheduleEntity 저장 {}-{}", scheduleEntity.getUser().getId(), scheduleEntity.getName());
        // scheduleRepository에 Schedule 저장
        ScheduleEntity savedSchedule = scheduleRepository.save(scheduleEntity);


        // 해당 User의 schedule 필드에 새로운 스케줄의 ID 추가
        UserEntity user = userRepository.findFirstById(scheduleEntity.getUser().getId());
        if (user != null) {
            String schedule = user.getSchedule();
            if (schedule == null) {
                schedule = String.valueOf(savedSchedule.getId());
            } else {
                schedule += "," + savedSchedule.getId();
            }
            user.setSchedule(schedule);
            userRepository.save(user);
        }

        return new CommonResponse(true, HttpStatus.CREATED,"Schedule Created");
    }

    @Override
    @Transactional
    public CommonResponse deleteSchedule(ScheduleEntity scheduleEntity) {
        // ScheduleEntity가 존재하지 않을 경우
        if(!scheduleRepository.existsByUserIdAndName(scheduleEntity.getUser().getId(), scheduleEntity.getName())){
            log.info("[ScheduleDAO]-[addSchedule] {} 회원 ScheduleEntity가 존재하지 않음 {}", scheduleEntity.getUser().getId(), scheduleEntity.getName());
            return new CommonResponse(false, HttpStatus.INTERNAL_SERVER_ERROR,"ScheduleName Doesn't Exists");
        }

        log.info("[ScheduleDAO]-[deleteSchedule] ScheduleEntity 삭제 {}-{}", scheduleEntity.getUser().getId(), scheduleEntity.getName());

        scheduleRepository.delete(scheduleEntity);

        // 해당 User의 schedule 필드에서 삭제된 스케줄의 ID 제거
        UserEntity user = userRepository.findFirstById(scheduleEntity.getUser().getId());
        if (user != null) {
            String schedule = user.getSchedule();
            if (schedule != null) {
                String scheduleIdToRemove = String.valueOf(scheduleEntity.getId());
                String[] scheduleIds = schedule.split(",");
                StringBuilder newSchedule = new StringBuilder();

                for (String id : scheduleIds) {
                    if (!id.equals(scheduleIdToRemove)) {
                        if (!newSchedule.isEmpty()) {
                            newSchedule.append(",");
                        }
                        newSchedule.append(id);
                    }
                }

                user.setSchedule(newSchedule.toString());
                userRepository.save(user);
            }
        }

        return new CommonResponse(true, HttpStatus.OK,"Schedule Deleted");
    }
}