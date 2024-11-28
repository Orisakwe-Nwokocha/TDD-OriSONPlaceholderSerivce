package utils;

import dev.orisha.orison.post.Post;
import org.jetbrains.annotations.NotNull;

public class TestUtil {

    public static @NotNull String getJson(Post post) {
        return STR."""
             {
                  "id": \{post.id()},
                  "userId": \{post.userId()},
                  "title": "\{post.title()}",
                  "body": "\{post.body()}",
                  "version": \{post.version()}
             }
        """;
    }

}
