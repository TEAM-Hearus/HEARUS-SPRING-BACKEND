package com.hearus.hearusspring.data.dao.impl;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dao.LectureDAO;
import com.hearus.hearusspring.data.entitiy.UserEntity;
import com.hearus.hearusspring.data.entitiy.schedule.ScheduleElementEntity;
import com.hearus.hearusspring.data.entitiy.schedule.ScheduleEntity;
import com.hearus.hearusspring.data.model.LectureModel;
import com.hearus.hearusspring.data.repository.LectureRepository;
import com.hearus.hearusspring.data.repository.UserRepository;
import com.hearus.hearusspring.data.repository.schedule.ScheduleElementRepository;
import com.hearus.hearusspring.data.repository.schedule.ScheduleRepository;
import com.mongodb.DuplicateKeyException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
            userRepository.save(user);

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
}
