package ru.zenchenko.socialbackendmicrocervice.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zenchenko.socialbackendmicrocervice.model.Subscriber;
import ru.zenchenko.socialbackendmicrocervice.model.User;
import ru.zenchenko.socialbackendmicrocervice.repositories.SubscriberRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;

    private final UserService userService;

    @Transactional
    public void save(int sub, int subTo) {
        subscriberRepository.save(new Subscriber((sub), (subTo)));
    }

    @Transactional
    public void delete(int sub, int subTo) {
        subscriberRepository.deleteBySubscriberAndSubscribedTo((sub), (subTo));
    }

    public List<User> findAllBySubscribedTo(int userId){
        List<Subscriber> subs = subscriberRepository.findAllBySubscribedTo(userId);
        List<User> subsUsers = new ArrayList<>();
        for (Subscriber sub : subs) {
            subsUsers.add(userService.findById(sub.getSubscriber()));
        }
        return subsUsers;
    }
    public List<User> findAllBySubscriber(int userId){
        List<Subscriber> subTo = subscriberRepository.findAllBySubscriber(userId);
        List<User> subsToUsers = new ArrayList<>();
        for (Subscriber sub : subTo) {
            subsToUsers.add(userService.findById(sub.getSubscribedTo()));
        }
        return subsToUsers;
    }

}
