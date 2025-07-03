package dev.orisha.orison.post.data.repository;

import dev.orisha.orison.post.data.model.User;
import org.springframework.data.repository.ListCrudRepository;

public interface UserRepository extends ListCrudRepository<User, Long> {
}
