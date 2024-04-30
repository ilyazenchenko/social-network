package ru.zenchenko.socialsecuritymicrocervice.contollers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.zenchenko.socialsecuritymicrocervice.dto.LoginDto;
import ru.zenchenko.socialsecuritymicrocervice.models.User;
import ru.zenchenko.socialsecuritymicrocervice.security.MyUserDetails;
import ru.zenchenko.socialsecuritymicrocervice.services.MyUserDetailsService;
import ru.zenchenko.socialsecuritymicrocervice.services.RegistrationService;
import ru.zenchenko.socialsecuritymicrocervice.services.UserService;

import java.util.Optional;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegistrationService registrationService;

    private final AuthenticationManager authenticationManager;

    private final HttpServletRequest request;

    private final MyUserDetailsService myUserDetailsService;

    private final UserService userService;

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

    @PostMapping("/process_login")
    public ResponseEntity<?> processLogin(@RequestBody LoginDto loginDto) {
        // Ваш код для проверки логина и пароля
        boolean isAuthenticated = authenticate(loginDto.getLogin(), loginDto.getPassword());
        if (isAuthenticated) {
            User user = ((MyUserDetails) myUserDetailsService.loadUserByUsername(loginDto.getLogin())).getUser();
            return ResponseEntity.ok(userService.findById(user.getId()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверные учетные данные");
        }
    }

    private boolean authenticate(String login, String password) {
        try {
            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = new UsernamePasswordAuthenticationToken(login, password);
            authentication = authenticationManager.authenticate(authentication);
            if (authentication.isAuthenticated()){
                context.setAuthentication(authentication);
                request.getSession(true).setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
                return true;
            };
        } catch (AuthenticationException e) {
            return false; // В случае ошибки аутентификации возвращаем false
        }
        return false;
    }

}
