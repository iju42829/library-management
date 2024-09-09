package com.example.library.library_management.service.book;

import com.example.library.library_management.domain.Book;
import com.example.library.library_management.domain.BookReservation;
import com.example.library.library_management.domain.Member;
import com.example.library.library_management.domain.constants.ReservationStatus;
import com.example.library.library_management.dto.book.response.BookReservationListResponse;
import com.example.library.library_management.exception.book.BookLimitExceededException;
import com.example.library.library_management.exception.book.BookNotEnoughQuantityException;
import com.example.library.library_management.exception.book.BookNotFoundException;
import com.example.library.library_management.exception.member.MemberNotFoundException;
import com.example.library.library_management.repository.BookRepository;
import com.example.library.library_management.repository.BookReservationRepository;
import com.example.library.library_management.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookReservationServiceImpl implements BookReservationService {

    private final BookRepository bookRepository;
    private final BookReservationRepository bookReservationRepository;

    private final MemberRepository memberRepository;

    @Override
    public Long reserveBook(String username, Long bookId) {
        Member member = getMemberByUsername(username);

        Book book = getBookById(bookId);

        validateReservationLimit(member);

        validateBookQuantity(book);

        book.changeQuantity(book.getQuantity() - 1);

        BookReservation bookReservation = BookReservation
                .createBookReservation(member, book,
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                ReservationStatus.RESERVATION);

        bookReservationRepository.save(bookReservation);

        return bookReservation.getId();
    }

    @Override
    public Slice<BookReservationListResponse> getBookReservationByUsername(String username, Pageable pageable) {
        if (username == null || username.isEmpty()) {
            return bookReservationRepository
                    .findAll(pageable)
                    .map(BookReservationListResponse::fromBookReservation);
        }

        else  {
            return bookReservationRepository.findByMemberUsernameContaining(username, pageable)
                    .map(BookReservationListResponse::fromBookReservation);
        }
    }

    @Override
    public Long approveBookReservation(Long bookReservationId) {
        BookReservation bookReservation = getBookReservationById(bookReservationId);

        bookReservation.changeStatus(ReservationStatus.APPROVED);

        bookReservation.updateLoanDatesAfterApproval();

        return bookReservation.getId();
    }

    @Override
    public Long returnBookReservation(Long bookReservationId) {
        BookReservation bookReservation = getBookReservationById(bookReservationId);

        bookReservation.changeStatus(ReservationStatus.RETURNED);

        Book book = bookReservation.getBook();

        book.changeQuantity(book.getQuantity() + 1);

        return bookReservation.getId();
    }

    private BookReservation getBookReservationById(Long bookReservationId) {
        return bookReservationRepository
                .findById(bookReservationId)
                .orElseThrow();
    }

    private Member getMemberByUsername(String username) {
        return memberRepository
                .findByUsername(username)
                .orElseThrow(MemberNotFoundException::new);
    }

    private Book getBookById(Long bookId) {
        return bookRepository
                .findById(bookId)
                .orElseThrow(BookNotFoundException::new);
    }

    private static void validateBookQuantity(Book book) {
        if (book.getQuantity() <= 0) {
            throw new BookNotEnoughQuantityException();
        }
    }

    private void validateReservationLimit(Member member) {
        Long count = bookReservationRepository
                .countByMemberIdAndReservationStatusIn(member.getId(),
                Arrays.asList(ReservationStatus.RESERVATION, ReservationStatus.APPROVED, ReservationStatus.OVERDUE));

        if (count >= 2) {
            throw new BookLimitExceededException();
        }
    }
}
