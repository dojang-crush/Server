package com.team1.dojang_crush.domain.auth.dto;

import com.team1.dojang_crush.domain.member.domain.dto.MemberRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {

    private MemberRequestDTO userinfo;
    private long accountId;
    private String accessToken;
    private String refreshToken;

}
