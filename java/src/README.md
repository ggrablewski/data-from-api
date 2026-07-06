# data-from-api (Java)

Java project for retrieving data from API
- This project reads from [https://jsonplaceholder.typicode.com/](https://jsonplaceholder.typicode.com/posts) via API.
- The contents read consists of posts.
- Each post is saved in JSON format in a separate file with the name: <post_id>.json - in the respective folder: /posts_java.

## Requirements

- Java 21+
- Maven 3.9+

## Design

The code is split into small, single-responsibility pieces:

| Class                                         | Responsibility                                             |
|-----------------------------------------------|------------------------------------------------------------|
| `Main`                                        | Wiring + entry point                                       |
| `PostApiClient`                               | Reads posts from the REST API (JDK `HttpClient` + Jackson) |
| `PostFileWriter`                              | Writes one JSON file per post                              |
| `PostDownloadService`                         | Orchestrates fetch → write                                 |
| `Post`                                        | Immutable domain record                                    |
| `PostFetchException` / `PostStorageException` | Domain-specific exceptions                                 |

## Build & test

```bash
mvn clean verify
```

## Run

```bash
mvn clean package
java -jar target/data-from-api.jar
```

Output files are written to `posts_java/` directory.
