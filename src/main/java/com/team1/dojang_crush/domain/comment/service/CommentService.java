package com.team1.dojang_crush.domain.comment.service;

import com.team1.dojang_crush.domain.comment.domain.Comment;
import com.team1.dojang_crush.domain.comment.repository.CommentRepository;
import com.team1.dojang_crush.domain.post.domain.Post;
import java.util.Optional;
import com.team1.dojang_crush.domain.comment.domain.dto.CommentCreatedRequestDTO;
import com.team1.dojang_crush.domain.comment.domain.dto.CommentResponseDTO;
import com.team1.dojang_crush.domain.comment.domain.dto.PostCommentListResponseDTO;
import com.team1.dojang_crush.domain.comment.domain.dto.UpdateCommentContentDTO;
import com.team1.dojang_crush.domain.comment.repository.CommentRepository;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.member.repository.MemberRepository;
import com.team1.dojang_crush.domain.post.domain.Post;
import com.team1.dojang_crush.domain.post.repository.PostRepository;
import com.team1.dojang_crush.global.exception.AppException;
import com.team1.dojang_crush.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentResponseDTO createNewComment(Long postId, CommentCreatedRequestDTO dto, Member member) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_POST, "게시글을 찾을 수 없습니다"));
        Comment newComment;
        if(dto.getParentId()==null){
            newComment = dto.from(post, member);

        }else {
            Comment parentComment = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_COMMENT, "연결할 댓글을 찾을 수 없습니다"));
            newComment = dto.from(post, member, parentComment);
        }
        Comment savedComment = commentRepository.save(newComment);

        return changeToDTO(savedComment);
    }

    @Transactional
    public PostCommentListResponseDTO findPostCommentList(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_POST, "게시글을 찾을 수 없습니다"));
        List<Comment> commentList = commentRepository.findAllByPost(post);
        return PostCommentListResponseDTO.from(commentList, post);
    }

    @Transactional
    public CommentResponseDTO changeCommentContent(Long commentId, UpdateCommentContentDTO dto) {
        Comment preComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_COMMENT, "변경할 댓글을 찾을 수 없습니다"));

        preComment.changeContent(dto.getContent());
        Comment postComment = commentRepository.save(preComment);
        return changeToDTO(postComment);
    }


    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }


    // post의 제일 최근 댓글 찾기
    @Transactional
    public Comment findRecentComment(Post post) {
        return commentRepository.findFirstByPostOrderByCreatedAtDesc(post)
                .orElse(null);
    }
  
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CommentResponseDTO changeToDTO(Comment comment){
        return CommentResponseDTO.from(comment);
    }
  
}
