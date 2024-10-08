package com.example.library.library_management.dto.book.request;

import com.example.library.library_management.domain.Book;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookUpdateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @Length(min = 13, max = 13)
    private String isbn;

    @NotNull
    @PastOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @NotNull
    @Positive
    private Integer page;

    @NotNull
    @PositiveOrZero
    private Integer quantity;

    public static BookUpdateRequest fromBook(Book book) {
        return new BookUpdateRequest(
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getReleaseDate(),
                book.getPage(),
                book.getQuantity());
    }
}
