package com.example.library.library_management.repository;

import com.example.library.library_management.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
