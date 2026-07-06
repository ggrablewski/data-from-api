package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpClient;
import java.nio.file.Path;
import java.time.Duration;
import java.util.logging.Logger;

/**
 * Application entry point
 */
public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    private static final String POSTS_ENDPOINT = "https://jsonplaceholder.typicode.com/posts";
    private static final String OUTPUT_DIRECTORY_NAME = "posts_java";

    public static void main(String[] args) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();
        ObjectMapper objectMapper = new ObjectMapper();

        PostApiClient apiClient = new PostApiClient(httpClient, objectMapper, POSTS_ENDPOINT);
        PostFileWriter fileWriter = new PostFileWriter(objectMapper, Path.of(OUTPUT_DIRECTORY_NAME));
        PostDownloadService service = new PostDownloadService(apiClient, fileWriter);

        try {
            int written = service.downloadAllPosts();
            LOG.info(() -> String.format("Completed. %d posts saved to %s.", written, OUTPUT_DIRECTORY_NAME));
        } catch (PostFetchException | PostStorageException e) {
            LOG.severe("Failed to download posts: " + e.getMessage());
            System.exit(1);
        }
    }

}