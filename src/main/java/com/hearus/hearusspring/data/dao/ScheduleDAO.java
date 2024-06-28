package com.hearus.hearusspring.data.dao;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.entitiy.UserEntity;
import com.hearus.hearusspring.data.entitiy.schedule.ScheduleElementEntity;
import com.hearus.hearusspring.data.entitiy.schedule.ScheduleEntity;

public interface ScheduleDAO {
    CommonResponse addSchedule(ScheduleEntity scheduleEntity);
    CommonResponse deleteSchedule(ScheduleEntity scheduleEntity);
    CommonResponse addElement(ScheduleEntity scheduleEntity, ScheduleElementEntity scheduleElementEntity);
    CommonResponse deleteElement(ScheduleEntity scheduleEntity, ScheduleElementEntity scheduleElementEntity);
    ScheduleEntity getSchedule(ScheduleEntity scheduleEntity);
}
