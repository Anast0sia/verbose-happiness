package ru.netology.repository;

import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {
    private final List<Post> posts = Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong count = new AtomicLong(1);

    public List<Post> all() {
        return new ArrayList<>(posts);
    }

    public Optional<Post> getById(long id) {
        synchronized (posts) {
            return posts.stream()
                    .filter(post -> post.getId() == id)
                    .findFirst();
        }
    }

    public Post save(Post post) {
        synchronized (posts) {
            if (post.getId() == 0) {
                long newId = count.getAndIncrement();
                Post newPost = new Post(newId, post.getContent());
                posts.add(newPost);
                return newPost;
            } else {
                Optional<Post> existingPost = getById(post.getId());
                if (existingPost.isPresent()) {
                    posts.remove(existingPost.get());
                    posts.add(post);
                    return post;
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    public void removeById(long id) {
        synchronized (posts) {
            posts.removeIf(post -> post.getId() == id);
        }
    }
}