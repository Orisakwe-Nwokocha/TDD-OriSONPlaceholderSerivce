package dev.orisha.orison.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@Slf4j
public class PostDataLoader implements CommandLineRunner {

    private final ObjectMapper objectMapper;
    private final PostRepository postRepository;

    public PostDataLoader(ObjectMapper objectMapper, PostRepository postRepository) {
        this.objectMapper = objectMapper;
        this.postRepository = postRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (postRepository.count() == 0) {
            String POSTS_JSON = "/data/posts.json";
            log.info("Loading posts into the database fro JSON: {}", POSTS_JSON);
            try(InputStream inputStream = PostDataLoader.class.getResourceAsStream(POSTS_JSON)) {
                List<Post> posts = List.of(objectMapper.readValue(inputStream, Post[].class));
                postRepository.saveAll(posts);
            } catch (IOException e) {
                log.error("Failed to load posts: {}", e.getMessage());
                throw new RuntimeException("Failed to read JSON data", e);
            }
        }
    }
}
