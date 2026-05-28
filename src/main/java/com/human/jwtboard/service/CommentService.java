package com.human.jwtboard.service;

import com.human.jwtboard.entity.Comment;
import com.human.jwtboard.entity.Member;
import com.human.jwtboard.entity.Post;
import com.human.jwtboard.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final MemberService memberService;

    // 댓글 작성

    // 댓글 수정

    // 댓글 삭제

    // Private 헬퍼 메서드
    // id로 댓글 가져오기
    private Comment getComment(Long commentId) {

    }
    // id로 게시글 가져 오기
    private Post getPost(Long postId) {

    }
    // 토큰의 클레임에서 memberId 가져를 가져와 Member 객체 반환
    private Member getCurrentMember() {

    }
    // 회원과 댓글 작성자가 동일한지 체크
    private void checkAuthor(Member author) {

    }
}
