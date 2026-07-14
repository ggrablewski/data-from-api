import {mkdir, writeFile} from 'fs/promises';
import path from 'path';

const posts = [];
const filenames = [];

const currentPath = path.basename(process.cwd());

function setBaseDir(currentPath) {
    switch (currentPath) {
        case "javascript":
            return "posts_js";
        case "data-from-api":
            return path.join("javascript", "posts_js");
        default:
            return path.join("data-from-api", "javascript", "posts_js");
    }
}

const baseDir = setBaseDir(currentPath);

await mkdir(baseDir, {recursive: true});

await fetch('https://jsonplaceholder.typicode.com/posts')
    .then(response => response.json())
    .then(data => {
        data.forEach(post => posts.push(post));
        console.log('All posts using fetch:', posts);
    });

posts.forEach(post => {
    filenames[post.id] = path.join(
        baseDir,
        `post_${String(post.id).padStart(3, "0")}.json`
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

