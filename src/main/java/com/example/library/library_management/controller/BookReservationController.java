package com.example.library.library_management.controller;

import com.example.library.library_management.controller.argumentResolver.Login;
import com.example.library.library_management.dto.book.request.BookReservationCreateRequest;
import com.example.library.library_management.dto.book.response.BookReservationListResponse;
import com.example.library.library_management.dto.member.MemberSessionDto;
import com.example.library.library_management.service.book.BookReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/book-reservations")
@RequiredArgsConstructor
public class BookReservationController {

    private final BookReservationService bookReservationService;

    @GetMapping
    public String retrieveBookReservations(@RequestParam(defaultValue = "1") int page,
                                           @RequestParam(required = false) String username,
                                           Model model) {

        PageRequest pageRequest = PageRequest.of(page - 1,
                10,
                Sort.by(Sort.Direction.DESC, "createdDate"));

        Slice<BookReservationListResponse> bookReservations = bookReservationService.getBookReservationByUsername(username, pageRequest);

        List<BookReservationListResponse> bookReservationListResponses = bookReservations.getContent();

        model.addAttribute("bookReservationListResponses", bookReservationListResponses);
        model.addAttribute("searchUsername", username);

        model.addAttribute("currentPage", page);
        model.addAttribute("hasNextPage", bookReservations.hasNext());
        model.addAttribute("hasPreviousPage", bookReservations.hasPrevious());

        return "book/list-book-reservations";
    }

    @PostMapping
    public String addBookReservation(@Login MemberSessionDto memberSessionDto,
                                     @Validated @ModelAttribute BookReservationCreateRequest bookReservationCreateRequest,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);

            redirectAttributes.addAttribute("bookId", bookReservationCreateRequest.getBookId());

            return "redirect:/books/{bookId}";
        }

        bookReservationService.reserveBook(memberSessionDto.getUsername(), bookReservationCreateRequest.getBookId());

        return "redirect:/";
    }

    @PatchMapping("/{bookReservationId}/approve")
    public String approveBookReservation(@PathVariable Long bookReservationId) {

        bookReservationService.approveBookReservation(bookReservationId);

        return "redirect:/book-reservations";
    }

    @PatchMapping("/{bookReservationId}/return")
    public String returnBookReservation(@PathVariable Long bookReservationId) {

        bookReservationService.returnBookReservation(bookReservationId);

        return "redirect:/book-reservations";
    }
}
