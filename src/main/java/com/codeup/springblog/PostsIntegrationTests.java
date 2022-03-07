package com.codeup.springblog;

import com.codeup.springblog.models.Post;
import com.codeup.springblog.models.User;
import com.codeup.springblog.repositories.PostRepository;
import com.codeup.springblog.repositories.UserRepository;
import org.hamcrest.Matchers;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.HttpSession;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringblogApplication.class)
@AutoConfigureMockMvc
public class PostsIntegrationTests {

    private User testUser;
    private HttpSession httpSession;

    @Autowired
    private MockMvc mvc;

    @Autowired
    UserRepository userDao;

    @Autowired
    PostRepository postsDao;

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

    @Test
    public void testIfUserSessonIsActive() {
        assertNotNull(httpSession);
    }

    @Test
    public void contextLoads() {
        // Sanity Test, just to make sure the MVC bean is working.
        assertNotNull(mvc);
    }

    @Test
    public void testPostsIndex() throws Exception {
        List<Post> posts = postsDao.findAll();
        this.mvc.perform(MockMvcRequestBuilders.get("/posts"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("All the posts")));
        for(Post post : posts) {
            this.mvc.perform(MockMvcRequestBuilders.get("/posts"))
                    .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(post.getTitle())));
        }
    }

    @Test
    public void testIndividualPostsPage() throws Exception {
        Post post = postsDao.findAll().get(0);
        this.mvc.perform(MockMvcRequestBuilders.get("/posts/" + post.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(post.getTitle())));
    }

    @Test
    public void testPostCreation() throws Exception {
        //        Testing without user login
        this.mvc.perform(MockMvcRequestBuilders
                        .post("/posts/create")
                        .param("title", "Testing Post Functionality")
                        .param("body", "Testing Post Functionality"))
                .andExpect(status().isForbidden());
        //        Testing with user login
        this.mvc.perform(MockMvcRequestBuilders
                        .post("/posts/create")
                        .with(csrf())
                        .session((MockHttpSession) httpSession)
                        .param("title", "Testing Post Functionality")
                        .param("body", "Testing Post Functionality"))
                .andExpect(status().is3xxRedirection());
    }
}
//
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = SpringblogApplication.class)
//@AutoConfigureMockMvc
//public class PostsIntegrationTests {
//
//    @RunWith(SpringRunner.class)
//    @SpringBootTest(classes = SpringblogApplication.class)
//    @AutoConfigureMockMvc
//    public class AdsIntegrationTests {
//
//        private User testUser;
//        private HttpSession httpSession;
//
//        @Autowired
//        private MockMvc mvc;
//
//        @Autowired
//        UserRepository userDao;
//
//        @Autowired
//        PostRepository postDao;
//
//        @Autowired
//        private PasswordEncoder passwordEncoder;
//
//        @Before
//        public void setup() throws Exception {
//
//            testUser = userDao.findByUsername("testUser");
//
//            // Creates the test user if not exists
//            if(testUser == null){
//                User newUser = new User();
//                newUser.setUsername("testUser");
//                newUser.setPassword(passwordEncoder.encode("pass"));
//                newUser.setEmail("testUser@codeup.com");
//                testUser = userDao.save(newUser);
//            }
//
//            // Throws a Post request to /login and expect a redirection to the Ads index page after being logged in
//            httpSession = this.mvc.perform(post("/login").with(csrf())
//                            .param("username", "testUser")
//                            .param("password", "pass"))
//                    .andExpect(status().is(HttpStatus.FOUND.value()))
//                    .andExpect(redirectedUrl("/ads"))
//                    .andReturn()
//                    .getRequest()
//                    .getSession();
//        }
//    }
//}
