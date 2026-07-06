package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Post;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Saves posts to disk as JSON files, one file per post.
 */
public class PostFileWriter {

    private final ObjectMapper objectMapper;
    private final Path outputDirectory;

    public PostFileWriter(ObjectMapper objectMapper, Path outputDirectory) {
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper is missing");
        this.outputDirectory = Objects.requireNonNull(outputDirectory, "outputDirectory is missing");
    }

    /**
     * Creates the output directory if it does not already exist.
     *
     * @throws PostStorageException if the directory cannot be created
     */
    public void fetchOutputDirectory() {
        try {
            Files.createDirectories(outputDirectory);
        } catch (IOException e) {
            throw new PostStorageException("Failed to create directory " + outputDirectory, e);
        }
    }

    /**
     * Writes a single post to {@code post_<id>.json}.
     *
     * @param post the post to store
     * @return the path the post was written to
     * @throws PostStorageException if serialization or writing fails
     */
    public Path write(Post post) {
        Objects.requireNonNull(post, "post to be saved is missing");
        Path target = outputDirectory.resolve("post_" + String.format("%03d", post.id()) + ".json");
        try {
            byte[] json = objectMapper.writeValueAsBytes(post);
            Files.write(target, json);
            return target;
        } catch (JsonProcessingException e) {
            throw new PostStorageException("Failed to serialize post " + post.id(), e);
        } catch (IOException e) {
            throw new PostStorageException("Failed to write " + target, e);
        }
    }
}
