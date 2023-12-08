package ru.zenchenko.socialsecuritymicrocervice.contollers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.zenchenko.socialsecuritymicrocervice.models.Post;
import ru.zenchenko.socialsecuritymicrocervice.security.MyUserDetails;

@Controller
@RequiredArgsConstructor
@RequestMapping("{id}/posts")
public class PostController {
    private final RestTemplate restTemplate;

    @PostMapping
    public String createPost(@PathVariable int id,
                             @ModelAttribute("newPost") Post post,
                             Model model) {
        restTemplate.postForObject("http://localhost:8081/" + id + "/posts",
                post, String.class);
        System.out.println("sent creating post:" + post);
        return "redirect:/" + id;
    }

    @DeleteMapping("/{postId}")
    public String deletePost(@PathVariable int id, @PathVariable("postId") int postId) {
        restTemplate.delete("http://localhost:8081/" + id + "/posts/" + postId);
        return "redirect:/" + id;
    }
}
