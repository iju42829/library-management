package com.example.library.library_management.service.book;

import com.example.library.library_management.domain.Book;
import com.example.library.library_management.dto.book.request.BookCreateRequest;
import com.example.library.library_management.dto.book.request.BookUpdateRequest;
import com.example.library.library_management.dto.book.response.BookDetailResponse;
import com.example.library.library_management.dto.book.response.BookListResponse;
import com.example.library.library_management.exception.book.BookNotEnoughQuantityException;
import com.example.library.library_management.exception.book.BookNotFoundException;
import com.example.library.library_management.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Long createBook(BookCreateRequest bookCreateRequest) {
        Book book = Book.createBook(
                bookCreateRequest.getTitle(),
                bookCreateRequest.getAuthor(),
                bookCreateRequest.getIsbn(),
                bookCreateRequest.getReleaseDate(),
                bookCreateRequest.getPage(),
                bookCreateRequest.getQuantity()
        );

        bookRepository.save(book);
        return book.getId();
    }

    @Override
    public Long updateBook(Long bookId, BookUpdateRequest bookUpdateRequest) {
        Book book = bookRepository
                .findById(bookId)
                .orElseThrow(BookNotFoundException::new);

        book.update(bookUpdateRequest.getTitle(),
                bookUpdateRequest.getAuthor(),
                bookUpdateRequest.getIsbn(),
                bookUpdateRequest.getReleaseDate(),
                bookUpdateRequest.getPage(),
                bookUpdateRequest.getQuantity());

        return book.getId();
    }

    public Slice<BookListResponse> getBooks(Pageable pageable) {
        return bookRepository
                .findAll(pageable)
                .map(BookListResponse::fromBook);
    }

    @Override
    public BookUpdateRequest getBookForUpdate(Long bookId) {
        Book book = bookRepository
                .findById(bookId)
                .orElseThrow(BookNotFoundException::new);

        return BookUpdateRequest.fromBook(book);
    }

    @Override
    public BookDetailResponse getBookForDetail(Long bookId) {
        Book book = bookRepository
                .findById(bookId)
                .orElseThrow(BookNotFoundException::new);

        return BookDetailResponse.fromBook(book);
    }

    @Override
    public void deleteBook(Long bookId) {
        bookRepository.deleteById(bookId);
    }

    @Override
    public Book getBookById(Long bookId) {
        return bookRepository
                .findById(bookId)
                .orElseThrow(BookNotFoundException::new);
    }

    @Override
    public void validateBookQuantity(Book book) {
        if (book.getQuantity() <= 0) {
            throw new BookNotEnoughQuantityException();
        }
    }
}
