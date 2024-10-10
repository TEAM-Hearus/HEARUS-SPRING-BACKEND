package com.hearus.hearusspring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dto.schedule.ScheduleDTO;
import com.hearus.hearusspring.data.dto.schedule.ScheduleElementDTO;
import com.hearus.hearusspring.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

        if(scheduleDTO.getName() == null || scheduleDTO.getName().isEmpty()){
            log.warn("[ScheduleController]-[addSchedule] Failed : Empty Name");
            response = new CommonResponse(false, HttpStatus.BAD_REQUEST,"Empty Name");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        String userId = getUserIdFromContext();

        // 이후 scheduleDTO의 UserID를 설정한다
        scheduleDTO.setUserId(userId);

        response = scheduleService.addSchedule(scheduleDTO);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping(value="/deleteSchedule")
    public ResponseEntity<CommonResponse> deleteSchedule(@Valid @RequestBody ScheduleDTO scheduleDTO){
        log.info("[ScheduleController]-[deleteSchedule] API Call");

        if(scheduleDTO.getName() == null || scheduleDTO.getName().isEmpty()){
            log.warn("[ScheduleController]-[deleteSchedule] Failed : Empty Name");
            response = new CommonResponse(false, HttpStatus.BAD_REQUEST,"Empty Name");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        String userId = getUserIdFromContext();
        scheduleDTO.setUserId(userId);

        response = scheduleService.deleteSchedule(scheduleDTO);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping(value="/addElement")
    public ResponseEntity<CommonResponse> addElement(@Valid @RequestBody Map<String, Object> requestBody){
        // ObjectMapper를 사용해 Map에서 키에 대한 값을 각각의 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();

        ScheduleDTO scheduleDTO = objectMapper.convertValue(requestBody.get("scheduleDTO"), ScheduleDTO.class);
        ScheduleElementDTO scheduleElementDTO = objectMapper.convertValue(requestBody.get("scheduleElementDTO"), ScheduleElementDTO.class);

        log.info("[ScheduleController]-[addElement] API Call");

        if(scheduleDTO.getName() == null || scheduleDTO.getName().isEmpty()){
            log.warn("[ScheduleController]-[addElement] Failed : Empty Schedule Name");
            response = new CommonResponse(false, HttpStatus.BAD_REQUEST,"Empty Schedule Name");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        String userId = getUserIdFromContext();
        scheduleDTO.setUserId(userId);

        response = scheduleService.addElement(scheduleDTO, scheduleElementDTO);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping(value="/deleteElement")
    public ResponseEntity<CommonResponse> deleteElement(@Valid @RequestBody Map<String, Object> requestBody){
        ObjectMapper objectMapper = new ObjectMapper();

        ScheduleDTO scheduleDTO = objectMapper.convertValue(requestBody.get("scheduleDTO"), ScheduleDTO.class);
        ScheduleElementDTO scheduleElementDTO = objectMapper.convertValue(requestBody.get("scheduleElementDTO"), ScheduleElementDTO.class);

        log.info("[ScheduleController]-[deleteElement] API Call");

        if(scheduleDTO.getName() == null || scheduleDTO.getName().isEmpty()){
            log.warn("[ScheduleController]-[deleteElement] Failed : Empty Schedule Name");
            response = new CommonResponse(false, HttpStatus.BAD_REQUEST,"Empty Schedule Name");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        String userId = getUserIdFromContext();
        scheduleDTO.setUserId(userId);

        response = scheduleService.deleteElement(scheduleDTO, scheduleElementDTO);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/getSchedule")
    public ResponseEntity<CommonResponse> getSchedule(@RequestParam("name") String name) {
        log.info("[ScheduleController]-[getSchedule] API Call");

        if (name == null || name.trim().isEmpty()) {
            log.warn("[ScheduleController]-[getSchedule] Schedule name is null or empty");
            return ResponseEntity.badRequest().body(new CommonResponse(false, HttpStatus.BAD_REQUEST, "Schedule name is required"));
        }

        try {
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            String userId = getUserIdFromContext();
            scheduleDTO.setName(name);
            scheduleDTO.setUserId(userId);

            ScheduleDTO resultScheduleDTO = scheduleService.getSchedule(scheduleDTO);

            if (resultScheduleDTO == null) {
                log.info("[ScheduleController]-[getSchedule] Schedule not found with name: {}", name);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponse(false, HttpStatus.NOT_FOUND, "Schedule not found with name"));
            }

            // UserID를 노출시키지 않아 보안 강화
            resultScheduleDTO.setUserId(null);

            return ResponseEntity.ok(new CommonResponse(true, HttpStatus.OK, "Schedule retrieved successfully", resultScheduleDTO));
        } catch (Exception e) {
            log.error("[ScheduleController]-[getSchedule] An error occurred while retrieving schedule", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CommonResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while retrieving schedule"));
        }
    }

    private String getUserIdFromContext(){
        // SecurityContext에서 Authentication으로 UserID를 받아온다
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (String) authentication.getPrincipal();
    }
}
