package com.example.library.library_management.service.book;

import com.example.library.library_management.dto.book.request.BookCreateRequest;
import com.example.library.library_management.dto.book.request.BookUpdateRequest;
import com.example.library.library_management.dto.book.response.BookDetailResponse;
import com.example.library.library_management.dto.book.response.BookListResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BookService {
    Long createBook(BookCreateRequest bookCreateRequest);

    Long updateBook(Long bookId, BookUpdateRequest bookUpdateRequest);

    Slice<BookListResponse> getBooks(Pageable pageable);

    BookUpdateRequest getBookForUpdate(Long bookId);

    BookDetailResponse getBookForDetail(Long bookId);

    void deleteBook(Long bookId);
}
