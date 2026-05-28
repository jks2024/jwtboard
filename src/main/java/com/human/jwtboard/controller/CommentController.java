package com.human.jwtboard.controller;

import com.human.jwtboard.dto.request.CommentReqDto;
import com.human.jwtboard.dto.response.ApiResponse;
import com.human.jwtboard.dto.response.CommentResDto;
import com.human.jwtboard.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 댓글 작성 POST /api/posts/{postId}/comments
    @PostMapping
    public ResponseEntity<ApiResponse<CommentResDto>> create(@PathVariable Long postId, CommentReqDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("댓글이 작성되었습니다.",
                        commentService.create(postId, dto)));
    }

    // 댓글 목록 조회  GET /api/posts/{postId}/comments
    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResDto>>> findByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(ApiResponse.ok(commentService.findByPostId(postId)));
    }

    // 댓글 수정  PUT /api/posts/{postId}/comments/{commentId}
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResDto>> update(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody @Valid CommentReqDto dto) {
        return ResponseEntity.ok(
                ApiResponse.ok("댓글이 수정되었습니다.",
                        commentService.update(commentId, dto)));
    }

    // 댓글 삭제  DELETE /api/posts/{postId}/comments/{commentId}
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.ok(
                ApiResponse.ok("댓글이 삭제되었습니다.", null));
    }

}
