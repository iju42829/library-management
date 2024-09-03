package com.example.library.library_management.dto.book.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookCreateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @Length(min = 13, max = 13)
    private String isbn;

    @NotNull
    @PastOrPresent
    private LocalDate releaseDate;

    @NotNull
    @Positive
    private Integer page;

    @NotNull
    @PositiveOrZero
    private Integer quantity;
}
