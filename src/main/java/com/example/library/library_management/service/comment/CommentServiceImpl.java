package com.example.library.library_management.service.comment;

import com.example.library.library_management.domain.Book;
import com.example.library.library_management.domain.Comment;
import com.example.library.library_management.domain.Member;
import com.example.library.library_management.dto.comment.request.CommentCreateRequest;
import com.example.library.library_management.dto.comment.response.CommentListResponse;
import com.example.library.library_management.dto.member.MemberSessionDto;
import com.example.library.library_management.exception.comment.CommentNotFoundException;
import com.example.library.library_management.exception.member.MemberUnauthorizedException;
import com.example.library.library_management.repository.CommentRepository;
import com.example.library.library_management.service.book.BookService;
import com.example.library.library_management.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final BookService bookService;
    private final MemberService memberService;
    private final CommentRepository commentRepository;

    @Override
    public Long createComment(CommentCreateRequest commentCreateRequest) {
        Member member = memberService.getMemberByUsername(commentCreateRequest.getUsername());

        Book book = bookService.getBookById(commentCreateRequest.getBookId());

        Comment comment = Comment.createComment(member, book, commentCreateRequest.getContent());

        commentRepository.save(comment);

        return comment.getId();
    }

    @Override
    public Slice<CommentListResponse> getComments(Long bookId, Pageable pageable) {
        return commentRepository
                .findAllByBookId(bookId, pageable)
                .map(CommentListResponse::fromComment);
    }

    @Override
    public Long deleteComment(Long commentId, MemberSessionDto memberSessionDto) {
        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        if (memberSessionDto.getUsername().equals(comment.getCreatedBy()) || memberSessionDto.getIsAdmin()) {
            Long bookId = comment.getBook().getId();

            commentRepository.delete(comment);

            return bookId;
        }

        else {
            throw new MemberUnauthorizedException();
        }
    }
}
