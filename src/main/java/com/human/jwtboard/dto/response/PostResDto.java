package com.human.jwtboard.dto.response;

import com.human.jwtboard.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostResDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String createdAt;
    private String updatedAt;
    private List<CommentResDto> comments;  // 게시글 조회시 댓글 목록을 가져 옴

    // Entity -> DTO
    public static PostResDto of(Post post) {
        PostResDto dto = new PostResDto();
        dto.id = post.getId();
        dto.title = post.getTitle();
        dto.content = post.getContent();
        dto.author = post.getAuthor().getName();
        dto.createdAt = post.getCreatedAt().toString();
        dto.updatedAt = post.getUpdatedAt().toString();

        // 댓글 리스트 변환 추가
        dto.comments = post.getComments().stream()
                .map(CommentResDto::of)
                .toList();

        return dto;
    }
}
