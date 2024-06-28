package com.hearus.hearusspring.data.dao.impl;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dao.ScheduleDAO;
import com.hearus.hearusspring.data.entitiy.UserEntity;
import com.hearus.hearusspring.data.entitiy.schedule.ScheduleElementEntity;
import com.hearus.hearusspring.data.entitiy.schedule.ScheduleEntity;
import com.hearus.hearusspring.data.repository.UserRepository;
import com.hearus.hearusspring.data.repository.schedule.ScheduleElementRepository;
import com.hearus.hearusspring.data.repository.schedule.ScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ScheduleDAOImpl implements ScheduleDAO {
    ScheduleRepository scheduleRepository;
    ScheduleElementRepository scheduleElementRepository;
    UserRepository userRepository;

    private String userId;
    private String scheduleName;

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
        userId = scheduleEntity.getUser().getId();
        scheduleName = scheduleEntity.getName();

        // 동일한 사용자에 대해 같은 이름의 Schedule을 생성하는 것은 불가함
        if(scheduleRepository.existsByUserIdAndName(userId, scheduleName)){
            log.info("[ScheduleDAO]-[addSchedule] ({}) User's ({}) ScheduleName Already Exists", userId, scheduleName);
            return new CommonResponse(false, HttpStatus.CONFLICT,"ScheduleName Already Exists");
        }

        log.info("[ScheduleDAO]-[addSchedule] Create new ScheduleEntity ({})-({})", userId, scheduleName);
        // scheduleRepository에 Schedule 저장
        ScheduleEntity savedSchedule = scheduleRepository.save(scheduleEntity);


        // 해당 User의 schedule 필드에 새로운 스케줄의 ID 추가
        UserEntity user = userRepository.findFirstById(userId);
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
        userId = scheduleEntity.getUser().getId();
        scheduleName = scheduleEntity.getName();
        ScheduleEntity deleteSchedule = scheduleRepository.findByUserIdAndName(userId, scheduleName);

        // ScheduleEntity가 존재하지 않을 경우
        if(!scheduleRepository.existsByUserIdAndName(userId, scheduleName)){
            log.info("[ScheduleDAO]-[addSchedule] ({}) User's ScheduleEntity ({}) doesnt exists", userId, scheduleName);
            return new CommonResponse(false, HttpStatus.INTERNAL_SERVER_ERROR,"ScheduleName Doesn't Exists");
        }

        log.info("[ScheduleDAO]-[deleteSchedule] Delete ScheduleEntity ({})-({})", userId, scheduleName);

        scheduleRepository.deleteByUserIdAndName(userId, scheduleName);

        // 해당 User의 schedule 필드에서 삭제된 스케줄의 ID 제거
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        String schedule = user.getSchedule();
        if (schedule != null) {
            List<String> scheduleIdList = Arrays.asList(schedule.split(","));
            String scheduleIdToRemove = String.valueOf(deleteSchedule.getId());

            // 삭제할 스케줄 ID를 제외한 새로운 리스트 생성
            List<String> newScheduleIdList = scheduleIdList.stream()
                    .filter(id -> !id.equals(scheduleIdToRemove))
                    .collect(Collectors.toList());

            // 새로운 리스트를 문자열로 변환하여 user의 schedule 필드에 저장
            String newSchedule = String.join(",", newScheduleIdList);

            user.setSchedule(newSchedule);
            userRepository.save(user);
        }

        return new CommonResponse(true, HttpStatus.OK,"Schedule Deleted");
    }

    @Override
    @Transactional
    public CommonResponse addElement(ScheduleEntity scheduleEntity, ScheduleElementEntity scheduleElementEntity) {
        userId = scheduleEntity.getUser().getId();
        scheduleName = scheduleEntity.getName();

        // ScheduleEntity가 존재하지 않을 경우
        if(!scheduleRepository.existsByUserIdAndName(userId, scheduleName)){
            log.info("[ScheduleDAO]-[addSchedule] ({}) User's ScheduleEntity ({}) doesnt exists", userId, scheduleName);
            return new CommonResponse(false, HttpStatus.INTERNAL_SERVER_ERROR,"ScheduleName Doesn't Exists");
        }

        log.info("[ScheduleDAO]-[addElement] Save ElementEntity ({})-({})", userId, scheduleElementEntity.getName());

        scheduleEntity = scheduleRepository.findByUserIdAndName(userId, scheduleName);

        scheduleElementEntity.setSchedule(scheduleEntity);
        ScheduleElementEntity savedElement = scheduleElementRepository.save(scheduleElementEntity);

        scheduleEntity.addScheduleElement(savedElement);
        scheduleRepository.save(scheduleEntity);

        System.out.println(scheduleEntity.getScheduleElements().size());

        return new CommonResponse(true, HttpStatus.OK,"ScheduleElement Added", savedElement.toDTO());
    }

    @Override
    @Transactional
    public CommonResponse deleteElement(ScheduleEntity scheduleEntity, ScheduleElementEntity scheduleElementEntity) {
        userId = scheduleEntity.getUser().getId();
        scheduleName = scheduleEntity.getName();

        // ScheduleEntity가 존재하지 않을 경우
        if(!scheduleRepository.existsByUserIdAndName(userId, scheduleName)){
            log.info("[ScheduleDAO]-[addSchedule] ({}) User's ScheduleEntity ({}) doesnt exists", userId, scheduleName);
            return new CommonResponse(false, HttpStatus.INTERNAL_SERVER_ERROR,"ScheduleName Doesn't Exists");
        }

        log.info("[ScheduleDAO]-[deleteElement] Delete ElementEntity ({})-({})", userId, scheduleElementEntity.getName());

        scheduleEntity = scheduleRepository.findByUserIdAndName(userId, scheduleName);

        // Entitiy는 동일 사용자 내 동일 스케줄에서도 이름 등이 중복 가능
        ScheduleElementEntity deleteEntity = scheduleElementRepository.findFirstById(scheduleElementEntity.getId());
        scheduleElementRepository.delete(deleteEntity);

        scheduleEntity.removeScheduleElement(deleteEntity);
        scheduleRepository.save(scheduleEntity);
        return new CommonResponse(true, HttpStatus.OK,"ScheduleElement Deleted");
    }

    @Override
    public ScheduleEntity getSchedule(ScheduleEntity scheduleEntity) {
        userId = scheduleEntity.getUser().getId();
        scheduleName = scheduleEntity.getName();
        return scheduleRepository.findByUserIdAndName(userId, scheduleName);
    }
}