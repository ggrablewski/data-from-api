package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Post;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

/**
 * Reads posts from a REST API that returns JSON.
 */
public class PostApiClient {

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(30);

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final URI postsEndpoint;

    public PostApiClient(HttpClient httpClient, ObjectMapper objectMapper, String postsEndpoint) {
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient is missing");
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper is missing");
        this.postsEndpoint = URI.create(Objects.requireNonNull(postsEndpoint, "postsEndpoint is missing"));
    }

    /**
     * Fetches all posts from the configured endpoint.
     *
     * @return the posts returned by the API (never {@code null})
     * @throws PostFetchException if the request fails or returns a non-2xx status
     */
    public List<Post> fetchAllPosts() {
        HttpRequest request = HttpRequest.newBuilder(postsEndpoint)
                .timeout(REQUEST_TIMEOUT)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = send(request);
        ensureSuccessful(response);
        return parsePosts(response.body());
    }

    private HttpResponse<String> send(HttpRequest request) {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new PostFetchException("Failed to reach " + postsEndpoint, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PostFetchException("Interrupted while fetching " + postsEndpoint, e);
        }
    }

    private void ensureSuccessful(HttpResponse<String> response) {
        int status = response.statusCode();
        if (status < 200 || status >= 300) {
            throw new PostFetchException(
                    "Unexpected HTTP status " + status + " from " + postsEndpoint);
        }
    }

    private List<Post> parsePosts(String body) {
        try {
            return objectMapper.readValue(body, new com.fasterxml.jackson.core.type.TypeReference<>() {
            });
        } catch (IOException e) {
            throw new PostFetchException("Failed to parse posts response", e);
        }
    }
}
