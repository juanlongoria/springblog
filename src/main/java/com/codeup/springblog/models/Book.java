package com.codeup.springblog.models;

import javax.persistence.*;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    // could also use the 'name = "nameGoesHEre"' argument to establish
    // a specific name that differs from the name of the field.
    @Column(nullable = false, length = 100)
    private String title;
    // could also use the 'unique = true' argument to establish a unique constraint
    @Column(nullable = false)
    private String author;

    public Book(){};

    public Book(long id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
