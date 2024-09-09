package com.example.library.library_management.service.book;

import com.example.library.library_management.dto.book.response.BookReservationListResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;


public interface BookReservationService {
    Long reserveBook(String username, Long bookId);

    Long approveBookReservation(Long bookReservationId);

    Long returnBookReservation(Long bookReservationId);

    Slice<BookReservationListResponse> getBookReservationByUsername(String username, Pageable pageable);
}
