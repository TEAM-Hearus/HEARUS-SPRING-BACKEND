package com.hearus.hearusspring.service.impl;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dao.ScheduleDAO;
import com.hearus.hearusspring.data.dao.UserDAO;
import com.hearus.hearusspring.data.dto.schedule.ScheduleDTO;
import com.hearus.hearusspring.data.entitiy.UserEntity;
import com.hearus.hearusspring.data.repository.UserRepository;
import com.hearus.hearusspring.data.repository.schedule.ScheduleRepository;
import com.hearus.hearusspring.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleDAO scheduleDAO;

    @Autowired
    UserRepository userRepository;

    @Override
    public CommonResponse addSchedule(ScheduleDTO scheduleDTO) {
        UserEntity user = userRepository.findFirstById(scheduleDTO.getUserId());

        return scheduleDAO.addSchedule(scheduleDTO.toEntity(user));
    }

    @Override
    public CommonResponse deleteSchedule(ScheduleDTO scheduleDTO) {
        UserEntity user = userRepository.findFirstById(scheduleDTO.getUserId());

        return scheduleDAO.deleteSchedule(scheduleDTO.toEntity(user));
    }
}
