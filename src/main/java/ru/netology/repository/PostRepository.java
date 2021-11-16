package ru.netology.repository;

import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PostRepository {
    public static volatile AtomicInteger count;
    private ConcurrentMap<Long, Post> collectionRequests;

    public PostRepository() {
        count = new AtomicInteger();
        collectionRequests = new ConcurrentHashMap<>();
    }

    public List<Post> all() {
        List<Post> listAll = new ArrayList<>();
        for (Post postR : collectionRequests.values()) {
            listAll.add(postR);
        }
        return listAll;
    }

    public Optional<Post> getById(long id) {
        if (collectionRequests.containsKey(id)) {
            return Optional.of(collectionRequests.get(id));
        } else
            return Optional.empty();
    }

    public Post save(Post post) {
        Long idReques = post.getId();
        if (idReques == 0) {
            count.getAndIncrement();
            long newId = count.get();
            while (collectionRequests.containsKey(newId)) {
                count.getAndIncrement();
                newId = count.get();
            }
            post.setId(newId);
            collectionRequests.put(newId, post);
        } else {
            if (collectionRequests.containsKey(idReques)) {
                collectionRequests.replace(idReques, post);
            } else {
                collectionRequests.put(idReques, post);
            }
        }
        return post;
    }

    public void removeById(long id) {
        if (collectionRequests.containsKey(id)) {
            collectionRequests.remove(id);
            count.getAndDecrement();
        }
    }
}

