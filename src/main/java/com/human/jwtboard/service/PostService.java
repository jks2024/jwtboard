package com.human.jwtboard.service;

import com.human.jwtboard.dto.response.PostResDto;
import com.human.jwtboard.entity.Member;
import com.human.jwtboard.entity.Post;
import com.human.jwtboard.exception.CustomException;
import com.human.jwtboard.repository.MemberRepository;
import com.human.jwtboard.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.human.jwtboard.security.SecurityUtil.getCurrentMemberId;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    // 게시글 전체 조회
    public List<PostResDto> findAll() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(PostResDto::of)
                .toList();
    }

    // 개별 게시글 조회
    public PostResDto findById(Long id) {
        return PostResDto.of(postRepository.findById(id).orElseThrow());
    }

    // 게시글 작성
    @Transactional
    public PostResDto create(PostResDto dto) {
        Long memberId = getCurrentMemberId();
        Member author = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "로그인 정보를 확인해 주세요"));

        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .author(author)
                .build();

        return PostResDto.of(postRepository.save(post));
    }

    // 게시글 수정

    // 게시글 삭제

    // 페이지네이션 : 게시글은 목록 갯수가 많으므로 해당 페이지에 대한 게시글만 가져 오도록 함

}
