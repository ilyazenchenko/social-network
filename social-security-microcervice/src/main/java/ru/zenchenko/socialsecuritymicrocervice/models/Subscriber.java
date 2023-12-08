package ru.zenchenko.socialsecuritymicrocervice.models;


import lombok.Data;

@Data
public class Subscriber {

    private int id;

    private User subscriber;

    private User subscribed_to;
}