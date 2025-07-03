package dev.orisha.orison.post.initializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class UserDataGenerator {
    
    private static final Faker faker = new Faker(Locale.of("en","US"));
    private static final SecureRandom random = new SecureRandom();
    private static final ObjectMapper mapper = new ObjectMapper();
    
    public static void main(String[] args) {
        List<User> users = generateUsers(1000);
        System.out.println("Generated " + users.size() + " users");
        System.out.println(users);
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("users.json"), users);
            System.out.println("Generated 1000 users in users.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static List<User> generateUsers(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            users.add(generateUser(i));
        }
        return users;
    }
    
    private static User generateUser(int id) {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String username = (firstName.charAt(0) + lastName).toLowerCase();
        
        return new User(
            id,
            firstName + " " + lastName,
            username,
            username + "@" + faker.internet().domainName(),
            generateAddress(),
            faker.phoneNumber().phoneNumber(),
            username.toLowerCase() + "." + randomDomain(),
            generateCompany()
        );
    }
    
    private static Address generateAddress() {
        return new Address(
            faker.address().streetName(),
            randomSuite(),
            faker.address().city(),
            faker.address().zipCode(),
            generateGeo()
        );
    }
    
    private static Geo generateGeo() {
        return new Geo(
            String.format(Locale.US, "%.4f", ThreadLocalRandom.current().nextDouble(-90, 90)),
            String.format(Locale.US, "%.4f", ThreadLocalRandom.current().nextDouble(-180, 180))
        );
    }
    
    private static Company generateCompany() {
        String[] companyTypes = {"LLC", "Inc", "Group", "and Sons", "Corp", "Industries"};
        String[] techWords = {"Tech", "Solutions", "Systems", "Networks", "Ventures", "Global"};
        String[] adjWords = {"Creative", "Innovative", "Advanced", "Digital", "Elite", "Prime"};
        
        String name = random.nextBoolean() 
            ? adjWords[random.nextInt(adjWords.length)] + " " + techWords[random.nextInt(techWords.length)] + " " + companyTypes[random.nextInt(companyTypes.length)]
            : adjWords[random.nextInt(adjWords.length)] + "-" + techWords[random.nextInt(techWords.length)];
        
        String[] catchPhrases = {
            "Multi-layered client-server neural-net",
            "Proactive didactic contingency",
            "Face to face bifurcated interface",
            "Multi-tiered zero tolerance productivity"
        };
        
        String[] bsPhrases = {
            "harness real-time e-markets",
            "synergize scalable supply-chains",
            "e-enable strategic applications",
            "transition cutting-edge web services"
        };
        
        return new Company(
            name,
            catchPhrases[random.nextInt(catchPhrases.length)],
            bsPhrases[random.nextInt(bsPhrases.length)]
        );
    }
    
    private static String randomSuite() {
        return random.nextBoolean() 
            ? "Apt. " + (100 + random.nextInt(900))
            : "Suite " + (100 + random.nextInt(900));
    }
    
    private static String randomDomain() {
        String[] domains = {"com", "net", "io", "org"};
        return domains[random.nextInt(domains.length)];
    }
    
    // Define your data classes
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class User {
        private int id;
        private String name;
        private String username;
        private String email;
        private Address address;
        private String phone;
        private String website;
        private Company company;
        

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class Address {
        private String street;
        private String suite;
        private String city;
        private String zipcode;
        private Geo geo;
        
        // Constructor, getters and setters
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class Geo {
        private String lat;
        private String lng;
        
        // Constructor, getters and setters
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class Company {
        private String name;
        private String catchPhrase;
        private String bs;
        
        // Constructor, getters and setters
    }
}