package com.team1.dojang_crush.global.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
@Slf4j
public class OAuth2CallbackController {
    @GetMapping("/login/oauth2/code/kakao")
    public String handleKakaoCallback(@RequestParam("accessToken") String accessToken, Model model) {
        // accessToken을 사용하여 필요한 처리를 합니다.
        model.addAttribute("accessToken", accessToken);
        log.info("로그인이 성공적으로 완료되었습니다. 발급된 accessToken: {}", accessToken);

        return "redirect:/"; // 인증 후 리다이렉트할 페이지를 지정합니다.
    }
}
