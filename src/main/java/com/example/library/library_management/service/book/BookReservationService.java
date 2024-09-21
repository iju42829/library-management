package com.example.library.library_management.service.book;

import com.example.library.library_management.dto.book.response.BookReservationListResponse;
import com.example.library.library_management.exception.book.BookLimitExceededException;
import com.example.library.library_management.exception.book.BookNotEnoughQuantityException;
import com.example.library.library_management.exception.book.BookNotFoundException;
import com.example.library.library_management.exception.book.BookReservationNotFoundException;
import com.example.library.library_management.exception.member.MemberCannotReserveBookException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;


public interface BookReservationService {
    /**
     * 멤버의 책 예약하는 메서드
     *
     * @param username 예약을 요청한 멤버 사용자의 이름
     * @param bookId 예약할 책 ID
     *
     * @return 생성된 책 예약 ID
     *
     * @throws MemberCannotReserveBookException 멤버가 예약할 수 없는 상태일 때 발생
     * @throws BookNotFoundException 책이 존재하지 않는 경우 발생
     * @throws BookLimitExceededException 멤버가 예약할 수 있는 최대 횟수를 초과한 경우 발생
     * @throws BookNotEnoughQuantityException 책의 수량이 부족한 경우 발생
     */
    Long reserveBook(String username, Long bookId);


    /**
     * 멤버의 책 예약을 대출로 변경하는 메서드
     *
     * @param bookReservationId 승인할 책 예약 ID
     *
     * @return 승인된 책 예약의 ID
     *
     * @throws BookReservationNotFoundException 책 예약이 존재하지 않는 경우 발생
     */
    Long approveBookReservation(Long bookReservationId);


    /**
     * 멤버의 책 예약을 반납 처리하는 메서드
     *
     * @param bookReservationId 반납할 책 예약 ID
     *
     * @return 반납 처리된 책 예약 ID
     *
     * @throws BookReservationNotFoundException 책 예약이 존재하지 않는 경우 발생
     */
    Long returnBookReservation(Long bookReservationId);

    /**
     * 멤버 이름을 기준으로 책 예약 정보를 페이지 단위로 조회,
     * 단 이름이 제공되지 않으면 전체 책 예약 정보를 조회
     *
     * @param username 예약을 조회할 멤버의 사용자 이름 (null 또는 빈 값일 경우 전체 조회)
     * @param pageable 페이지 정보
     *
     * @return 예약 정보의 페이지 객체를 반환
     */
    Slice<BookReservationListResponse> getBookReservationByUsername(String username, Pageable pageable);
}
