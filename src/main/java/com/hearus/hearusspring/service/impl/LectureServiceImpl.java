package com.hearus.hearusspring.service.impl;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dao.impl.LectureDAOImpl;
import com.hearus.hearusspring.data.dto.schedule.ScheduleElementDTO;
import com.hearus.hearusspring.data.model.LectureModel;
import com.hearus.hearusspring.data.model.Problem;
import com.hearus.hearusspring.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LectureServiceImpl implements LectureService {
    @Autowired
    private LectureDAOImpl lectureDAO;
    @Override
    public CommonResponse addLecture(String userId, LectureModel lecture) {
        return lectureDAO.addLecture(userId, lecture);
    }

    @Override
    public CommonResponse putScript(String lectureId, String script) {
        return lectureDAO.putScript(lectureId, script);
    }

    @Override
    public CommonResponse updateLecture(LectureModel lecture) {
        return lectureDAO.updateLecture(lecture);
    }

    @Override
    public CommonResponse deleteLecture(String userId, String lectureId) {
        return lectureDAO.deleteLecture(userId, lectureId);
    }

    @Override
    public CommonResponse addProblem(String lectureId, Problem problem) {
        return lectureDAO.addProblem(lectureId, problem);
    }

    @Override
    public CommonResponse updateProblem(String lectureId, String problemId, Problem newProblem) {
        return lectureDAO.updateProblem(lectureId, problemId, newProblem);
    }

    @Override
    public CommonResponse deleteProblem(String lectureId, String problemId) {
        return lectureDAO.deleteProblem(lectureId, problemId);
    }

    @Override
    public CommonResponse getLecture(String lectureId, boolean changeScheduleElementID) {
        return lectureDAO.getLecture(lectureId, changeScheduleElementID);
    }

    @Override
    public CommonResponse getLectureByScheduleElementId(String scheduleElementId) { return lectureDAO.getLectureByScheduleElementId(scheduleElementId); }

    @Override
    public CommonResponse getAllLecture(String userId) {
        return lectureDAO.getAllLecture(userId);
    }
}
