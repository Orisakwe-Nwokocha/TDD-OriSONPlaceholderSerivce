package dev.orisha.orison.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.TestUtil.getJson;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PostRepository postRepository;

    List<Post> posts = new ArrayList<>();

    @BeforeEach
    void setUp() {
        posts = List.of(
                new Post(1, 1, "Hello, World!", "This is my first post.", null),
                new Post(2, 1, "Second Post", "This is my second post.", null)
        );
    }

    @Test
    void shouldFindAllPostsTest() throws Exception {
        String jsonResponse = """
                [
                    {
                      "id": 1,
                      "userId": 1,
                      "title": "Hello, World!",
                      "body": "This is my first post.",
                      "version": null
                    },
                    {
                      "id": 2,
                      "userId": 1,
                      "title": "Second Post",
                      "body": "This is my second post.",
                      "version": null
                    }
                ]
                """;

        when(postRepository.findAll()).thenReturn(posts);

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));
    }

    @Test
    void shouldFindPostWhenGivenValidIdTest() throws Exception {
        when(postRepository.findById(1)).thenReturn(Optional.of(posts.getFirst()));

        var json = getJson(posts.getFirst());

        mockMvc.perform(get("/api/posts/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(json))
                .andDo(print());
    }



    @Test
    void shouldNotFindPostWhenGivenInvalidIdTest() throws Exception {
        when(postRepository.findById(999)).thenThrow(PostNotFoundException.class);
        when(postRepository.findById(1)).thenReturn(Optional.of(posts.getFirst()));

        mockMvc.perform(get("/api/posts/{id}", 999))
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    void shouldCreateNewPostWhenPostIsValidTest() throws Exception {
        var post = new Post(3, 1, "NEW TITLE", "NEW BODY", null);
        when(postRepository.save(post)).thenReturn(post);

        var json = getJson(post);

        mockMvc.perform(post("/api/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().json(getJson(post)))
                .andDo(print());

    }

    @Test
    void shouldNotCreateNewPostWhenPostIsInvalidTest() throws Exception {
        var post = new Post(3, 1, "", "", null);
        when(postRepository.save(post)).thenReturn(post);

        mockMvc.perform(post("/api/posts")
                        .contentType(APPLICATION_JSON)
                        .content(getJson(post)))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    @Test
    void shouldUpdatePostWhenGivenValidIdTest() throws Exception {
        Post updatedPost = new Post(1, 1, "UPDATED TITLE", "UPDATED BODY", 1);
        when(postRepository.findById(1)).thenReturn(Optional.of(updatedPost));
        when(postRepository.save(updatedPost)).thenReturn(updatedPost);

        var json = getJson(updatedPost);
        mockMvc.perform(put("/api/posts/{id}", 1)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().json(json))
                .andDo(print());
    }

    @Test
    void shouldDeletePostWhenGivenValidIdTest() throws Exception {

        doNothing().when(postRepository).deleteById(1);

        mockMvc.perform(delete("/api/posts/{id}", 1))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(postRepository, times(1)).deleteById(1);
    }


}
