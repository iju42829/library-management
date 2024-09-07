package com.example.library.library_management.controller;

import com.example.library.library_management.controller.argumentResolver.Login;
import com.example.library.library_management.dto.book.request.BookReservationCreateRequest;
import com.example.library.library_management.dto.member.MemberSessionDto;
import com.example.library.library_management.service.book.BookReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/book-reservations")
@RequiredArgsConstructor
public class BookReservationController {

    private final BookReservationService bookReservationService;

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
}
