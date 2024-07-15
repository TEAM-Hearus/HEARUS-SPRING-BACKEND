package com.hearus.hearusspring.service;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dto.schedule.ScheduleElementDTO;
import com.hearus.hearusspring.data.entitiy.schedule.ScheduleElementEntity;
import com.hearus.hearusspring.data.model.LectureModel;
import com.hearus.hearusspring.data.model.Problem;

public interface LectureService {
    CommonResponse addLecture(String userId, LectureModel lecture);
    CommonResponse putScript(String lectureId, String script);
    CommonResponse updateLecture(LectureModel lecture);
    CommonResponse deleteLecture(String userId, String lectureId);
    CommonResponse addProblem(String lectureId, Problem problem);
    CommonResponse updateProblem(String lectureId, String problemId, Problem newProblem);
    CommonResponse deleteProblem(String lectureId, String problemId);
    CommonResponse getLecture(String lectureId);
    CommonResponse getAllLecture(String userId);
}
