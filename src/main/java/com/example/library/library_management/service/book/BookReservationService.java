package com.example.library.library_management.service.book;

public interface BookReservationService {
    Long reserveBook(String username, Long bookId);
}
