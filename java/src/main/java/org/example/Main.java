package org.example;

import org.example.model.Post;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpClient;
import java.nio.file.Path;
import java.time.Duration;
import java.util.logging.Logger;
import java.util.List;

/**
 * Application entry point
 */
public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    private static final String POSTS_ENDPOINT = "https://jsonplaceholder.typicode.com/posts";
    private static final String OUTPUT_DIRECTORY_NAME = getBaseDir();
    
    public static void main(String[] args) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();
        ObjectMapper objectMapper = new ObjectMapper();

        PostApiClient apiClient = new PostApiClient(httpClient, objectMapper, POSTS_ENDPOINT);
        PostFileWriter fileWriter = new PostFileWriter(objectMapper, Path.of(OUTPUT_DIRECTORY_NAME));
        PostDownloadService downloadService = new PostDownloadService(apiClient);

        try {
            List<Post> posts = downloadService.downloadAllPosts();
            fileWriter.writeAllPosts(posts);

            LOG.info(() -> String.format("Completed. %d posts saved to %s.", posts.size(), OUTPUT_DIRECTORY_NAME));
        } catch (PostFetchException e) {
            LOG.severe("Failed to download posts: " + e.getMessage());
            System.exit(1);
        } catch (PostStorageException e) {
            LOG.severe("Failed to save posts: " + e.getMessage());
            System.exit(2);
        }
    }

    private static String getBaseDir() {
        String currentPath = System.getProperty("user.dir");
        String[] pathArray = currentPath.split("\\\\");
        String userDir = pathArray[pathArray.length-1];
        switch (userDir) {
            case "java":
                return "posts_java";
            case "data-from-api":
                return Path.of("java", "posts_java").toString();
            default:
                return Path.of("data-from-api", "java", "posts_java").toString();
            }
    }

}