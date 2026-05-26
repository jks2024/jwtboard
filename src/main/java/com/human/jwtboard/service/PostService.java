package com.human.jwtboard.service;

import com.human.jwtboard.dto.request.PostReqDto;
import com.human.jwtboard.dto.response.PostResDto;
import com.human.jwtboard.entity.Member;
import com.human.jwtboard.entity.Post;
import com.human.jwtboard.exception.CustomException;
import com.human.jwtboard.repository.MemberRepository;
import com.human.jwtboard.repository.PostRepository;
import com.human.jwtboard.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
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
    public PostResDto create(PostReqDto dto) {
        Member author = getCurrentMember();
        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .author(author)
                .build();
        return PostResDto.of(postRepository.save(post));
    }

    // 게시글 수정
    @Transactional
    public PostResDto update(Long postId, PostReqDto dto) {
        Post post = getPost(postId);
        checkAuthor(post.getAuthor());
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        return PostResDto.of(post);
    }

    // 게시글 삭제
    @Transactional
    public void delete(Long postId) {
        Post post = getPost(postId);
        checkAuthor(post.getAuthor());
        postRepository.delete(post);
    }

    // 페이지네이션
    public Page<PostResDto> findAllByPage(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(PostResDto::of);
    }

    // private 헬퍼 메서드
    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() ->
                        new CustomException(HttpStatus.NOT_FOUND,
                                "게시글을 찾을 수 없습니다. id=" + postId));
    }


    private Member getCurrentMember() {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new CustomException(HttpStatus.UNAUTHORIZED,
                                "로그인 정보를 확인해주세요."));
    }

    private void checkAuthor(Member author) {
        Long currentId = SecurityUtil.getCurrentMemberId();
        if (!author.getId().equals(currentId)) {
            throw new CustomException(HttpStatus.FORBIDDEN,
                    "작성자만 수행할 수 있습니다.");
        }
    }

}
