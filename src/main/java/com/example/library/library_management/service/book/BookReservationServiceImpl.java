package com.example.library.library_management.service.book;

import com.example.library.library_management.domain.Book;
import com.example.library.library_management.domain.BookReservation;
import com.example.library.library_management.domain.Member;
import com.example.library.library_management.domain.constants.LoanAccessStatus;
import com.example.library.library_management.domain.constants.ReservationStatus;
import com.example.library.library_management.dto.book.response.BookReservationListResponse;
import com.example.library.library_management.exception.book.BookLimitExceededException;
import com.example.library.library_management.exception.book.BookReservationNotFoundException;
import com.example.library.library_management.exception.member.MemberCannotReserveBookException;
import com.example.library.library_management.repository.BookReservationRepository;
import com.example.library.library_management.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookReservationServiceImpl implements BookReservationService {

    private final BookReservationRepository bookReservationRepository;

    private final MemberService memberService;
    private final BookService bookService;

    @Override
    public Long reserveBook(String username, Long bookId) {
        Member member = memberService.getMemberByUsername(username);

        if (member.getLoanAccessStatus().equals(LoanAccessStatus.UNAVAILABLE)) {
            throw new MemberCannotReserveBookException();
        }

        Book book = bookService.getBookById(bookId);

        validateReservationLimit(member);

        bookService.validateBookQuantity(book);

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

        bookReservation.updateLoanDatesAfterApproval(14L);

        return bookReservation.getId();
    }

    @Override
    public Long returnBookReservation(Long bookReservationId) {
        BookReservation bookReservation = getBookReservationById(bookReservationId);

        updateReservationStatusAndQuantity(bookReservation, ReservationStatus.RETURNED);

        if (bookReservation.getLoanDeadlineDate().isBefore(LocalDate.now())) {
            Member member = bookReservation.getMember();
            member.changeLoanAccessStatus(LoanAccessStatus.UNAVAILABLE);

            long days = bookReservation.getLoanDeadlineDate().until(LocalDate.now(), ChronoUnit.DAYS);

            member.changeReservationLockDate(LocalDate.now().plusDays(days));
        }

        return bookReservation.getId();
    }

    @Scheduled(cron = "0 0 18 * * ?")
    public void checkLoanDeadlineDates() {
        LocalDate today = LocalDate.now();

        List<BookReservation> reservations = bookReservationRepository
                .findAllByReservationStatus(ReservationStatus.APPROVED);

        for (BookReservation reservation : reservations) {
            if (reservation.getLoanDeadlineDate().isBefore(today) || reservation.getLoanDeadlineDate().isEqual(today)) {
                reservation.changeStatus(ReservationStatus.OVERDUE);
            }
        }
    }

    @Scheduled(cron = "0 15 18 * * ?")
    public void scheduledCancelBookReservations() {
        LocalDate today = LocalDate.now();

        List<BookReservation> reservations = bookReservationRepository
                .findAllByReservationStatus(ReservationStatus.RESERVATION);

        for (BookReservation reservation : reservations) {
            if (reservation.getExpiryReservationDate().isBefore(today) || reservation.getExpiryReservationDate().isEqual(today)) {
                updateReservationStatusAndQuantity(reservation, ReservationStatus.CANCELLATION);
            }
        }
    }

    private void updateReservationStatusAndQuantity(BookReservation reservation, ReservationStatus reservationStatus) {
        Book book = reservation.getBook();

        book.changeQuantity(book.getQuantity() + 1);

        reservation.changeStatus(reservationStatus);
    }

    private BookReservation getBookReservationById(Long bookReservationId) {
        return bookReservationRepository
                .findById(bookReservationId)
                .orElseThrow(BookReservationNotFoundException::new);
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
