package com.hearus.hearusspring.service;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dto.schedule.ScheduleElementDTO;
import com.hearus.hearusspring.data.entitiy.schedule.ScheduleElementEntity;
import com.hearus.hearusspring.data.model.LectureModel;

public interface LectureService {
    CommonResponse addLecture(String userId, LectureModel lecture);
    CommonResponse putScript(String lectureId, String script);
    CommonResponse getLecture(String lectureId);
}
