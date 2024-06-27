package com.hearus.hearusspring.data.entitiy.schedule;

import com.hearus.hearusspring.data.dto.schedule.ScheduleDTO;
import com.hearus.hearusspring.data.dto.schedule.ScheduleElementDTO;
import com.hearus.hearusspring.data.entitiy.BaseEntitiy;
import com.hearus.hearusspring.data.entitiy.UserEntity;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

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
        List<ScheduleElementDTO> scheduleElementDTO = new ArrayList<>();
        if(scheduleElements != null) {
            for (ScheduleElementEntity scheduleElement : scheduleElements) {
                scheduleElementDTO.add(scheduleElement.toDTO());
            }
        }

        return ScheduleDTO.builder()
                .id(id)
                .scheduleElements(scheduleElementDTO)
                .userId(user.getId())
                .build();
    }
}