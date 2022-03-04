package com.codeup.springblog.controllers;

import com.codeup.springblog.models.Book;
import com.codeup.springblog.repositories.AuthorRepository;
import com.codeup.springblog.repositories.BookRepository;
import com.codeup.springblog.repositories.GenreRepository;
import com.codeup.springblog.services.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BookController {
    private BookRepository bookDao;
    private AuthorRepository authorsDao;
    private GenreRepository genresDao;
    private final EmailService emailService;

public BookController(BookRepository bookDao, AuthorRepository authorsDao, GenreRepository genresDao, EmailService emailService) {
    this.bookDao = bookDao;
        this.authorsDao = authorsDao;
        this.genresDao = genresDao;
    this.emailService = emailService;

}
    @GetMapping("/books")
    public String showBooks(Model model) {
        model.addAttribute("allBooks", bookDao.findAll());
        return "books/index";
    }
    @GetMapping("/books/create")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("genres", genresDao.findAll());
        return "books/create";
    }

    @GetMapping("/books/{id}")
    public String showBook(@PathVariable long id, Model model) {
        model.addAttribute("singleBook",bookDao.getById(id));
        return "books/show";
    }

    @GetMapping("/books/{id}/edit")
    public String showEditBookForm(@PathVariable long id, Model model) {
        model.addAttribute("bookToEdit", bookDao.getById(id));
        return "books/edit";
    }

    @PostMapping("/books/{id}/edit")
    public String submitBookEdit(@ModelAttribute Book bookToEdit, @PathVariable long id) {
        bookToEdit.setAuthor(authorsDao.getById(1L));
        bookDao.save(bookToEdit);
        return "redirect:/books/" + id;
    }

    @PostMapping("/books/create")
//    public String createBook(@ModelAttribute Book book, @RequestParam(name="genres") List<Long> genreIds) {
        public String createBook(@ModelAttribute Book book) {
            book.setAuthor(authorsDao.getById(1L));
//            List<Genre> genres = new ArrayList<>();
//            for (long id : genreIds) {
//                System.out.println(id);
//                genres.add(genresDao.getById(id));
//            }
//            book.setGenres(genres);
            bookDao.save(book);
            return "redirect:/books";
        }

    @GetMapping("/send-email")
    public String sendEmail() {
        emailService.prepareAndSend("Testing", "Did this work");
        return "redirect:/";
    }

    @GetMapping("/books/{id}/delete")
    public String deleteBook(@PathVariable long id) {
        bookDao.delete(bookDao.getById(id));
        return "redirect:/books";
    }

    }

//import com.codeup.springblog.repositories.BookRepository;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//public class BookController {
//    private BookRepository bookDao;
//
//    public BookController(BookRepository bookDao) {
//        this.bookDao = bookDao;
//    }
//
//    @GetMapping("/books")
//    public String showBooks(Model model) {
//        model.addAttribute("allBooks", bookDao.findAll());
//        return "books";
//    }
//
//
//
//}