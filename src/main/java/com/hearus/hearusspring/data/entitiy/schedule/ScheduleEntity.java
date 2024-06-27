package com.hearus.hearusspring.data.entitiy.schedule;

import com.hearus.hearusspring.data.dto.schedule.ScheduleDTO;
import com.hearus.hearusspring.data.dto.schedule.ScheduleElementDTO;
import com.hearus.hearusspring.data.entitiy.BaseEntitiy;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "schedule")
public class ScheduleEntity extends BaseEntitiy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleElementEntity> scheduleElements = new ArrayList<>();

    private String name;

    public void addScheduleElement(ScheduleElementEntity scheduleElement) {
        scheduleElements.add(scheduleElement);
        scheduleElement.setSchedule(this);
    }

    public void removeScheduleElement(ScheduleElementEntity scheduleElement) {
        scheduleElements.remove(scheduleElement);
        scheduleElement.setSchedule(null);
    }

    public ScheduleDTO toDTO() {
        List<ScheduleElementDTO> scheduleElementDTOs = scheduleElements.stream()
                .map(ScheduleElementEntity::toDTO)
                .collect(Collectors.toList());

        return ScheduleDTO.builder()
                .id(id)
                .scheduleElements(scheduleElementDTOs)
                .build();
    }
}