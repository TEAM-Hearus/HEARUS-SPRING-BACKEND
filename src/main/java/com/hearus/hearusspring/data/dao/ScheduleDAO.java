package com.hearus.hearusspring.data.dao;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.entitiy.UserEntity;
import com.hearus.hearusspring.data.entitiy.schedule.ScheduleEntity;

public interface ScheduleDAO {
    CommonResponse addSchedule(ScheduleEntity scheduleEntity);

    CommonResponse deleteSchedule(ScheduleEntity scheduleEntity);
}
