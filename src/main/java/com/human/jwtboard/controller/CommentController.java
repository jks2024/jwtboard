package com.human.jwtboard.controller;

import com.human.jwtboard.dto.request.CommentReqDto;
import com.human.jwtboard.dto.response.ApiResponse;
import com.human.jwtboard.dto.response.CommentResDto;
import com.human.jwtboard.service.CommentService;
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


    // 댓글 삭제  DELETE /api/posts/{postId}/comments/{commentId}

}
