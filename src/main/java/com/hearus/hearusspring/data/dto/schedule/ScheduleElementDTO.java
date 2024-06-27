package com.hearus.hearusspring.data.dto.schedule;

import com.hearus.hearusspring.data.entitiy.schedule.ScheduleElementEntity;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ScheduleElementDTO {
    private Long id;
    private Long scheduleId;
    private String name;
    private String location;
    private String dayOfWeek;
    private Date startTime;
    private Date endTime;

    public ScheduleElementEntity toEntity() {
        return ScheduleElementEntity.builder()
                .id(id)
                .name(name)
                .location(location)
                .dayOfWeek(dayOfWeek)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}
