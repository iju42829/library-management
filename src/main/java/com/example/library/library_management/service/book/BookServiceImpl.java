package com.example.library.library_management.service.book;

import com.example.library.library_management.domain.Book;
import com.example.library.library_management.dto.book.request.BookCreateRequest;
import com.example.library.library_management.dto.book.request.BookUpdateRequest;
import com.example.library.library_management.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        Book book = bookRepository.findById(bookId).orElseThrow();

        book.update(bookUpdateRequest.getTitle(),
                bookUpdateRequest.getAuthor(),
                bookUpdateRequest.getIsbn(),
                bookUpdateRequest.getReleaseDate(),
                bookUpdateRequest.getPage(),
                bookUpdateRequest.getQuantity());

        return book.getId();
    }


    @Override
    public BookUpdateRequest getBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow();

        return BookUpdateRequest.fromBook(book);
    }

    @Override
    public void deleteBook(Long bookId) {
        bookRepository.deleteById(bookId);
    }
}
