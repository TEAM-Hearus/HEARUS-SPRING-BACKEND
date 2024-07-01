package com.hearus.hearusspring.service.impl;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dao.impl.LectureDAOImpl;
import com.hearus.hearusspring.data.dto.schedule.ScheduleElementDTO;
import com.hearus.hearusspring.data.model.LectureModel;
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
    public CommonResponse getLecture(String lectureId) {
        return lectureDAO.getLecture(lectureId);
    }
}
