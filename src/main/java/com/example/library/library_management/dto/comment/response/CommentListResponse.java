package com.example.library.library_management.dto.comment.response;

import com.example.library.library_management.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentListResponse {
    private Long id;

    private String username;

    private String content;

    private LocalDateTime createdAt;

    public static CommentListResponse fromComment(Comment comment) {
        return new CommentListResponse(
                comment.getId(),
                comment.getCreatedBy(),
                comment.getContent(),
                comment.getCreatedDate()
        );
    }
}
