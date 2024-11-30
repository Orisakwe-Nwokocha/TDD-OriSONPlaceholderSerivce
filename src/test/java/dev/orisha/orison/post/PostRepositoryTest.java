package dev.orisha.orison.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@Testcontainers
@DataJdbcTest
@AutoConfigureTestDatabase(replace = NONE)
class PostRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    PostRepository postRepository;


    @BeforeEach
    void setUp() {
        List<Post> posts = List.of(new Post(1, 1, "Hello, World!", "This is my first post", null));
        postRepository.saveAll(posts);
    }

    @Test
    void connectionEstablishedTest() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();

    }

    @Test
    void shouldReturnPostByTitleTest() {
        Post post = postRepository.findByTitle("Hello, World!").orElse(null);
        assertThat(post).isNotNull();
    }

    @Test
    void shouldNotReturnPostByWrongTitleTest() {
        Optional<Post> post = postRepository.findByTitle("Hello, Wrong Title!");
        assertFalse(post.isPresent(), "Post should not be present");
    }

}