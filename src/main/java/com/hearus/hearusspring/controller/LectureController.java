package com.hearus.hearusspring.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.common.environment.ConfigUtil;
import com.hearus.hearusspring.data.dto.ProblemReqDTO;
import com.hearus.hearusspring.data.dto.lecture.LectureDeleteDTO;
import com.hearus.hearusspring.data.dto.lecture.problem.ProblemAddDTO;
import com.hearus.hearusspring.data.dto.lecture.problem.ProblemDTO;
import com.hearus.hearusspring.data.dto.lecture.problem.ProblemDeleteDTO;
import com.hearus.hearusspring.data.model.LectureModel;
import com.hearus.hearusspring.data.model.Problem;
import com.hearus.hearusspring.service.LectureService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lecture")
public class LectureController {

    private final LectureService lectureService;
    private final ConfigUtil configUtil;
    private CommonResponse response;

    // Request를 Slice하여 Restructure요청할 때의 SIZE
    private final int SLICE_SIZE = 4;

    private String getUserIdFromContext(){
        // SecurityContext에서 Authentication으로 UserID를 받아온다
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (String) authentication.getPrincipal();
    }

    public CommonResponse validationRequest(BindingResult bindingResult, String logContent){
        List<FieldError> list = bindingResult.getFieldErrors();
        for(FieldError error : list) {
            log.info("{} Failed : {}",logContent, error.getDefaultMessage());
            CommonResponse response = new CommonResponse(false, HttpStatus.BAD_REQUEST, error.getDefaultMessage());
            return response;
        }
        return null;
    }

    @PostMapping(value="/addLecture")
    public ResponseEntity<CommonResponse> addSchedule(@Valid @RequestBody LectureModel lectureModel){
        log.info("[LectureController]-[addLecture] API Call");

        if(lectureModel.getName().isEmpty()){
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
    public ResponseEntity<CommonResponse> deleteLecture(@Valid @RequestBody LectureDeleteDTO lectureDeleteDTO, BindingResult bindingResult){
        log.info("[LectureController]-[deleteLecture] API Call");


        // Request 데이터 검증
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(validationRequest(bindingResult, "[LectureController]-[deleteLecture]"), HttpStatus.BAD_REQUEST);
        }

        String lectureId = lectureDeleteDTO.getLectureId();

        response = lectureService.deleteLecture(getUserIdFromContext(), lectureId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping(value="/addProblem")
    public ResponseEntity<CommonResponse> addProblem(@Valid @RequestBody ProblemAddDTO problemAddDTO, BindingResult bindingResult){
        log.info("[LectureController]-[addProblem] API Call");


        // Request 데이터 검증
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(validationRequest(bindingResult, "[LectureController]-[addProblem]"), HttpStatus.BAD_REQUEST);
        }

        String lectureId = problemAddDTO.getLectureId();
        ProblemDTO problem = problemAddDTO.getProblem();

        response = lectureService.addProblem(lectureId, problem.toEntity());
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
    public ResponseEntity<CommonResponse> deleteProblem(@Valid @RequestBody ProblemDeleteDTO problemDeleteDTO, BindingResult bindingResult){
        log.info("[LectureController]-[deleteProblem] API Call");

        // Request 데이터 검증
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(validationRequest(bindingResult, "[LectureController]-[deleteProblem]"), HttpStatus.BAD_REQUEST);
        }

        String lectureId = problemDeleteDTO.getLectureId();
        String problemId = problemDeleteDTO.getProblemId();

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
        response = lectureService.getLecture(lectureId, true);
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
        response = lectureService.getLecture(lectureId, false);

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

    @GetMapping("/restructureScript")
    public DeferredResult<ResponseEntity<CommonResponse>> restructureScript(@RequestParam("lectureId") String lectureId) {
        log.info("[LectureController]-[restructureScript] API Call");
        String fastAPIEndpoint = configUtil.getProperty("FAST_API_ENDPOINT");

        // Timeout 시간을 5분으로 설정
        long timeoutInMillis = 5 * 60 * 1000;
        DeferredResult<ResponseEntity<CommonResponse>> deferredResult = new DeferredResult<>(timeoutInMillis);

        CompletableFuture.runAsync(() -> {
            try{
                // LectureId로 Model을 가져와 processedScript에 저장
                LectureModel lectureModel = (LectureModel) lectureService.getLecture(lectureId, false).getObject();
                List<String> processedScript = lectureModel.getProcessedScript();
                List<String> newProcessedScript = new ArrayList<>();

                int start = 0, end;
                int requestLen = processedScript.size() / SLICE_SIZE;
                if(processedScript.size() % SLICE_SIZE != 0)
                    requestLen++;

                for(int i = 0;i < requestLen;i++) {
                    end = start + SLICE_SIZE;
                    if(end > processedScript.size())
                        end = processedScript.size();

                    log.info("[LectureController]-[restructureScript] restructuring script {} ~ {}", start, end);
                    Map<String, List<String>> requestMap = new HashMap<>();
                    requestMap.put("processedScript", new ArrayList<>(processedScript.subList(start, end)));

                    String jsonBody = new ObjectMapper().writeValueAsString(requestMap);

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
                            fastAPIEndpoint + "/restructure_script",
                            entity,
                            String.class
                    );

                    if (result.getStatusCode() == HttpStatus.OK) {
                        // JSON 문자열을 Map으로 파싱
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, List<String>> responseMap = objectMapper.readValue(result.getBody(), new TypeReference<Map<String, List<String>>>() {});

                        if (responseMap != null && responseMap.containsKey("processedScript")) {
                            // ., 패턴을 .로 replace
                            List<String> responseScript = responseMap.get("processedScript");
                            responseScript.replaceAll(s -> s.replaceAll("\\.,", "."));

                            // processedScript 키에 해당하는 리스트 추출
                            newProcessedScript.addAll(responseScript);
                            log.info("[LectureController]-[restructureScript] processedScript updated successfully");
                            start = end;
                        } else {
                            log.error("[LectureController]-[restructureScript] 'processedScript' key not found in the response");
                            i--;
                        }
                    } else
                        log.error("[LectureController]-[restructureScript] restructure script failed {} ~ {}", start, end);
                }

                lectureModel.setProcessedScript(newProcessedScript);
                response = lectureService.updateLecture(lectureModel);
            } catch (Exception e) {
                log.error("[LectureController]-[restructureScript] {}", e.getStackTrace());
                response = new CommonResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "Restructure Script Failed with Internal Server Error");
            } finally {
                log.info("[LectureController]-[restructureScript] {}", response.getMsg());
                deferredResult.setResult(ResponseEntity.status(response.getStatus()).body(response));
            }
        });

        return deferredResult;
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
                LectureModel lectureModel = (LectureModel) lectureService.getLecture(requestBody.getLectureId(), false).getObject();
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
