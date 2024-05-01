package ru.zenchenko.socialbackendmicrocervice.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zenchenko.socialbackendmicrocervice.model.Subscriber;
import ru.zenchenko.socialbackendmicrocervice.model.User;
import ru.zenchenko.socialbackendmicrocervice.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public User findById(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return null;
        return user;
    }

    @Transactional
    public void save(User user) {
        user.setRole("ROLE_USER");
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(new User());
    }

    public List<User> search(String query) {
        return userRepository.findByNameStartingWithIgnoreCaseOrSurnameStartingWithIgnoreCase(query, query);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
