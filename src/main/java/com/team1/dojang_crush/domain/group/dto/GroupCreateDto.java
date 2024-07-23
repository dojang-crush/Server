package com.team1.dojang_crush.domain.group.dto;

import com.team1.dojang_crush.domain.group.domain.Group;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GroupCreateDto {

    @NotBlank(message = "그룹이름은 필수입니다.")
    private String groupName;

}
