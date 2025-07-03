package dev.orisha.orison.post.data.repository;

import dev.orisha.orison.post.data.model.Post;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface PostRepository extends ListCrudRepository<Post, Integer> {

    Optional<Post> findByTitle(String title);

}
