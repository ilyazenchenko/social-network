package ru.zenchenko.socialbackendmicrocervice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "subscribers")
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int subscriber;

    private int subscribedTo;

    public Subscriber() {}

    public Subscriber(int subscriber, int subscribedTo) {
        this.subscriber = subscriber;
        this.subscribedTo = subscribedTo;
    }

}
