package ru.zenchenko.socialbackendmicrocervice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.zenchenko.socialbackendmicrocervice.model.Post;
import ru.zenchenko.socialbackendmicrocervice.model.User;
import ru.zenchenko.socialbackendmicrocervice.services.PostService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("{id}/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Post createPost(@PathVariable int id, @RequestBody Post post){
        return postService.save(post, id);
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable int postId) {
        postService.deleteById(postId);
    }

}
