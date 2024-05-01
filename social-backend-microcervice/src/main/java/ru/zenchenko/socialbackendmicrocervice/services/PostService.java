package ru.zenchenko.socialbackendmicrocervice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zenchenko.socialbackendmicrocervice.model.Post;
import ru.zenchenko.socialbackendmicrocervice.model.User;
import ru.zenchenko.socialbackendmicrocervice.repositories.PostRepository;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void deleteById(int postId){
        postRepository.deleteById(postId);
    }

    public Post save(Post post, int userId) {
        post.setId(postRepository.findMaxId()+1);
        post.setUser(new User(userId));
        post.setTime(new Date());
        postRepository.save(post);
        return post;
    }
}
