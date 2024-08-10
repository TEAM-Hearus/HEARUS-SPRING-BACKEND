package com.hearus.hearusspring.data.dao.impl;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dao.LectureDAO;
import com.hearus.hearusspring.data.entitiy.UserEntity;
import com.hearus.hearusspring.data.entitiy.schedule.ScheduleElementEntity;
import com.hearus.hearusspring.data.model.LectureModel;
import com.hearus.hearusspring.data.model.Problem;
import com.hearus.hearusspring.data.repository.LectureRepository;
import com.hearus.hearusspring.data.repository.UserRepository;
import com.hearus.hearusspring.data.repository.schedule.ScheduleElementRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class LectureDAOImpl implements LectureDAO {

    UserRepository userRepository;
    ScheduleElementRepository scheduleElementRepository;
    LectureRepository lectureRepository;

    @Autowired
    public LectureDAOImpl(ScheduleElementRepository scheduleElementRepository, UserRepository userRepository, LectureRepository lectureRepository) {
        this.scheduleElementRepository = scheduleElementRepository;
        this.userRepository = userRepository;
        this.lectureRepository = lectureRepository;
    }

    @Override
    @Transactional
    public CommonResponse addLecture(String userId, LectureModel lecture) {
        try {
            if(lectureRepository.existsByName(lecture.getName()))
                return new CommonResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "Lecture name already exists");

            lecture.setCreatedAt(new Date());
            LectureModel savedLecture = lectureRepository.save(lecture);

            // User의 savedLectures에 저장된 강의 ID 추가
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

            // UserRepository에 새로운 SavedLectures 추가
            List<String> newSavedLectures = user.getSavedLectures();
            if (newSavedLectures == null) {
                newSavedLectures = new ArrayList<>();
            }
            newSavedLectures.add(savedLecture.getId());
            user.setSavedLectures(newSavedLectures);
            UserEntity updatedUser = userRepository.save(user);

            log.info("[LectureDAOImpl]-[addLecture] User {} SavedLecturesSize {}", userId, updatedUser.getSavedLectures().size());

            return CommonResponse.builder()
                    .status(HttpStatus.OK)
                    .isSuccess(true)
                    .msg("Lecture added successfully")
                    .object(savedLecture)
                    .build();
        } catch (Exception e) {
            log.error("Failed to add lecture", e);
            return CommonResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .isSuccess(false)
                    .msg("Failed to add lecture")
                    .build();
        }
    }

    @Override
    public CommonResponse putScript(String lectureId, String script) {
        try{
            LectureModel lecture = lectureRepository.findFirstById(lectureId);
            if(lecture == null)
                return new CommonResponse(false, HttpStatus.NOT_FOUND, "Lecture doesn't exists");
            lecture.getProcessedScript().add(script);
            LectureModel savedLecture = lectureRepository.save(lecture);

            return new CommonResponse(true, HttpStatus.OK, "Lecture ProcessedScript Added", savedLecture);
        }catch (Exception e){
            return new CommonResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to put Script");
        }
    }

    @Override
    public CommonResponse updateLecture(LectureModel lecture) {
        try{
            if(lecture == null)
                return new CommonResponse(false, HttpStatus.NOT_FOUND, "Lecture doesn't exists");
            LectureModel updatedLecture = lectureRepository.save(lecture);

            return new CommonResponse(true, HttpStatus.OK, "Lecture Updated", updatedLecture);
        }catch (Exception e){
            return new CommonResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to put Script");
        }
    }

    @Transactional
    @Override
    public CommonResponse deleteLecture(String userId, String lectureId) {
        try {
            lectureRepository.deleteById(lectureId);

            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

            List<String> newSavedLectures = user.getSavedLectures();
            newSavedLectures.remove(lectureId);
            user.setSavedLectures(newSavedLectures);
            UserEntity updatedUser = userRepository.save(user);

            log.info("[LectureDAOImpl]-[deleteLecture] User {} SavedLecturesSize {}", userId, updatedUser.getSavedLectures().size());
            return new CommonResponse(true, HttpStatus.OK, "Lecture Deleted successfully");
        } catch (Exception e) {
            log.error("Failed to delete lecture", e);
            return new CommonResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete lecture");
        }
    }

    @Override
    public CommonResponse addProblem(String lectureId, Problem problem) {
        try{
            LectureModel lecture = lectureRepository.findFirstById(lectureId);
            if(lecture == null)
                return new CommonResponse(false, HttpStatus.NOT_FOUND, "Lecture doesn't exists");

            lecture.addProblem(problem);
            LectureModel updatedLecture = lectureRepository.save(lecture);

            return new CommonResponse(true, HttpStatus.OK, "Problem Added", updatedLecture.getProblems());
        }catch (Exception e){
            return new CommonResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to add Problem");
        }
    }

    @Override
    public CommonResponse updateProblem(String lectureId, String problemId, Problem newProblem) {
        try{
            LectureModel lecture = lectureRepository.findFirstById(lectureId);
            if(lecture == null)
                return new CommonResponse(false, HttpStatus.NOT_FOUND, "Lecture doesn't exists");

            lecture.updateProblem(problemId, newProblem);
            LectureModel updatedLecture = lectureRepository.save(lecture);

            return new CommonResponse(true, HttpStatus.OK, "Lecture Updated", updatedLecture.getProblems());
        }catch (Exception e){
            return new CommonResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update Problem");
        }
    }

    @Override
    public CommonResponse deleteProblem(String lectureId, String problemId) {
        try{
            LectureModel lecture = lectureRepository.findFirstById(lectureId);
            if(lecture == null)
                return new CommonResponse(false, HttpStatus.NOT_FOUND, "Lecture doesn't exists");

            lecture.deleteProblem(problemId);
            LectureModel updatedLecture = lectureRepository.save(lecture);

            return new CommonResponse(true, HttpStatus.OK, "Lecture Updated", updatedLecture.getProblems());
        }catch (Exception e){
            return new CommonResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete Problem");
        }
    }

    @Override
    public CommonResponse getLecture(String lectureId, boolean changeScheduleElementID) {
        try{
            LectureModel lecture = lectureRepository.findFirstById(lectureId);
            if(lecture == null)
                return new CommonResponse(false, HttpStatus.NOT_FOUND, "Lecture doesn't exists");

            if(changeScheduleElementID && lecture.getScheduleElementId() != null) {
                ScheduleElementEntity scheduleElement = scheduleElementRepository.findFirstById(Long.valueOf(lecture.getScheduleElementId()));
                if(scheduleElement != null) {
                    lecture.setScheduleElementId(scheduleElement.getName());
                }else{
                    lecture.setScheduleElementId(null);
                    lectureRepository.save(lecture);
                }
            }

            return new CommonResponse(true, HttpStatus.OK, "LectureModel", lecture);
        }catch (Exception e){
            return new CommonResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get Lecture");
        }
    }

    @Override
    public CommonResponse getLectureByScheduleElementId(String scheduleElementId) {
        try {
            List<LectureModel> lectures = lectureRepository.findByScheduleElementId(scheduleElementId);

            if (lectures.isEmpty())
                return new CommonResponse(false, HttpStatus.NOT_FOUND, "No lectures found for the given scheduleElementId");

            // processedScript와 problems를 null로 설정
            List<LectureModel> simplifiedLectures = lectures.stream()
                    .peek(lecture -> {
                        lecture.setProcessedScript(null);
                        lecture.setProblems(null);
                    })
                    .toList();

            return new CommonResponse(true, HttpStatus.OK, "Lectures retrieved successfully", simplifiedLectures);
        } catch (Exception e) {
            log.error("Failed to get Lectures for scheduleElementId: " + scheduleElementId, e);
            return new CommonResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get Lectures");
        }
    }

    @Transactional
    @Override
    public CommonResponse getAllLecture(String userId) {
        try{
            // User의 savedLectures에 저장된 강의 ID 추가
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

            // UserRepository에 새로운 SavedLectures 추가
            List<String> userLectures = user.getSavedLectures();
            if (userLectures == null)
                return new CommonResponse(true, HttpStatus.OK, "User has no Lecture");

            List<LectureModel> lectureList = new ArrayList<>();
            for (String lectureId : userLectures) {
                LectureModel lecture = lectureRepository.findFirstById(lectureId);
                if(lecture == null) {
                    log.info("[LectureDAO]-[getAllLecture] Lecture {} removed from User {}", lectureId, userId);
                    userLectures.remove(lectureId);
                    continue;
                }

                List<String> processedScript = lecture.getProcessedScript();
                if (processedScript != null && !processedScript.isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    for (String script : processedScript) {
                        if (sb.length() + script.length() <= 100) {
                            sb.append(script);
                        } else {
                            if (100 - sb.length() > 0)
                                sb.append(script, 0, 100 - sb.length());
                            break;
                        }
                    }
                    lecture.setProcessedScript(Collections.singletonList(sb.toString()));
                }

                lecture.setProblems(null);
                lectureList.add(lecture);
            }

            user.setSavedLectures(userLectures);
            userRepository.save(user);

            return new CommonResponse(true, HttpStatus.OK, "LectureLists", lectureList);
        }catch (Exception e){
            return new CommonResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get AllLecture");
        }
    }
}
