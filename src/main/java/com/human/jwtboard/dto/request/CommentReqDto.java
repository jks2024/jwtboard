package com.human.jwtboard.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 프론트엔드에서 댓글 작성해서 전달하면 수신 받는 DTO
@Getter
@Setter
@NoArgsConstructor
public class CommentReqDto {
    @NotBlank(message = "댓글 내용은 필수 입력힙니다.")
    private String content;
}
