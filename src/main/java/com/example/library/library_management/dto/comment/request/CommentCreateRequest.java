package com.example.library.library_management.dto.comment.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateRequest {

    @NotNull
    private Long bookId;

    @NotBlank
    private String username;

    @NotBlank
    private String content;
}
