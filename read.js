import { mkdir, writeFile } from "fs/promises";
import path from "path";

const posts = [];
const filenames = [];

await mkdir("./data-from-api/posts_js", { recursive: true });

fetch('https://jsonplaceholder.typicode.com/posts/1')
      .then(response => response.json())
      .then(json => console.log('Post id=1 using fetch:', json))

fetch('https://jsonplaceholder.typicode.com/posts')
      .then(response => response.json())
      .then(data => {
        data.forEach(post => posts.push(post));
        console.log('All posts using fetch:');
        posts.forEach(post => {
            filenames[post.id] = path.join(
                "data-from-api",
                "posts_js",
                `post_${post.id}.json`
            );
            console.log(`Filename: <${filenames[post.id]}>\n`, post);
            writeFile(filenames[post.id], JSON.stringify(post))
            .then(() => {
                    console.log(`File <${filenames[post.id]}> saved successfully.`);
                })
            .catch(err => {
                console.error(err);
            });
        });
    });
