package ru.zenchenko.socialsecuritymicrocervice.contollers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.zenchenko.socialsecuritymicrocervice.dto.LoginDto;
import ru.zenchenko.socialsecuritymicrocervice.models.User;
import ru.zenchenko.socialsecuritymicrocervice.security.MyUserDetails;
import ru.zenchenko.socialsecuritymicrocervice.services.MyUserDetailsService;
import ru.zenchenko.socialsecuritymicrocervice.services.RegistrationService;
import ru.zenchenko.socialsecuritymicrocervice.services.UserService;

import java.util.Map;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final RegistrationService registrationService;

    private final AuthenticationManager authenticationManager;

    private HttpServletRequest request;

    private final MyUserDetailsService myUserDetailsService;

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/login")
    public ResponseEntity login(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        if (myUserDetails != null){
            return ResponseEntity.ok("hello :-)");
        }
        return ResponseEntity.status(403).body(Map.of("message", "unauthenticated"));
    }

    @PostMapping("/register")
    public String performRegister(@RequestBody User user) {
        registrationService.register(user);
        return "redirect:/auth/login";
    }

    @PostMapping("/process_login")
    public ResponseEntity<?> processLogin(@RequestBody LoginDto loginDto, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        if (myUserDetails != null) {
            return ResponseEntity.ok(userService.findById(myUserDetails.getUser().getId()));
        }
        logger.info(loginDto.getLogin());
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

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        try {
            request.logout();
        } catch (ServletException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.ok(Map.of("logout", "success"));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getAuthenticatedUser(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        if (myUserDetails == null) {
            return ResponseEntity.status(403).body(Map.of("message", "unauthorized"));
        }
        return ResponseEntity.ok(userService.findById(myUserDetails.getUser().getId()));
    }
}
