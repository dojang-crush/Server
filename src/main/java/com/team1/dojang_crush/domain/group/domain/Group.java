package com.team1.dojang_crush.domain.group.domain;

import com.team1.dojang_crush.domain.BaseEntity;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.place.domain.Place;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "`group`") // 테이블 이름을 이스케이프
public class Group extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id", updatable = false)
    private Long groupId;

    @Column(name = "group_name")
    @NotBlank
    private String groupName;

    @Column(name = "group_code")
    @NotBlank
    private String groupCode;

//    @Column(name = "group_image_url")
//    @NotBlank
//    private String groupImageUrl;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Member> members;



//    @Builder
//    public Group(String groupName, String groupCode, String groupImageUrl){
//        this.groupName = groupName;
//        this.groupCode = groupCode;
//        this.groupImageUrl = groupImageUrl;
//    }
    
    
    @Builder
    public Group(String groupName, String groupCode){
        this.groupName = groupName;
        this.groupCode = groupCode;
    }

//    public void updateImg(String url) {
//        this.groupImageUrl = url;
//    }


    public void updateName(String name) {
        this.groupName = name;
    }
}