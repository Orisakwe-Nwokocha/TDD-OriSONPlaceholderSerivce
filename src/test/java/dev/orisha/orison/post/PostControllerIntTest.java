package dev.orisha.orison.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.*;

@Testcontainers
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
public class PostControllerIntTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    TestRestTemplate restTemplate;


    @Test
    void connectionEstablishedTest() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldFindAllPostsTest() {
        Post[] posts = restTemplate.getForObject("/api/posts", Post[].class);
        assertThat(posts.length).isGreaterThanOrEqualTo(99);
    }

    @Test
    void shouldFindPostWhenGivenValidPostIDTest() {
        ResponseEntity<Post> response = restTemplate.exchange("/api/posts/{id}", GET, null, Post.class, 1);
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void shouldThrowNotFoundExceptionWhenGivenInvalidPostIDTest() {
        ResponseEntity<Post> response = restTemplate.exchange("/api/posts/999", GET, null, Post.class);
        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    void shouldCreateNewPostWhenPostIsValidTest() {
        Post post = new Post(101,1,"101 Title","101 Body",null);

        ResponseEntity<Post> response = restTemplate.postForEntity("/api/posts", new HttpEntity<>(post), Post.class);
        assertThat(response.getStatusCode()).isEqualTo(CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(Objects.requireNonNull(response.getBody()).id()).isEqualTo(101);
        assertThat(response.getBody().userId()).isEqualTo(1);
        assertThat(response.getBody().title()).isEqualTo("101 Title");
        assertThat(response.getBody().body()).isEqualTo("101 Body");
    }

    @Test
    void shouldNotCreateNewPostWhenValidationFailsTest() {
        Post post = new Post(101,1,"","",null);
        ResponseEntity<Post> response = restTemplate.postForEntity("/api/posts", new HttpEntity<>(post), Post.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void shouldUpdatePostWhenPostIsValidTest() {
        ResponseEntity<Post> response = restTemplate.exchange("/api/posts/{id}", GET, null, Post.class, 99);
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        Post existing = response.getBody();

        Post updated = new Post(existing.id(), existing.userId(), "NEW POST TITLE #1", "NEW POST BODY #1", existing.version());
        response = restTemplate.exchange("/api/posts/{id}", PUT, new HttpEntity<>(updated), Post.class, 99);
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();

        updated = response.getBody();

        assertThat(updated.id()).isEqualTo(99);
        assertThat(updated.userId()).isEqualTo(10);
        assertThat(updated.title()).isEqualTo("NEW POST TITLE #1");
        assertThat(updated.body()).isEqualTo("NEW POST BODY #1");
    }

    @Test
    void shouldDeleteWithValidIDTest() {
        ResponseEntity<Void> response = restTemplate.exchange("/api/posts/{id}", DELETE, null, Void.class, 88);
        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
    }

}
