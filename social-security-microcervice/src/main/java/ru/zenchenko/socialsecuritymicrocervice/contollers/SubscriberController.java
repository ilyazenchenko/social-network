package ru.zenchenko.socialsecuritymicrocervice.contollers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.zenchenko.socialsecuritymicrocervice.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("{sub}/subs/")
@RequiredArgsConstructor
public class SubscriberController {

    private final RestTemplate restTemplate;

    private final Logger logger = LoggerFactory.getLogger(SubscriberController.class);

    @PostMapping("/{sub_to}")
    public ResponseEntity<Map> subscribe(@PathVariable int sub, @PathVariable("sub_to") int subTo) {
        Map response = restTemplate.postForObject("http://localhost:8081/" + sub + "/subscribe/" + subTo,
                null, Map.class);
        Map result = (HashMap) response;
        result.put("from", sub);
        result.put("to", subTo);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{sub_to}")
    public ResponseEntity unsubscribe(@PathVariable int sub, @PathVariable("sub_to") int subTo) {
        restTemplate.delete("http://localhost:8081/" + sub + "/subscribe/" + subTo);
        logger.info("sub deleted");
        return ResponseEntity.ok(Map.of(
                "sub", "deleted",
                "from", sub,
                "to", subTo
        ));
    }

}