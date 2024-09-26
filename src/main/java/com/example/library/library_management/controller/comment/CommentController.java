package com.example.library.library_management.controller.comment;

import com.example.library.library_management.controller.argumentResolver.Login;
import com.example.library.library_management.dto.comment.request.CommentCreateRequest;
import com.example.library.library_management.dto.comment.response.CommentListResponse;
import com.example.library.library_management.dto.member.MemberSessionDto;
import com.example.library.library_management.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public String addComment(@Validated @ModelAttribute CommentCreateRequest commentCreateRequest,
                             Model model) {
        commentService.createComment(commentCreateRequest);

        PageRequest pageRequest = PageRequest.of(0,
                10,
                Sort.by(Sort.Direction.DESC, "createdDate"));

        Slice<CommentListResponse> comments = commentService.getComments(commentCreateRequest.getBookId(), pageRequest);
        List<CommentListResponse> commentListResponse = comments.getContent();

        model.addAttribute("commentListResponse", commentListResponse);

        return "fragment/comment-list :: commentList";
    }


    @GetMapping
    public String retrieveComments(@RequestParam int page,
                                   @RequestParam(required = false) Long bookId,
                                   @Login MemberSessionDto memberSessionDto,
                                   Model model) {

        PageRequest pageRequest = PageRequest.of(page - 1,
                10,
                Sort.by(Sort.Direction.DESC, "createdDate"));

        Slice<CommentListResponse> comments = commentService.getComments(bookId, pageRequest);
        List<CommentListResponse> commentListResponse = comments.getContent();

        model.addAttribute("commentListResponse", commentListResponse);
        model.addAttribute("memberSessionDto", memberSessionDto);

        return "fragment/comment-list :: commentList";
    }

    @DeleteMapping("/{commentId}")
    public String deleteComment(@PathVariable Long commentId,
                                @Login MemberSessionDto memberSessionDto) {
        Long bookId = commentService.deleteComment(commentId, memberSessionDto);

        return "redirect:/books/" + bookId;
    }
}
