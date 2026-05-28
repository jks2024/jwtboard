package com.human.jwtboard.service;

import com.human.jwtboard.dto.request.CommentReqDto;
import com.human.jwtboard.dto.response.CommentResDto;
import com.human.jwtboard.entity.Comment;
import com.human.jwtboard.entity.Member;
import com.human.jwtboard.entity.Post;
import com.human.jwtboard.exception.CustomException;
import com.human.jwtboard.repository.CommentRepository;
import com.human.jwtboard.repository.MemberRepository;
import com.human.jwtboard.repository.PostRepository;
import com.human.jwtboard.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    // 댓글 작성
    @Transactional
    public CommentResDto create(Long postId, CommentReqDto dto) {
        Post post = getPost(postId);
        Member author = getCurrentMember();
        Comment comment = Comment.builder()
                .content(dto.getContent())
                .author(author)
                .post(post)
                .build();
        return CommentResDto.of(commentRepository.save(comment));
    }

    // 댓글 목록 조회
    public List<CommentResDto> findByPostId(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(CommentResDto::of)
                .toList();
    }

    // 댓글 수정
    @Transactional
    public CommentResDto update(Long commentId, CommentReqDto dto) {
        Comment comment = getComment(commentId);
        checkAuthor(comment.getAuthor());
        comment.setContent(dto.getContent());
        return CommentResDto.of(comment);
    }

    // 댓글 삭제
    @Transactional
    public void delete(Long commentId) {
        Comment comment = getComment(commentId);
        checkAuthor(comment.getAuthor());
        commentRepository.delete(comment);
    }

    // Private 헬퍼 메서드
    // id로 댓글 가져오기
    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(()-> new CustomException(
                        HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다. id=" + commentId));

    }
    // id로 게시글 가져 오기
    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(
                        HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다. id=" + postId));

    }
    // 토큰의 클레임에서 memberId 가져를 가져와 Member 객체 반환
    private Member getCurrentMember() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new CustomException(
                        HttpStatus.UNAUTHORIZED, "로그인 정보를 확인해주세요."));

    }
    // 회원과 댓글 작성자가 동일한지 체크
    private void checkAuthor(Member author) {
        if (!author.getId().equals(SecurityUtil.getCurrentMemberId())) {
            throw new CustomException(HttpStatus.FORBIDDEN, "작성자만 수행할 수 있습니다.");
        }
    }
}
