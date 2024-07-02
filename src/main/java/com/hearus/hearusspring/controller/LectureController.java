package com.hearus.hearusspring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dto.schedule.ScheduleDTO;
import com.hearus.hearusspring.data.dto.schedule.ScheduleElementDTO;
import com.hearus.hearusspring.data.model.LectureModel;
import com.hearus.hearusspring.data.model.Problem;
import com.hearus.hearusspring.service.LectureService;
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
@RequestMapping("/api/v1/lecture")
public class LectureController {
    @Autowired
    private LectureService lectureService;

    private CommonResponse response;

    private String getUserIdFromContext(){
        // SecurityContext에서 Authentication으로 UserID를 받아온다
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (String) authentication.getPrincipal();
    }

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

    @PutMapping(value="/putScript")
    public ResponseEntity<CommonResponse> putScript(@Valid @RequestBody Map<String, Object> requestBody){
        ObjectMapper objectMapper = new ObjectMapper();

        String lectureId = objectMapper.convertValue(requestBody.get("lectureId"), String.class);
        String script = objectMapper.convertValue(requestBody.get("script"), String.class);

        log.info("[LectureController]-[putScript] API Call - LectureId : {}", lectureId);

        if(lectureId.isEmpty() || script.isEmpty()){
            response = new CommonResponse(false, HttpStatus.BAD_REQUEST,"Empty Variables");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        response = lectureService.putScript(lectureId, script);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping(value="/updateLecture")
    public ResponseEntity<CommonResponse> updateLecture(@Valid @RequestBody LectureModel lectureModel){
        log.info("[LectureController]-[updateLecture] API Call");

        if(lectureModel.getId().isEmpty()){
            log.warn("[LectureController]-[updateLecture] Failed : Empty Variables");
            response = new CommonResponse(false, HttpStatus.BAD_REQUEST,"Empty Id");
            return ResponseEntity.status(response.getStatus()).body(response);
        }
        response = lectureService.updateLecture(lectureModel);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping(value="/deleteLecture")
    public ResponseEntity<CommonResponse> deleteLecture(@Valid @RequestBody Map<String, String> requestBody){
        log.info("[LectureController]-[deleteLecture] API Call");

        ObjectMapper objectMapper = new ObjectMapper();
        String lectureId = objectMapper.convertValue(requestBody.get("lectureId"), String.class);

        if(lectureId.isEmpty()){
            log.warn("[LectureController]-[deleteLecture] Failed : Empty LectureId");
            response = new CommonResponse(false, HttpStatus.BAD_REQUEST,"Empty LectureId");
            return ResponseEntity.status(response.getStatus()).body(response);
        }
        response = lectureService.deleteLecture(getUserIdFromContext(), lectureId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping(value="/addProblem")
    public ResponseEntity<CommonResponse> addProblem(@Valid @RequestBody Map<String, Object> requestBody){
        log.info("[LectureController]-[addProblem] API Call");

        ObjectMapper objectMapper = new ObjectMapper();
        String lectureId = objectMapper.convertValue(requestBody.get("lectureId"), String.class);
        Problem problem = objectMapper.convertValue(requestBody.get("problem"), Problem.class);

        if(lectureId.isEmpty()){
            log.warn("[LectureController]-[addLecture] Failed : Empty LectureId");
            response = new CommonResponse(false, HttpStatus.BAD_REQUEST,"Empty LectureId");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        response = lectureService.addProblem(lectureId, problem);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping(value="/updateProblem")
    public ResponseEntity<CommonResponse> updateProblem(@Valid @RequestBody Map<String, Object> requestBody){
        log.info("[LectureController]-[addProblem] API Call");

        ObjectMapper objectMapper = new ObjectMapper();
        String lectureId = objectMapper.convertValue(requestBody.get("lectureId"), String.class);
        Problem newProblem = objectMapper.convertValue(requestBody.get("problem"), Problem.class);

        if(lectureId.isEmpty()){
            log.warn("[LectureController]-[addLecture] Failed : Empty LectureId");
            response = new CommonResponse(false, HttpStatus.BAD_REQUEST,"Empty LectureId");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        response = lectureService.updateProblem(lectureId, newProblem.getId(), newProblem);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping(value="/deleteProblem")
    public ResponseEntity<CommonResponse> deleteProblem(@Valid @RequestBody Map<String, String> requestBody){
        log.info("[LectureController]-[addProblem] API Call");

        ObjectMapper objectMapper = new ObjectMapper();
        String lectureId = objectMapper.convertValue(requestBody.get("lectureId"), String.class);
        String problemId = objectMapper.convertValue(requestBody.get("problemId"), String.class);

        if(lectureId.isEmpty() || problemId.isEmpty()){
            log.warn("[LectureController]-[addLecture] Failed : Empty Variables");
            response = new CommonResponse(false, HttpStatus.BAD_REQUEST,"Empty Variables");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        response = lectureService.deleteProblem(lectureId, problemId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/getLecture")
    public ResponseEntity<CommonResponse> getLecture(@RequestParam("lectureId") String lectureId) {
        log.info("[LectureController]-[getLecture] API Call");
        if(lectureId.isEmpty()){
            response = new CommonResponse(false, HttpStatus.BAD_REQUEST,"Empty LectureId");
            return ResponseEntity.status(response.getStatus()).body(response);
        }
        response = lectureService.getLecture(lectureId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/getProblem")
    public ResponseEntity<CommonResponse> getProblem(@RequestParam("lectureId") String lectureId) {
        log.info("[LectureController]-[getLecture] API Call");
        if(lectureId.isEmpty()){
            response = new CommonResponse(false, HttpStatus.BAD_REQUEST,"Empty LectureId");
            return ResponseEntity.status(response.getStatus()).body(response);
        }
        response = lectureService.getLecture(lectureId);

        LectureModel lecture = (LectureModel) response.getObject();
        response.setObject(lecture.getProblems());

        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
