package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

@Repository
public class PostRepositoryStubImpl implements PostRepository {
    private int count;
    private ConcurrentMap<Long, Post> collectionRequests;

    public List<Post> all() {
        return new ArrayList<>(collectionRequests.values());
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
            ++count;
            long newId = count;
            while (collectionRequests.containsKey(newId)) {
                ++count;
                newId = count;
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
            count--;
        }
    }
}
