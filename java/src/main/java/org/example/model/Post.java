package org.example.model;

//import lombok.Data;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A single post as returned by the JSONPlaceholder API.
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
public record Post(int id, int userId, String title, String body) {
}

//@Data
//public class Post {
//    private final int id;
//    private final int userId;
//    private final String title;
//    private final String body;
//}
