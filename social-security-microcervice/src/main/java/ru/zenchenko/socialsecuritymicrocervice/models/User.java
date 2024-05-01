package ru.zenchenko.socialsecuritymicrocervice.models;

//import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

@Data
public class User {

    private Integer id;

    private String username;

    private String password;

    private String name;

    private String surname;

    private String birthDate;

    private String city;

    private String role;

    private List<Post> posts;

    private List<User> subscribers = new ArrayList<>();

    private List<User> subscribedTo = new ArrayList<>();

    public User(int userId) {
        id = userId;
    }

    public User() {}

    public User(Integer id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public List<Integer> getSubscribersIds() {
        List<Integer> lst = new ArrayList<>();
        System.out.println("method getSubscribersIds: user id="+id+ " subs=" +subscribers);

        for (User subscriber : subscribers) {
            lst.add(subscriber.getId());
        }
        return lst;
    }

    public List<Post> getAllSubToPosts(){
        List<Post> allSubToPosts = new ArrayList<>();
        for (User user : subscribedTo) {
            allSubToPosts.addAll(user.getPosts());
        }
        Collections.sort(allSubToPosts, Collections.reverseOrder());
        System.out.println("User: getAllSubToPosts: sorted posts list: " +allSubToPosts);
        return allSubToPosts;
    }
}
