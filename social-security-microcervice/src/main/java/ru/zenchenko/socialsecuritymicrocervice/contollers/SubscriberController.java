package ru.zenchenko.socialsecuritymicrocervice.contollers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.zenchenko.socialsecuritymicrocervice.models.User;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/{sub}/subscribe")
@RequiredArgsConstructor
public class SubscriberController {

    private final RestTemplate restTemplate;

    @PostMapping("/{sub_to}")
    public String subscribe(@PathVariable int sub, @PathVariable int sub_to) {
        Map response = restTemplate.postForObject("http://localhost:8081/" + sub + "/subscribe/" + sub_to,
                null, Map.class);
        System.out.println(response);
        return "redirect:/"+sub_to;
    }

    @DeleteMapping("/{sub_to}")
    public String unsubscribe(@PathVariable int sub, @PathVariable int sub_to) {
        restTemplate.delete("http://localhost:8081/" + sub + "/subscribe/" + sub_to);
        System.out.println("sub deleted");
        return "redirect:/"+sub_to;
    }

}