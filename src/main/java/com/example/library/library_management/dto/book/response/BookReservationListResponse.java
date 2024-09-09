package com.example.library.library_management.dto.book.response;

import com.example.library.library_management.domain.BookReservation;
import com.example.library.library_management.domain.constants.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookReservationListResponse {

    private Long id;

    private String username;
    private String title;

    private ReservationStatus reservationStatus;

    private LocalDate reservationDate;
    private LocalDate expiryReservationDate;

    private LocalDate loanDate;
    private LocalDate loanDeadlineDate;

    public static BookReservationListResponse fromBookReservation(BookReservation bookReservation) {
        return new BookReservationListResponse(
                bookReservation.getId(),
                bookReservation.getMember().getUsername(),
                bookReservation.getBook().getTitle(),
                bookReservation.getReservationStatus(),
                bookReservation.getReservationDate(),
                bookReservation.getExpiryReservationDate(),
                bookReservation.getLoanDate(),
                bookReservation.getLoanDeadlineDate()
        );
    }
}
