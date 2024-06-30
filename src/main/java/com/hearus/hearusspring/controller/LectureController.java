package com.hearus.hearusspring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dto.schedule.ScheduleDTO;
import com.hearus.hearusspring.data.dto.schedule.ScheduleElementDTO;
import com.hearus.hearusspring.data.model.LectureModel;
import com.hearus.hearusspring.service.LectureService;
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

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/lecture")
public class LectureController {
    @Autowired
    private LectureService lectureService;

    private CommonResponse response;

    @PostMapping(value="/addLecture")
    public ResponseEntity<CommonResponse> addSchedule(@Valid @RequestBody LectureModel lectureModel){
        log.info("[LectureController]-[addLecture] API Call");

        if(lectureModel.getScheduleElementId().isEmpty() || lectureModel.getName().isEmpty()){
            log.warn("[LectureController]-[addLecture] Failed : Empty Variables");
            response = new CommonResponse(false, HttpStatus.BAD_REQUEST,"Empty Name");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        String userId = getUserIdFromContext();

        response = lectureService.addLecture(userId, lectureModel);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    private String getUserIdFromContext(){
        // SecurityContext에서 Authentication으로 UserID를 받아온다
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (String) authentication.getPrincipal();
    }
}
