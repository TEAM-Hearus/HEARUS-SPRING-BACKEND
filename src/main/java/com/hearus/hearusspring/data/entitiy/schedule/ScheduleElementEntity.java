package com.hearus.hearusspring.data.entitiy.schedule;

import com.hearus.hearusspring.data.dto.schedule.ScheduleElementDTO;
import com.hearus.hearusspring.data.entitiy.BaseEntitiy;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "schedule_element")
public class ScheduleElementEntity extends BaseEntitiy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn 다대일 관계에서 외래 키 매핑
    @JoinColumn(name = "schedule_id")
    private ScheduleEntity schedule;

    private String name;

    private String location;

    private String dayOfWeek;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    public ScheduleElementDTO toDTO() {
        return ScheduleElementDTO.builder()
                .id(id)
                .scheduleId(schedule.getId())
                .name(name)
                .location(location)
                .dayOfWeek(dayOfWeek)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}