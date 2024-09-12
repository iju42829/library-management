package com.example.library.library_management.service.book;

import com.example.library.library_management.domain.BookReservation;
import com.example.library.library_management.domain.Member;
import com.example.library.library_management.domain.constants.LoanAccessStatus;
import com.example.library.library_management.domain.constants.ReservationStatus;
import com.example.library.library_management.dto.book.request.BookCreateRequest;
import com.example.library.library_management.dto.book.response.BookReservationListResponse;
import com.example.library.library_management.dto.member.request.MemberCreateRequest;
import com.example.library.library_management.exception.book.BookLimitExceededException;
import com.example.library.library_management.exception.book.BookNotEnoughQuantityException;
import com.example.library.library_management.exception.member.MemberNotFoundException;
import com.example.library.library_management.repository.BookRepository;
import com.example.library.library_management.repository.BookReservationRepository;
import com.example.library.library_management.repository.MemberRepository;
import com.example.library.library_management.service.member.SignUpService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class BookReservationServiceImplTest {

    @Autowired
    MessageSource ms;

    @Autowired
    SignUpService signUpService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BookService bookService;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookReservationService bookReservationService;

    @Autowired
    BookReservationRepository bookReservationRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Member member1;
    private Member member2;
    private Long bookId;
    private Integer bookQuantity;

    @BeforeEach
    void setUp() throws IOException {
        // 멤버1 생성
        String memberJson1 = ms.getMessage("test.member.first", null, Locale.getDefault());
        MemberCreateRequest memberCreateRequest1 = objectMapper.readValue(memberJson1, MemberCreateRequest.class);

        Long memberId1 = signUpService.signUp(memberCreateRequest1);
        member1 = memberRepository.findById(memberId1).orElseThrow(MemberNotFoundException::new);

        // 멤버2 생성
        String memberJson2 = ms.getMessage("test.member.second", null, Locale.getDefault());
        MemberCreateRequest memberCreateRequest2 = objectMapper.readValue(memberJson2, MemberCreateRequest.class);

        Long memberId2 = signUpService.signUp(memberCreateRequest2);

        member2 = memberRepository.findById(memberId2).orElseThrow(MemberNotFoundException::new);

        // 책 생성
        String bookJson = ms.getMessage("test.book", null, Locale.getDefault());
        BookCreateRequest bookCreateRequest = objectMapper.readValue(bookJson, BookCreateRequest.class);
        bookCreateRequest.setReleaseDate(LocalDate.now());

        bookId = bookService.createBook(bookCreateRequest);

        bookQuantity = bookCreateRequest.getQuantity();
    }


    @Test
    @DisplayName("도서를 성공적으로 예약한 경우")
    void reserveBookSuccess() {
        // given
        Long reservationId = bookReservationService.reserveBook(member1.getUsername(), bookId);

        // when
        BookReservation bookReservation = bookReservationRepository.findById(reservationId).orElseThrow();

        // then
        assertThat(bookReservation.getReservationStatus()).isEqualTo(ReservationStatus.RESERVATION);
        assertThat(bookReservation.getReservationDate()).isEqualTo(LocalDate.now());
        assertThat(bookReservation.getExpiryReservationDate()).isEqualTo(LocalDate.now().plusDays(1));
        assertThat(bookReservation.getBook().getQuantity()).isEqualTo(bookQuantity - 1);
    }

    @Test
    @DisplayName("예약 한도를 초과한 경우")
    void reserveBookWithExceedingLimit() {
        // given
        bookReservationService.reserveBook(member1.getUsername(), bookId);
        bookReservationService.reserveBook(member1.getUsername(), bookId);

        // when - then
        assertThatThrownBy(() -> bookReservationService
                .reserveBook(member1.getUsername(), bookId))
                .isInstanceOf(BookLimitExceededException.class);
    }

    @Test
    @DisplayName("책의 수량이 부족한 경우")
    void reserveBookWithInsufficientQuantity() {
        // given
        bookReservationService.reserveBook(member1.getUsername(), bookId);
        bookReservationService.reserveBook(member1.getUsername(), bookId);

        // when
        bookReservationService.reserveBook(member2.getUsername(), bookId);

        // then
        assertThatThrownBy(() -> bookReservationService
                .reserveBook(member2.getUsername(), bookId))
                .isInstanceOf(BookNotEnoughQuantityException.class);
    }

    @Test
    @DisplayName("사용자 이름을 통해 검색")
    void getBookReservationByUsername() {
        // given
        PageRequest pageRequest = PageRequest.of( 0, 10, Sort.by(Sort.Direction.DESC, "createdDate"));

        bookReservationService.reserveBook(member1.getUsername(), bookId);
        bookReservationService.reserveBook(member1.getUsername(), bookId);

        bookReservationService.reserveBook(member2.getUsername(), bookId);

        // when
        Slice<BookReservationListResponse> reservations = bookReservationService.getBookReservationByUsername(member1.getUsername(), pageRequest);

        // then
        assertThat(reservations).isNotEmpty();
        assertThat(reservations.getContent().size()).isEqualTo(2);

    }

    @Test
    @DisplayName("빈 사용자 이름을 통해 검색")
    void getBookReservationByEmptyUsername() {
        // given
        PageRequest pageRequest = PageRequest.of( 0, 10, Sort.by(Sort.Direction.DESC, "createdDate"));

        bookReservationService.reserveBook(member1.getUsername(), bookId);
        bookReservationService.reserveBook(member1.getUsername(), bookId);

        bookReservationService.reserveBook(member2.getUsername(), bookId);

        // when
        Slice<BookReservationListResponse> reservations = bookReservationService.getBookReservationByUsername(null, pageRequest);

        // then
        assertThat(reservations).isNotEmpty();
        assertThat(reservations.getContent().size()).isEqualTo(3);

    }

    @Test
    @DisplayName("책 예약 승인")
    void approveBookReservation() {
        // given
        Long bookReservationId = bookReservationService.reserveBook(member1.getUsername(), bookId);

        // when
        bookReservationService.approveBookReservation(bookReservationId);
        BookReservation bookReservation = bookReservationRepository.findById(bookReservationId).orElseThrow();

        // then
        assertThat(bookReservation.getReservationStatus()).isEqualTo(ReservationStatus.APPROVED);
        assertThat(bookReservation.getLoanDate()).isEqualTo(LocalDate.now());
        assertThat(bookReservation.getLoanDeadlineDate()).isEqualTo(LocalDate.now().plusDays(14));
    }

    @Test
    @DisplayName("책 기한내 반납한 경우")
    void returnBookReservationWithinDeadline() {
        // given
        Long bookReservationId = bookReservationService.reserveBook(member1.getUsername(), bookId);
        bookReservationService.approveBookReservation(bookReservationId);

        // when
        Long returnBookReservationId = bookReservationService.returnBookReservation(bookReservationId);
        BookReservation bookReservation = bookReservationRepository.findById(returnBookReservationId).orElseThrow();

        // then
        assertThat(bookReservation.getReservationStatus()).isEqualTo(ReservationStatus.RETURNED);
        assertThat(bookReservation.getMember().getLoanAccessStatus()).isEqualTo(LoanAccessStatus.AVAILABLE);
        assertThat(bookReservation.getBook().getQuantity()).isEqualTo(bookQuantity);
    }

    @Test
    @DisplayName("책 기한내 반납하지 못한 경우")
    void returnBookReservationAfterDeadline() {
        // given
        Long bookReservationId = bookReservationService.reserveBook(member1.getUsername(), bookId);
        bookReservationService.approveBookReservation(bookReservationId);
        BookReservation bookReservation = bookReservationRepository.findById(bookReservationId).orElseThrow();
        bookReservation.updateLoanDatesAfterApproval(-1L);

        // when
        Long returnBookReservationId = bookReservationService.returnBookReservation(bookReservationId);

        // then
        assertThat(bookReservation.getReservationStatus()).isEqualTo(ReservationStatus.RETURNED);
        assertThat(bookReservation.getMember().getLoanAccessStatus()).isEqualTo(LoanAccessStatus.UNAVAILABLE);
        assertThat(bookReservation.getBook().getQuantity()).isEqualTo(bookQuantity);

    }
}