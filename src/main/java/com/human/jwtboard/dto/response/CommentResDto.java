package com.human.jwtboard.dto.response;

import com.human.jwtboard.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResDto {
    private Long id;
    private String content;
    private String author;
    private String createdAt;

    // Entity -> DTO
    public static CommentResDto of(Comment comment) {
        CommentResDto dto = new CommentResDto();
        dto.id = comment.getId();
        dto.content = comment.getContent();
        dto.author = comment.getAuthor().getName();
        dto.createdAt = comment.getCreatedAt().toString();
        return dto;
    }
}
