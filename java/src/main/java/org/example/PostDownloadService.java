package org.example;

import org.example.model.Post;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Coordinates fetching posts from the API.
 */
public class PostDownloadService {

    private static final Logger LOG = Logger.getLogger(PostDownloadService.class.getName());

    private final PostApiClient apiClient;

    public PostDownloadService(PostApiClient apiClient) {
        this.apiClient = Objects.requireNonNull(apiClient, "apiClient is missing");
    }

    /**
     * Fetches every post and writes each one to its own JSON file.
     *
     * @return the list of posts successfully downloaded
     */
    public List<Post> downloadAllPosts() {
        List<Post> posts = apiClient.fetchAllPosts();
        LOG.info(() -> "Fetched " + posts.size() + " posts from the API.");
        return posts;
    }
}
