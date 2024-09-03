package com.example.library.library_management.service.book;

import com.example.library.library_management.dto.book.request.BookCreateRequest;
import com.example.library.library_management.dto.book.request.BookUpdateRequest;

public interface BookService {
    Long createBook(BookCreateRequest bookCreateRequest);

    Long updateBook(Long bookId, BookUpdateRequest bookUpdateRequest);

    BookUpdateRequest getBook(Long bookId);

    void deleteBook(Long bookId);
}
