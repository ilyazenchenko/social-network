package ru.zenchenko.socialbackendmicrocervice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.zenchenko.socialbackendmicrocervice.model.User;
import ru.zenchenko.socialbackendmicrocervice.services.SubscriberService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/{sub}/subscribe")
@RequiredArgsConstructor
public class SubscriberController {

    private final SubscriberService subscriberService;

    @PostMapping("/{sub_to}")
    public Map<String, String> subscribe(@PathVariable int sub, @PathVariable int sub_to){
        subscriberService.save(sub, sub_to);
        return Map.of("sub", "confirmed");
    }

    @DeleteMapping("/{sub_to}")
    public Map<String, String> unsubscribe(@PathVariable int sub, @PathVariable int sub_to){
        subscriberService.delete(sub, sub_to);
        return Map.of("sub", "deleted");
    }

    @GetMapping("/subs")
    public List<User> getSubs(@PathVariable("sub") int userId){
        return subscriberService.findAllBySubscribedTo(userId);
    }

    @GetMapping("/sub_to")
    public List<User> getSubTo(@PathVariable("sub") int userId){
        return subscriberService.findAllBySubscriber(userId);
    }

}
