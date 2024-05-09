package ru.zenchenko.socialsecuritymicrocervice.services;

//import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.zenchenko.socialsecuritymicrocervice.models.User;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        HashMap hashMap = restTemplate.postForObject("http://backend:8081",user, HashMap.class);
        System.out.println(hashMap);
    }

}
