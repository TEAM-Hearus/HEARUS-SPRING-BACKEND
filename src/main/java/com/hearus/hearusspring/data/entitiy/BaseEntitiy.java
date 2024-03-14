package com.hearus.hearusspring.data.entitiy;


import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class BaseEntitiy {
    // @MappedSuperClass
    // 부모 클래스는 테이블과 매핑하지 않고 오로지 부모 클래스를 상속 받는 자식 클래스에게
    // 부모 클래스가 가지는 Column만 매핑정보로 제공
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
