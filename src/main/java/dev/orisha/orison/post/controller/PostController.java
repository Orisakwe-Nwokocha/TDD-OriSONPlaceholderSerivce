package dev.orisha.orison.post.controller;

import dev.orisha.orison.post.data.model.Post;
import dev.orisha.orison.post.exception.ResourceNotFoundException;
import dev.orisha.orison.post.data.repository.PostRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("")
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @GetMapping("/{id}")
    public Post findById(@PathVariable Integer id) {
        return postRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public Post create(@RequestBody @Valid Post post) {
        return postRepository.save(post);
    }

    @PutMapping("/{id}")
    public Post update(@PathVariable Integer id, @RequestBody @Valid Post post) {
        Post existingPost = findById(id);
        Post updatedPost = new Post(
                existingPost.id(),
                existingPost.userId(),
                post.title(),
                post.body(),
                existingPost.version()
        );
        return postRepository.save(updatedPost);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id) {
        postRepository.deleteById(id);
    }

}
