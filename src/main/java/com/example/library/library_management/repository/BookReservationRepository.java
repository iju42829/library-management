package com.example.library.library_management.repository;

import com.example.library.library_management.domain.BookReservation;
import com.example.library.library_management.domain.constants.ReservationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookReservationRepository extends JpaRepository<BookReservation, Long> {

    Long countByMemberIdAndReservationStatusIn(Long memberId, List<ReservationStatus> reservationStatuses);

    List<BookReservation> findAllByReservationStatus(ReservationStatus reservationStatus);

    @EntityGraph(attributePaths = {"member", "book"})
    Slice<BookReservation> findByMemberUsernameContaining(String username, Pageable pageable);
}
