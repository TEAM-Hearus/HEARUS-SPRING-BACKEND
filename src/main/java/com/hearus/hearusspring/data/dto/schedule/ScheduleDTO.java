package com.hearus.hearusspring.data.dto.schedule;

import com.hearus.hearusspring.data.entitiy.UserEntity;
import com.hearus.hearusspring.data.entitiy.schedule.ScheduleElementEntity;
import com.hearus.hearusspring.data.entitiy.schedule.ScheduleEntity;
import lombok.*;

import java.util.ArrayList;
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
    private String userId;

    public ScheduleEntity toEntity(UserEntity user) {
        List<ScheduleElementEntity> scheduleElementEntities = new ArrayList<>();
        if(scheduleElements != null) {
            for (ScheduleElementDTO scheduleElementDTO : scheduleElements) {
                scheduleElementEntities.add(scheduleElementDTO.toEntity());
            }
        }

        return ScheduleEntity.builder()
                .id(id)
                .scheduleElements(scheduleElementEntities)
                .name(name)
                .user(user)
                .build();
    }
}