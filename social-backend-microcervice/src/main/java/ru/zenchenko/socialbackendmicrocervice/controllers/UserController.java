package ru.zenchenko.socialbackendmicrocervice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.zenchenko.socialbackendmicrocervice.model.User;
import ru.zenchenko.socialbackendmicrocervice.services.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public User findUserById(@PathVariable int id){
        return userService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> addUser(@RequestBody User user){
        userService.save(user);
        return Map.of("message", "created");
    }

    @GetMapping("/username/{username}")
    public User findByUsername(@PathVariable String username){
        return userService.findByUsername(username);
    }

    @GetMapping("/search/{query}")
    public List<User> searchByQuery(@PathVariable String query){
        return userService.search(query);
    }

    @GetMapping("/all")
    public List<User> findAll(){
        return userService.findAll();
    }
}
