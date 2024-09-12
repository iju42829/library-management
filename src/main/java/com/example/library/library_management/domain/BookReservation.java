package com.example.library.library_management.domain;

import com.example.library.library_management.domain.base.BaseTimeEntity;
import com.example.library.library_management.domain.constants.ReservationStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookReservation extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private LocalDate reservationDate;
    private LocalDate expiryReservationDate;

    private LocalDate loanDate;
    private LocalDate loanDeadlineDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    private BookReservation(Member member, Book book, LocalDate reservationDate,
                            LocalDate expiryReservationDate, ReservationStatus reservationStatus) {

        this.member = member;
        this.book = book;
        this.reservationDate = reservationDate;
        this.expiryReservationDate = expiryReservationDate;
        this.reservationStatus = reservationStatus;
    }

    public static BookReservation createBookReservation(Member member, Book book, LocalDate reservationDate,
                                                        LocalDate expiryReservationDate, ReservationStatus reservationStatus) {

        return new BookReservation(member, book, reservationDate, expiryReservationDate, reservationStatus);
    }

    public void changeStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public void updateLoanDatesAfterApproval(Long time) {
        this.loanDate = LocalDate.now();
        this.loanDeadlineDate = LocalDate.now().plusDays(time);
    }
}
