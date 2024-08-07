package com.hearus.hearusspring.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.common.environment.ConfigUtil;
import com.hearus.hearusspring.data.dto.ProblemReqDTO;
import com.hearus.hearusspring.data.model.LectureModel;
import com.hearus.hearusspring.data.model.Problem;
import com.hearus.hearusspring.service.LectureService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/v1/lecture")
public class LectureController {
    @Autowired
    private LectureService lectureService;

    @Autowired
    private ConfigUtil configUtil;

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

    @GetMapping("/getLectureByScheduleElement")
    public ResponseEntity<CommonResponse> getLectureByScheduleElement(@RequestParam("scheduleElementId") String scheduleElementId) {
        log.info("[LectureController]-[getLectureByScheduleElement] API Call");
        if(scheduleElementId.isEmpty()){
            response = new CommonResponse(false, HttpStatus.BAD_REQUEST,"Empty scheduleElementId");
            return ResponseEntity.status(response.getStatus()).body(response);
        }
        response = lectureService.getLectureByScheduleElementId(scheduleElementId);
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

    @GetMapping("/getAllLecture")
    public ResponseEntity<CommonResponse> getAllLecture() {
        log.info("[LectureController]-[getAllLecture] API Call");
        response = lectureService.getAllLecture(getUserIdFromContext());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping(value="/generateProblems")
    public DeferredResult<ResponseEntity<CommonResponse>> generateProblems(@Valid @RequestBody ProblemReqDTO requestBody){
        log.info("[LectureController]-[generateProblem] API Call");
        String fastAPIEndpoint = configUtil.getProperty("FAST_API_ENDPOINT");

        // Timeout 시간을 3분으로 설정
        long timeoutInMillis = 3 * 60 * 1000;
        DeferredResult<ResponseEntity<CommonResponse>> deferredResult = new DeferredResult<>(timeoutInMillis);

        CompletableFuture.runAsync(() -> {
            try{
                // LectureId로 Model을 가져와 내부의 Script를 하나로 합친 후 requestBody에 적용
                LectureModel lectureModel = (LectureModel) lectureService.getLecture(requestBody.getLectureId()).getObject();
                requestBody.setScript(String.join(" ", lectureModel.getProcessedScript()));

                String jsonBody = new ObjectMapper().writeValueAsString(requestBody);

                // FastAPI 비동기 요청 보내기
                RestTemplate restTemplate = new RestTemplate();

                // UTF-8 인코딩을 사용하는 StringHttpMessageConverter 설정
                StringHttpMessageConverter converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
                converter.setWriteAcceptCharset(false);
                restTemplate.getMessageConverters().add(0, converter);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

                ResponseEntity<String> result = restTemplate.postForEntity(
                        fastAPIEndpoint + "/generateProblems",
                        entity,
                        String.class
                );

                if (result.getStatusCode() == HttpStatus.OK) {
                    // result.getBody()에서 JSON 문자열 추출
                    String responseBody = result.getBody();
                    System.out.println(responseBody);

                    // JSON 문자열을 Map으로 파싱
                    ObjectMapper objectMapper = new ObjectMapper();
                    List<Map<String, Object>> objectList = objectMapper.readValue(responseBody, new TypeReference<List<Map<String, Object>>>() {});

                    response = new CommonResponse(true, HttpStatus.OK, "Problem Created", objectList);
                }
                else
                    response = new CommonResponse(false, (HttpStatus) result.getStatusCode(), "Problem Creation Failed");
            } catch (Exception e) {
                log.error("[LectureController]-[generateProblem] {}", e.getStackTrace());
                response = new CommonResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "Problem Creation Failed with Internal Server Error");
            } finally {
                log.info("[LectureController]-[generateProblem] {}", response.getMsg());
                deferredResult.setResult(ResponseEntity.status(response.getStatus()).body(response));
            }
        });

        return deferredResult;
    }

}
