package com.example.library.library_management.service.book;

import com.example.library.library_management.dto.book.request.BookCreateRequest;
import com.example.library.library_management.dto.book.request.BookUpdateRequest;
import com.example.library.library_management.dto.book.response.BookDetailResponse;

public interface BookService {
    Long createBook(BookCreateRequest bookCreateRequest);

    Long updateBook(Long bookId, BookUpdateRequest bookUpdateRequest);

    BookUpdateRequest getBookForUpdate(Long bookId);

    BookDetailResponse getBookForDetail(Long bookId);

    void deleteBook(Long bookId);
}
