package com.team1.dojang_crush.domain.group.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AddGroupMemberDto {

    @NotBlank(message = "그룹코드는 필수입니다.")
    private String groupCode;
}
