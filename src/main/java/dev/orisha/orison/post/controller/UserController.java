package dev.orisha.orison.post.controller;

import dev.orisha.orison.post.exception.ResourceNotFoundException;
import dev.orisha.orison.post.data.model.User;
import dev.orisha.orison.post.data.repository.UserRepository;
import dev.orisha.orison.post.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final SecureRandom secureRandom = new SecureRandom();

    @GetMapping("")
    public Page<User> findAll(Pageable pageable) {
        boolean flag = secureRandom.nextBoolean();
        if (flag) {
            log.info("Finding all users with userRepository");
            return this.userRepository.findAll(pageable);
        } else {
            log.info("Finding all users with userService");
            return this.userService.findAll(pageable);
        }
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public User create(@RequestBody @Valid User user) {
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody @Valid User user) {
        User existingUser = findById(id);
        modelMapper.map(user, existingUser);
        existingUser.setId(id);
        return userRepository.save(existingUser);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

}
