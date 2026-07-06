package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import org.example.model.Post;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PostApiClientTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private HttpServer server;

    @BeforeEach
    void startServer() throws Exception {
        server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        server.start();
    }

    @AfterEach
    void stopServer() {
        server.stop(0);
    }

    private String endpoint() {
        return "http://localhost:" + server.getAddress().getPort() + "/posts";
    }

    private void respondWith(int status, String body) {
        server.createContext("/posts", exchange -> {
            byte[] payload = body.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(status, payload.length);
            try (var os = exchange.getResponseBody()) {
                os.write(payload);
            }
        });
    }

    @Test
    void fetchesAndParsesPosts() {
        respondWith(200, "[{\"id\":1,\"userId\":2,\"title\":\"test-title\",\"body\":\"test-body\"}]");
        PostApiClient client = new PostApiClient(HttpClient.newHttpClient(), objectMapper, endpoint());

        List<Post> posts = client.fetchAllPosts();

        assertEquals(1, posts.size());
        assertEquals(new Post(1, 2, "test-title", "test-body"), posts.getFirst());
    }

    @Test
    void throwsOnNonSuccessStatus() {
        respondWith(500, "no such service");
        PostApiClient client = new PostApiClient(HttpClient.newHttpClient(), objectMapper, endpoint());

        assertThrows(PostFetchException.class, client::fetchAllPosts);
    }
}
