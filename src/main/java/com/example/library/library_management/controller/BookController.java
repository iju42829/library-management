package com.example.library.library_management.controller;

import com.example.library.library_management.controller.argumentResolver.Login;
import com.example.library.library_management.dto.book.request.BookCreateRequest;
import com.example.library.library_management.dto.book.request.BookUpdateRequest;
import com.example.library.library_management.dto.book.response.BookDetailResponse;
import com.example.library.library_management.dto.book.response.BookListResponse;
import com.example.library.library_management.dto.member.MemberSessionDto;
import com.example.library.library_management.service.book.BookService;
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

import java.util.List;

import static com.example.library.library_management.controller.constants.BookConstants.*;

@Slf4j
@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @ModelAttribute("memberSessionDto")
    public MemberSessionDto addMemberSessionDto(@Login MemberSessionDto memberSessionDto) {
        return memberSessionDto;
    }

    @GetMapping
    public String retrieveBookList(@RequestParam(defaultValue = "1") int page,
                                   Model model) {
        PageRequest pageRequest = PageRequest
                .of(page - 1, DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.DESC, DEFAULT_SORT_FIELD));

        Slice<BookListResponse> books = bookService.getBooks(pageRequest);

        List<BookListResponse> content = books.getContent();

        model.addAttribute("books", content);
        model.addAttribute("currentPage", page);
        model.addAttribute("hasNextPage", books.hasNext());
        model.addAttribute("hasPreviousPage", books.hasPrevious());

        return "book/list-books";
    }
    
    @GetMapping("/{bookId}")
    public String retrieveBook(@PathVariable("bookId") Long bookId, Model model) {
        BookDetailResponse bookDetailResponse = bookService.getBookForDetail(bookId);

        model.addAttribute("bookDetailResponse", bookDetailResponse);

        return "book/detail-books";
    }
    
    @GetMapping("/add")
    public String addBookForm(Model model) {
        model.addAttribute("bookCreateRequest", new BookCreateRequest());

        return "book/add-books";
    }

    @PostMapping
    public String addBook(@Validated @ModelAttribute BookCreateRequest bookCreateRequest,
                          BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);

            return "book/add-books";
        }

        bookService.createBook(bookCreateRequest);

        return "redirect:/";
    }

    @GetMapping("/{bookId}/edit")
    public String editBookForm(Model model, @PathVariable Long bookId) {

        BookUpdateRequest bookUpdateRequest = bookService.getBookForUpdate(bookId);

        model.addAttribute("bookId", bookId);
        model.addAttribute("bookUpdateRequest", bookUpdateRequest);

        return "book/edit-books";
    }

    @PatchMapping("/{bookId}")
    public String editBook(@PathVariable Long bookId,
                           @Validated @ModelAttribute BookUpdateRequest bookUpdateRequest,
                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);

            return "book/edit-books";
        }

        bookService.updateBook(bookId, bookUpdateRequest);

        return "redirect:/";
    }

    @DeleteMapping("/{bookId}")
    public String deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);

        return "redirect:/";
    }
}
