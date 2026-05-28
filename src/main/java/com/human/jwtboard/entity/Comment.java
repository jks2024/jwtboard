package com.human.jwtboard.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)  // 지연로딩, 실제 사용 될 때 그때 DB에서 정보를 가져옴
    @JoinColumn(name = "member_id", nullable = false)
    private Member author;

    // 연관관계의 주인: FK(post_id)를 이 필드가 관리
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public Comment(String content, Member author, Post post) {
        this.content = content;
        this.author  = author;
        this.post    = post;
    }
}
