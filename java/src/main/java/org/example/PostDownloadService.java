package org.example;

import org.example.model.Post;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Coordinates fetching posts from the API and storing them on disk.
 */
public class PostDownloadService {

    private static final Logger LOG = Logger.getLogger(PostDownloadService.class.getName());

    private final PostApiClient apiClient;
    private final PostFileWriter fileWriter;

    public PostDownloadService(PostApiClient apiClient, PostFileWriter fileWriter) {
        this.apiClient = Objects.requireNonNull(apiClient, "apiClient is missing");
        this.fileWriter = Objects.requireNonNull(fileWriter, "fileWriter is missing");
    }

    /**
     * Fetches every post and writes each one to its own JSON file.
     *
     * @return the number of posts successfully written
     */
    public int downloadAllPosts() {
        fileWriter.fetchOutputDirectory();

        List<Post> posts = apiClient.fetchAllPosts();
        LOG.info(() -> "Fetched " + posts.size() + " posts from the API.");

        int written = 0;
        for (Post post : posts) {
            Path savedTo = fileWriter.write(post);
            LOG.info(() -> "Saved " + savedTo);
            written++;
        }
        return written;
    }
}
