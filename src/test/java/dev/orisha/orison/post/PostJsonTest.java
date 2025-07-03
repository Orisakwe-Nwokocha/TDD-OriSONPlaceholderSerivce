package dev.orisha.orison.post;

import dev.orisha.orison.post.data.model.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class PostJsonTest {

    @Autowired
    private JacksonTester<Post> jacksonTester;

    public static void main(String[] args) {
//        String regex = "([a-z0-9]+\\.)?[a-z0-9]+@\\S+(\\.[a-z]{2,})$";
        String regex = "([a-z0-9]+\\.)?[a-z0-9]+@[a-z0-9.-]+(\\.[a-z]{2,})";


//        String email = "o.nwokocha@native.semicolon.africa";
        String email = "c9.hg@example.comabca";
//        String email = "user123@subdomain.domain.com";
        boolean isValidEmail = email.matches(regex);
        Matcher matcher = Pattern.compile(regex).matcher(email);
        System.out.println(STR."isValidEmail: \{isValidEmail}");
        System.out.println(STR."matcher isValidEmail: \{matcher.find()}");

    }


    @Test
    void shouldSerializePostTest() throws Exception {
        Post post = new Post(1,1,"Hello, World!", "This is my first post.",null);
        String expected = STR."""
                {
                    "id":\{post.id()},
                    "userId":\{post.userId()},
                    "title":"\{post.title()}",
                    "body":"\{post.body()}",
                    "version": null
                }
                """;
        assertThat(jacksonTester.write(post)).isEqualToJson(expected);
    }

    @Test
    void shouldDeserializePostTest() throws Exception {
        Post post = new Post(1,1,"Hello, World!", "This is my first post.",null);
        String content = STR."""
                {
                    "id":\{post.id()},
                    "userId":\{post.userId()},
                    "title":"\{post.title()}",
                    "body":"\{post.body()}",
                    "version": null
                }
                """;

        assertThat(jacksonTester.parse(content).getObject()).isEqualTo(post);
        assertThat(jacksonTester.parseObject(content).id()).isEqualTo(1);
        assertThat(jacksonTester.parseObject(content).userId()).isEqualTo(1);
        assertThat(jacksonTester.parseObject(content).title()).isEqualTo("Hello, World!");
        assertThat(jacksonTester.parseObject(content).body()).isEqualTo("This is my first post.");
        assertThat(jacksonTester.parseObject(content).version()).isNull();
    }


}
