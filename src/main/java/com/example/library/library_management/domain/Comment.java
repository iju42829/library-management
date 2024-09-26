package com.example.library.library_management.domain;

import com.example.library.library_management.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private String content;

    private Comment(Member member, Book book, String content) {
        this.member = member;
        this.book = book;
        this.content = content;
    }

    public static Comment createComment(Member member, Book book, String content) {
        return new Comment(member, book, content);
    }

    public void changeContent(String content) {
        this.content = content;
    }

}
