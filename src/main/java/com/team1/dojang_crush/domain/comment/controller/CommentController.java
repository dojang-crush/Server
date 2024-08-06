package com.team1.dojang_crush.domain.comment.controller;

import com.team1.dojang_crush.domain.comment.domain.dto.CommentCreatedRequestDTO;
import com.team1.dojang_crush.domain.comment.domain.dto.CommentResponseDTO;
import com.team1.dojang_crush.domain.comment.domain.dto.PostCommentListResponseDTO;
import com.team1.dojang_crush.domain.comment.domain.dto.UpdateCommentContentDTO;
import com.team1.dojang_crush.domain.comment.service.CommentService;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.member.dto.MemberRequestDTO;
import com.team1.dojang_crush.domain.member.repository.MemberRepository;
import com.team1.dojang_crush.global.exception.AppException;
import com.team1.dojang_crush.global.exception.ErrorCode;
import com.team1.dojang_crush.global.oauth.AuthUser;
import com.team1.dojang_crush.global.utils.JWTUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final JWTUtils jwtUtils;
    private final MemberRepository memberRepository;

    @PostMapping("/{postId}")
    public ResponseEntity<CommentResponseDTO> createNewComment(@AuthUser Member member, @PathVariable(name = "postId")Long postId,
                                                               @RequestBody @Valid final CommentCreatedRequestDTO dto){
        CommentResponseDTO responseDTO = commentService.createNewComment(postId, dto, member);

        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostCommentListResponseDTO> findPostCommentList(@AuthUser Member member, @PathVariable(name = "postId")Long postId){
        PostCommentListResponseDTO responseDTO = commentService.findPostCommentList(postId);

        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponseDTO> updateCommentContent(@AuthUser Member member, @PathVariable(name = "commentId")Long commentId,
                                                                   @RequestBody @Valid UpdateCommentContentDTO dto){
        CommentResponseDTO responseDTO = commentService.changeCommentContent(commentId, dto);

        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@AuthUser Member member, @PathVariable(name = "commentId")Long commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().body("댓글이 삭제되었습니다.");
    }

    @GetMapping("/test")
    public ResponseEntity testController(){
        Member member = memberRepository.findById(1l)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REQUEST, ""));
        MemberRequestDTO build = MemberRequestDTO.builder().imgUrl("").email("yujinalice00@gmail.com").nickname("").build();
        String token = jwtUtils.createToken(build);
        return ResponseEntity.ok().body(token);
    }



}
