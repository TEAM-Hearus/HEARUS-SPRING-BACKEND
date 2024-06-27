package com.hearus.hearusspring.data.dto.schedule;

import com.hearus.hearusspring.data.entitiy.schedule.ScheduleElementEntity;
import com.hearus.hearusspring.data.entitiy.schedule.ScheduleEntity;
import lombok.*;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ScheduleDTO {
    private Long id;
    private List<ScheduleElementDTO> scheduleElements;
    private String name;

    public ScheduleEntity toEntity() {
        List<ScheduleElementEntity> scheduleElementEntities = scheduleElements.stream()
                .map(ScheduleElementDTO::toEntity)
                .collect(Collectors.toList());

        return ScheduleEntity.builder()
                .id(id)
                .scheduleElements(scheduleElementEntities)
                .build();
    }
}