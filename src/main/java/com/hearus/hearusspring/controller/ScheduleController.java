package com.hearus.hearusspring.controller;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dto.UserDTO;
import com.hearus.hearusspring.data.dto.schedule.ScheduleDTO;
import com.hearus.hearusspring.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    private CommonResponse response;

    @PostMapping(value="/addSchedule")
    public ResponseEntity<CommonResponse> addSchedule(@Valid @RequestBody ScheduleDTO scheduleDTO){
        log.info("[ScheduleController]-[addSchedule] API Call");

        if(scheduleDTO.getName().isEmpty() ){
            log.info("[ScheduleController]-[addSchedule] Failed : Empty Name");
            response = new CommonResponse(false, HttpStatus.BAD_REQUEST,"Empty Name");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        // SecurityContext에서 Authentication으로 UserID를 받아온다
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String) authentication.getPrincipal();

        // 이후 scheduleDTO의 UserID를 설정한다
        scheduleDTO.setUserId(userId);

        response = scheduleService.addSchedule(scheduleDTO);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping(value="/deleteSchedule")
    public ResponseEntity<CommonResponse> deleteSchedule(@Valid @RequestBody ScheduleDTO scheduleDTO){
        log.info("[ScheduleController]-[deleteSchedule] API Call");

        if(scheduleDTO.getName().isEmpty()){
            log.info("[ScheduleController]-[deleteSchedule] Failed : Empty Name");
            response = new CommonResponse(false, HttpStatus.BAD_REQUEST,"Empty Name");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String) authentication.getPrincipal();

        scheduleDTO.setUserId(userId);

        response = scheduleService.deleteSchedule(scheduleDTO);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
