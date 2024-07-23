package com.team1.dojang_crush.domain.group.dto;

import com.team1.dojang_crush.domain.group.domain.Group;
import com.team1.dojang_crush.domain.member.domain.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GroupResponseDto {
    private Long groupId;
    private String groupName;
    //private String groupImageUrl;
    private String groupCode;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private List<Long> members;
    private List<GroupMemberDto> member;

    public static GroupResponseDto from(Group group, List<Member> members, List<GroupMemberDto> memberDto) {

        if (memberDto == null) {

            List<Long> memberIds = new ArrayList<>();
            for (Member member : members) {
                memberIds.add(member.getMemberId());
            }

            return new GroupResponseDto(
                    group.getGroupId(),
                    group.getGroupName(),
                    //group.getGroupImageUrl(),
                    group.getGroupCode(),
                    group.getCreatedAt(),
                    group.getModifiedAt(),
                    memberIds,
                    null
            );

        } else {
            return new GroupResponseDto(
                    group.getGroupId(),
                    group.getGroupName(),
                    //group.getGroupImageUrl(),
                    group.getGroupCode(),
                    group.getCreatedAt(),
                    group.getModifiedAt(),
                    null,
                    memberDto
            );
        }

    }
}


