package com.human.jwtboard.controller;

import com.human.jwtboard.dto.request.PostReqDto;
import com.human.jwtboard.dto.response.ApiResponse;
import com.human.jwtboard.dto.response.PostResDto;
import com.human.jwtboard.service.PostService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    // 게시글 전체 조회
    @GetMapping("/all")                          // ✅ "api/posts" → "/all"
    public ResponseEntity<ApiResponse<List<PostResDto>>> findAll() {
        return ResponseEntity.ok(ApiResponse.ok(postService.findAll()));
    }

    // 게시글 개별 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(postService.findById(id)));
    }

    // 게시글 작성
    @PostMapping                                 // ✅ @PutMapping → @PostMapping
    public ResponseEntity<ApiResponse<PostResDto>> create(
            @RequestBody @Valid PostReqDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("게시글이 작성되었습니다.", postService.create(dto)));
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResDto>> update(
            @PathVariable Long id,
            @RequestBody @Valid PostReqDto dto) { // ✅ PostResDto → PostReqDto
        return ResponseEntity.ok(ApiResponse.ok("게시글이 수정되었습니다.", postService.update(id, dto)));
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long postId) {
        postService.delete(postId);
        return ResponseEntity.ok(ApiResponse.ok("게시글이 삭제되었습니다.", null));
    }

    // 페이지네이션
    @GetMapping
    // GET /api/posts?page=1&size=10
    public ResponseEntity<ApiResponse<Page<PostResDto>>> findAllByPage(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            @Parameter(hidden = true)   // ← 이 한 줄 추가
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(postService.findAllByPage(pageable)));
    }
}