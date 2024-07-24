package com.team1.dojang_crush.domain.auth.dto;

import com.team1.dojang_crush.domain.member.dto.MemberRequestDTO;
import com.team1.dojang_crush.domain.member.dto.MemberResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {

    private MemberRequestDTO memberInfo;
//    private long memberId;
    private String accessToken;
    private String refreshToken;

}
