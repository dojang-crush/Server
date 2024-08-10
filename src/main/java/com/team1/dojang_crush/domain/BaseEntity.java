package com.team1.dojang_crush.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "modified_at", insertable = false)
    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @PrePersist
    public void onPrePersist(){
        LocalDateTime timestamp=toSeoulDate();
        this.createdAt=timestamp;
        this.modifiedAt=timestamp;
    }

    @PreUpdate
    public void onPreUpdate(){
        this.modifiedAt=toSeoulDate();
    }


    private LocalDateTime toSeoulDate(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTime = now.atZone(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime();

        return localDateTime;
    }

}
