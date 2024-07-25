
package com.team1.dojang_crush.domain.member.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRequestDTO {
    private String nickname;
    private String email;
    private String imgUrl; //
}
