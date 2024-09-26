package com.example.library.library_management.service.comment;

import com.example.library.library_management.dto.comment.request.CommentCreateRequest;
import com.example.library.library_management.dto.comment.response.CommentListResponse;
import com.example.library.library_management.dto.member.MemberSessionDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CommentService {
    Long createComment(CommentCreateRequest commentCreateRequest);

    Slice<CommentListResponse> getComments(Long bookId, Pageable pageable);

    Long deleteComment(Long commentId, MemberSessionDto memberSessionDto);
}
