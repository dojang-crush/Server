package com.team1.dojang_crush.domain.postImgUrl.controller;

import com.team1.dojang_crush.domain.postImgUrl.service.PostImgUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostImgUrlController {
    private final PostImgUrlService postImgUrlService;


}
