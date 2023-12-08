package ru.zenchenko.socialsecuritymicrocervice.contollers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.zenchenko.socialsecuritymicrocervice.models.User;
import ru.zenchenko.socialsecuritymicrocervice.services.RegistrationService;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegistrationService registrationService;


    @GetMapping("/login")
    public String login() {
        return "/auth/login";
    }

    @GetMapping("/register")
    public String registerPage(@ModelAttribute("user") User user) {
        return "auth/register";
    }

    @PostMapping("/register")
    public String performRegister(@ModelAttribute("user") User user) {
        registrationService.register(user);
        return "redirect:/auth/login";
    }

}
