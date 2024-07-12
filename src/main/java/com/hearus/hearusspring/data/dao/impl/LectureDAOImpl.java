package com.hearus.hearusspring.data.dao.impl;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dao.LectureDAO;
import com.hearus.hearusspring.data.entitiy.UserEntity;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public CommonResponse getLecture(String lectureId) {
        try{
            LectureModel lecture = lectureRepository.findFirstById(lectureId);
            if(lecture == null)
                return new CommonResponse(false, HttpStatus.NOT_FOUND, "Lecture doesn't exists");
            return new CommonResponse(true, HttpStatus.OK, "LectureModel", lecture);
        }catch (Exception e){
            return new CommonResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get Lecture");
        }
    }
}
