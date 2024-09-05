package com.example.library.library_management.dto.book.response;

import com.example.library.library_management.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookListResponse {

    private Long id;

    private String title;

    private String author;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    public static BookListResponse fromBook(Book book) {
        return new BookListResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getReleaseDate());
    }
}
