package com.example.library.library_management.repository;

import com.example.library.library_management.domain.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Slice<Comment> findAllByBookId(Long bookId, Pageable pageable);
}
