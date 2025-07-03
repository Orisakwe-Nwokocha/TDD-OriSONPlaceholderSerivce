package dev.orisha.orison.post.initializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.orisha.orison.post.data.model.User;
import dev.orisha.orison.post.data.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
public class UserDataLoader implements ApplicationRunner {

    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    public UserDataLoader(ObjectMapper objectMapper, UserRepository userRepository) {
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public void run(ApplicationArguments args) {
        log.info("Initializing UserDataLoader...");

//        if (this.userRepository.count() == 0) {
//            String USERS_JSON = "data/users.json";
//            log.info("Loading users into database from JSON: {}", USERS_JSON);
//            try(InputStream inputStream = new ClassPathResource(USERS_JSON).getInputStream()) {
//                List<User> users = List.of(objectMapper.readValue(inputStream, User[].class));
//                this.userRepository.saveAll(users);
//            } catch (IOException e) {
//                log.error("Failed to load users: {}", e.getMessage());
//                throw new RuntimeException("Failed to read JSON data", e);
//            }
//        }

        if (this.userRepository.count() == 0) {
            List<UserDataGenerator.User> generateUsers = UserDataGenerator.generateUsers(10000);
            List<User> users = List.of(objectMapper.convertValue(generateUsers, User[].class));
            log.info("Loading {} users into database", users.size());

            // Batch size configuration (e.g., 500 per batch)
            int batchSize = 500;
            int totalBatches = (int) Math.ceil((double) users.size() / batchSize);

            for (int index = 0; index < totalBatches; index++) {
                int fromIndex = index * batchSize;
                int toIndex = Math.min(fromIndex + batchSize, users.size());
                List<User> batch = users.subList(fromIndex, toIndex);

                userRepository.saveAll(batch);
                log.info("Saved batch {} of {} (users {} to {})",
                        index + 1, totalBatches, fromIndex + 1, toIndex);
            }
        }

    }

}
