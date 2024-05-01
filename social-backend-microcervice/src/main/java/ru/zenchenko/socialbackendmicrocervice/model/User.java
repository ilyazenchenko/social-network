package ru.zenchenko.socialbackendmicrocervice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Formula;

import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String password;

    private String name;

    private String surname;

    private String birthDate;

    private String role;

    @Column(name = "city")
    private String city;

    @OneToMany(mappedBy = "user")
    @OrderBy("time desc")
    private List<Post> posts;

    @Transient
    @JsonIgnore
    private List<User> subscribers;

    @Transient
    @JsonIgnore
    private List<User> subscribedTo;

    public User() {}

    public User(Integer id) {
        this.id = id;
    }
}
