package com.example.library.library_management.domain;

import com.example.library.library_management.domain.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private LocalDate releaseDate;

    @Column(nullable = false)
    private Integer page;

    @Column(nullable = false)
    private Integer quantity;

    private Book(String title, String author, String isbn, LocalDate releaseDate, Integer page, Integer quantity) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.releaseDate = releaseDate;
        this.page = page;
        this.quantity = quantity;
    }

    public static Book createBook(String title, String author, String isbn, LocalDate releaseDate, Integer page, Integer quantity) {
        return new Book(title, author, isbn, releaseDate, page, quantity);
    }

    public void update(String title, String author, String isbn, LocalDate releaseDate, Integer page, Integer quantity) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.releaseDate = releaseDate;
        this.page = page;
        this.quantity = quantity;
    }
}
