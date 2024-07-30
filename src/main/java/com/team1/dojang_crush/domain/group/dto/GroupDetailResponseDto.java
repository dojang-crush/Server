package com.team1.dojang_crush.domain.group.dto;

import com.team1.dojang_crush.domain.group.domain.Group;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GroupDetailResponseDto {
    private Long groupId;
    private String groupName;
    private String groupCode;
    //    private String groupImageUrl;
    private List<Long> members;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public GroupDetailResponseDto(Long groupId, String groupName, String groupCode, List<Long> members, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupCode = groupCode;
        this.members = members;
//        this.groupImageUrl = group.getGroupImageUrl();
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public GroupDetailResponseDto(Group group) {
        this.groupId = group.getGroupId();
        this.groupName = group.getGroupName();
        this.groupCode = group.getGroupCode();
        this.members = group.getMembers().stream().map(member -> member.getMemberId()).collect(Collectors.toList());
        this.createdDate = group.getCreatedAt();
        this.modifiedDate = group.getModifiedAt();
    }
}