package ru.zenchenko.socialbackendmicrocervice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zenchenko.socialbackendmicrocervice.model.Subscriber;
import ru.zenchenko.socialbackendmicrocervice.model.User;
import java.util.List;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Integer> {

    void deleteBySubscriberAndSubscribedTo(int subscriber, int subscribedTo);

    List<Subscriber> findBySubscriber(int subscriber);
    List<Subscriber> findAllBySubscribedTo(int subscribedTo);
    List<Subscriber> findAllBySubscriber(int subscriber);

}
