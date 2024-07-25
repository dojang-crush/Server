package com.team1.dojang_crush.domain.comment.controller;

import com.team1.dojang_crush.domain.comment.domain.dto.CommentCreatedRequestDTO;
import com.team1.dojang_crush.domain.comment.domain.dto.CommentResponseDTO;
import com.team1.dojang_crush.domain.comment.domain.dto.PostCommentListResponseDTO;
import com.team1.dojang_crush.domain.comment.domain.dto.UpdateCommentContentDTO;
import com.team1.dojang_crush.domain.comment.service.CommentService;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.member.repository.MemberRepository;
import com.team1.dojang_crush.global.exception.AppException;
import com.team1.dojang_crush.global.exception.ErrorCode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /* 임시 */
    private final MemberRepository memberRepository;

    private Member findMember(Long id){
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_POST, ""));
        return member;
    }

    @PostMapping("/{postId}")
    public ResponseEntity<CommentResponseDTO> createNewComment(@PathVariable(name = "postId")Long postId,
        /*테스트용 임시*/                                        @RequestBody @Valid final CommentCreatedRequestDTO dto){
        Member member = findMember(1l);

        CommentResponseDTO responseDTO = commentService.createNewComment(postId, dto, member);
        System.out.println(responseDTO.getWriter().getName());

        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostCommentListResponseDTO> findPostCommentList(@PathVariable(name = "postId")Long postId){
        Member member = findMember(1l);

        PostCommentListResponseDTO responseDTO = commentService.findPostCommentList(postId);

        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponseDTO> updateCommentContent(@PathVariable(name = "commentId")Long commentId,
                                                                   @RequestBody @Valid UpdateCommentContentDTO dto){
        CommentResponseDTO responseDTO = commentService.changeCommentContent(commentId, dto);

        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable(name = "commentId")Long commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().body("댓글이 삭제되었습니다.");
    }

}
