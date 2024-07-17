package com.team1.dojang_crush.domain.comment.controller;

import com.team1.dojang_crush.domain.comment.domain.dto.CommentCreatedRequestDTO;
import com.team1.dojang_crush.domain.comment.domain.dto.CommentCreatedResponseDTO;
import com.team1.dojang_crush.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


}
