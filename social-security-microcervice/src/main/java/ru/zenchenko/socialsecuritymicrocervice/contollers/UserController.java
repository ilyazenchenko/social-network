package ru.zenchenko.socialsecuritymicrocervice.contollers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.zenchenko.socialsecuritymicrocervice.models.Post;
import ru.zenchenko.socialsecuritymicrocervice.models.User;
import ru.zenchenko.socialsecuritymicrocervice.security.MyUserDetails;
import ru.zenchenko.socialsecuritymicrocervice.services.UserService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final RestTemplate restTemplate;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/hello")
    public String hello(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        User user = myUserDetails.getUser();
        return "redirect:/" + user.getId();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, User>> accountPage(@PathVariable int id) {
        return ResponseEntity.ok(Map.of("user", userService.findById(id)));
    }

    @GetMapping("/news")
    public ResponseEntity<List<Post>> newsPage(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        User authenticatedUser = myUserDetails.getUser();
        User user = userService.findById(authenticatedUser.getId());
        return ResponseEntity.ok(user.getAllSubToPosts());
    }



    @GetMapping("/search")
    public ResponseEntity<List<User>> searchPage(@RequestParam(name = "query", required = false) String query) {
        if (query == null || query.isEmpty()) {
            return null;
        }
        ResponseEntity<List> resultObj =
                restTemplate.getForEntity("http://localhost:8081/search/" + query,
                List.class);
        List<User> resultlist = mapToUserList(Objects.requireNonNull(resultObj.getBody()));
        return ResponseEntity.ok(resultlist);
    }

    @GetMapping("/sub_to_ids")
    public List<Integer> getSubToIds(@AuthenticationPrincipal MyUserDetails myUserDetails){
        User user = userService.findById(myUserDetails.getUser().getId());
        return user.getSubscribedTo().stream().map(User::getId).collect(Collectors.toList());
    }

    @GetMapping("/admin")
    public ResponseEntity<List<User>> getAllUsers(){
        ResponseEntity<List> resultObj =
                restTemplate.getForEntity("http://localhost:8081/all",
                        List.class);
        List<User> resultList = mapToUserList(Objects.requireNonNull(resultObj.getBody()));
        return ResponseEntity.ok(resultList);
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
        user.setCity((String) map.get("city"));
        user.setBirthDate((String) map.get("birthDate"));
        user.setRole((String) map.get("role"));
        user.setUsername((String) map.get("username"));
        return user;
    }
}
