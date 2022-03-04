package com.codeup.springblog;

import com.codeup.springblog.models.Book;
import com.codeup.springblog.models.User;
import com.codeup.springblog.repositories.BookRepository;
import com.codeup.springblog.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringblogApplication.class)
@AutoConfigureMockMvc
public class BooksIntegrationTests {

    private User testUser;
    private HttpSession httpSession;

    @Autowired
    private MockMvc mvc;

    @Autowired
    UserRepository userDao;

    @Autowired
    BookRepository bookDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Before
    public void setup() throws Exception {
        testUser = userDao.findByUsername("testUser");

        // Creates the test user if not exists
        if (testUser == null) {
            User newUser = new User();
            newUser.setUsername("testUser");
            newUser.setPassword(passwordEncoder.encode("password"));
            newUser.setEmail("testUser@codeup.com");
            testUser = userDao.save(newUser);
        }

        httpSession = this.mvc.perform(post("/login").with(csrf())
                        .param("username", "testUser")
                        .param("password", "password"))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("/posts"))
                .andReturn()
                .getRequest()
                .getSession();
    }

    // SANITY TESTS

    @Test
    public void contextLoads() {
        // Sanity Test, just to make sure the MVC bean is working.
        assertNotNull(mvc);
    }

    @Test
    public void testIfUserSessionIsActive() {
        // It makes sure the returned session is not null.
        assertNotNull(httpSession);
    }

    // CRUD FUNCTIONALITY TESTING


    // CREATE
    @Test
    public void testCreateBook() throws Exception{
        // Makes a post request to /books/create and expecta redirection to the book.
        this.mvc.perform(post("/books/create").with(csrf())
                        .session((MockHttpSession) httpSession)
                        .param("title", "Alice in Wonderland")
//                        .param("genre_id", "6"))
                        // param name is genres, can enter comma separated string to point to multiple values.
                        .param("genres", "1, 6"))
                .andExpect(status().is3xxRedirection());
    }

    // READ
    @Test
    public void testShowAd() throws Exception {

        Book existingBook = bookDao.findAll().get(0);

        // Makes a Get request to /ads/{id} and expect a redirection to the Ad show page
        this.mvc.perform(get("/books/" + existingBook.getId()))
                .andExpect(status().isOk())
                // Test the dynamic content of the page
                .andExpect(content().string(containsString(existingBook.getTitle())));
    }

    @Test
    public void testBooksIndex() throws Exception {
        Book existingBook = bookDao.findAll().get(0);

        this.mvc.perform(get("/books"))
                .andExpect(status().isOk())
                // checking static content of the page.
                .andExpect(content().string(containsString("Notable Books")))
                // checking dynamic content of the page.
                .andExpect(content().string(containsString(existingBook.getTitle())));
    }

    // UPDATE

    @Test
    public void testEditBook() throws Exception {
        Book existingBook = bookDao.findAll().get(0);

        // Makes a post request to /books/{id}/edit and expect a redirection to the Book show page
        this.mvc.perform(post("/books/" + existingBook.getId() + "/edit").with(csrf())
                        .session((MockHttpSession) httpSession)
//                        .param("title", "edited title"))
                        .param("title", "new edit title")
                        .param("genres", "6"))
                .andExpect(status().is3xxRedirection());
        // Makes a get request to /books/{id}/edit and expect a redirection to the book show page.
        this.mvc.perform(get("/books/" + existingBook.getId()))
                .andExpect(status().isOk())
//                .andExpect(content().string(containsString("edited title")));
                .andExpect(content().string(containsString("has rabbits")))
                .andExpect(content().string(containsString("new edit title")));
    }

    // DELETE
    @Test
    public void testDeleteBook() throws Exception {
        if(bookDao.findBookByTitle("testBook") == null) {
            this.mvc.perform(post("/books/create").with(csrf())
                            .session((MockHttpSession) httpSession)
                            .param("title", "testBook"))
                    .andExpect(status().is3xxRedirection());
        }

        Book bookToDelete = bookDao.findBookByTitle("testBook");

        this.mvc.perform(get("/books/" + bookToDelete.getId() + "/delete").with(csrf())
                        .session((MockHttpSession) httpSession)
                        .param("id", String.valueOf(bookToDelete.getId())))
                .andExpect(status().is3xxRedirection());
    }

}
