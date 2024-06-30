package com.hearus.hearusspring.data.dao;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.entitiy.schedule.ScheduleElementEntity;
import com.hearus.hearusspring.data.entitiy.schedule.ScheduleEntity;
import com.hearus.hearusspring.data.model.LectureModel;

public interface LectureDAO {
    CommonResponse addLecture(String userId, ScheduleElementEntity scheduleElementEntity, LectureModel lecture);
}
