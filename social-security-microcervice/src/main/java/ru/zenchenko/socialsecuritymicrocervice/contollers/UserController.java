package ru.zenchenko.socialsecuritymicrocervice.contollers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import ru.zenchenko.socialsecuritymicrocervice.models.Post;
import ru.zenchenko.socialsecuritymicrocervice.models.User;
import ru.zenchenko.socialsecuritymicrocervice.security.MyUserDetails;
import ru.zenchenko.socialsecuritymicrocervice.services.UserService;

import java.util.*;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final RestTemplate restTemplate;

    @GetMapping("/hello")
    public String hello(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        User user = myUserDetails.getUser();
        return "redirect:/" + user.getId();
    }

    @GetMapping("/{id}")
    public String accountPage(@PathVariable int id, Model model, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        User user = userService.findById(id);
        User authenticatedUser = myUserDetails.getUser();
        if (user != null) {
            System.out.println("UserController: accountPage: current user:" + user);
            model.addAttribute("newPost", new Post());
            model.addAttribute("user", user);
            model.addAttribute("authenticatedUser", authenticatedUser);
            return "user/account";
        } else {
            model.addAttribute("error", "Ошибка: пользователь не найден");
            return "redirect:/error";
        }
    }

    @GetMapping("/news")
    public String newsPage(Model model, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        User authenticatedUser = myUserDetails.getUser();
        User user = userService.findById(authenticatedUser.getId());
        System.out.println("UserController: newsPage: current user:" + user);
        model.addAttribute("user", user);
        return "user/news";
    }

    @GetMapping("/search")
    public String searchPage(@RequestParam(name = "query", required = false) String query,
                             Model model,
                             @AuthenticationPrincipal MyUserDetails myUserDetails) {
        model.addAttribute("authenticatedUser", myUserDetails.getUser());
        if (query == null) {
            model.addAttribute("result", new ArrayList<User>());
            return "user/search";
        }
        ResponseEntity<List> resultObj =
                restTemplate.getForEntity("http://localhost:8081/search/" + query,
                List.class);
        List<User> result = mapToUserList(resultObj.getBody());
        model.addAttribute("result", result);
        return "user/search";
    }

    private List<User> mapToUserList(List resultObj) {
        List<User> result = new ArrayList<>();
        for (Object o : resultObj) {
            result.add(mapToUser((Map) o));
        }
        return result;
    }

    private User mapToUser(Map map) {
        User user = new User();
        user.setId((Integer) map.get("id"));
        user.setName((String) map.get("name"));
        user.setSurname((String) map.get("surname"));
        return user;
    }
}
