package ru.zenchenko.socialsecuritymicrocervice.contollers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.zenchenko.socialsecuritymicrocervice.models.Post;
import ru.zenchenko.socialsecuritymicrocervice.security.MyUserDetails;

import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("{id}/posts")
public class PostController {
    private final RestTemplate restTemplate;

    private final Logger logger = LoggerFactory.getLogger(PostController.class);

    @PostMapping
    public ResponseEntity createPost(@PathVariable int id,
                                     @RequestBody Post post) {
        Post response = restTemplate.postForObject("http://backend:8081/" + id + "/posts",
                post, Post.class);
        logger.info("sent creating post:" + post);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity deletePost(@PathVariable int id, @PathVariable("postId") int postId) {
        restTemplate.delete("http://backend:8081/" + id + "/posts/" + postId);
        return ResponseEntity.ok(Map.of("result", "deleted"));
    }
}
