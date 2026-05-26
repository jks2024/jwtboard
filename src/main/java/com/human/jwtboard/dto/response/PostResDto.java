package com.human.jwtboard.dto.response;

import com.human.jwtboard.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    // Entity -> DTO
    public static PostResDto of(Post post) {
        PostResDto dto = new PostResDto();
        dto.id = post.getId();
        dto.title = post.getTitle();
        dto.content = post.getContent();
        dto.author = post.getAuthor().getName();
        dto.createdAt = post.getCreatedAt().toString();
        dto.updatedAt = post.getUpdatedAt().toString();
        return dto;
    }
}
