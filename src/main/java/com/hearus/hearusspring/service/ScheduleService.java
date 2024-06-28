package com.hearus.hearusspring.service;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dto.schedule.ScheduleDTO;
import com.hearus.hearusspring.data.dto.schedule.ScheduleElementDTO;

public interface ScheduleService {
    CommonResponse addSchedule(ScheduleDTO scheduleDTO);
    CommonResponse deleteSchedule(ScheduleDTO scheduleDTO);
    CommonResponse addElement(ScheduleDTO scheduleDTO, ScheduleElementDTO scheduleElementDTO);
    CommonResponse deleteElement(ScheduleDTO scheduleDTO, ScheduleElementDTO scheduleElementDTO);
    ScheduleDTO getSchedule(ScheduleDTO scheduleDTO);
}
