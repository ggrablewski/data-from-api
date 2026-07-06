package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Post;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PostFileWriterTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void writesPostToExpectedFileName(@TempDir Path outputDir) throws Exception {
        PostFileWriter writer = new PostFileWriter(objectMapper, outputDir);
        Post post = new Post(7, 1, "hello", "world");

        Path written = writer.write(post);

        assertEquals("post_007.json", written.getFileName().toString());
        assertTrue(Files.exists(written));

        Post roundTripped = objectMapper.readValue(written.toFile(), Post.class);
        assertEquals(post, roundTripped);
    }

    @Test
    void fetchOutputDirectoryExistsCreatesMissingDirectory(@TempDir Path outputDir) {
        Path path = outputDir.resolve("posts_java");
        PostFileWriter writer = new PostFileWriter(objectMapper, path);

        writer.fetchOutputDirectory();

        assertTrue(Files.isDirectory(path));
    }
}
