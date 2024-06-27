package com.hearus.hearusspring.service;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dto.schedule.ScheduleDTO;

public interface ScheduleService {
    CommonResponse addSchedule(ScheduleDTO scheduleDTO);
    CommonResponse deleteSchedule(ScheduleDTO scheduleDTO);
}
