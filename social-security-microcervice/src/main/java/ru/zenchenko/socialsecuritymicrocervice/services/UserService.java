package ru.zenchenko.socialsecuritymicrocervice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.zenchenko.socialsecuritymicrocervice.models.Post;
import ru.zenchenko.socialsecuritymicrocervice.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RestTemplate restTemplate;

    public User findById(int id) {
        User user = getUserFromBackendService(id);

        if (user == null) return null;

        setSubs(id, user);

        System.out.println("UserService: return from findById: " +
                user);
        return user;
    }

    private User getUserFromBackendService(int id) {
        HashMap<String, String> uriVariables = new HashMap<>();
        uriVariables.put("id", String.valueOf(id));
        ResponseEntity<User> responseEntity = restTemplate.getForEntity
                ("http://backend:8081/{id}",
                        User.class, uriVariables);
        User user = responseEntity.getBody();
        System.out.println("UserService: getUserFromBackendService: retrieved user: " + responseEntity.getBody());
        return user;
    }

    private void setSubs(int id, User user) {
        setSubscribers(id, user);
        setSubscribedTo(id, user);
    }

    private void setSubscribedTo(int id, User user) {
        ResponseEntity<List> subToListEntity =
                restTemplate.getForEntity("http://backend:8081/" + id + "/subscribe/sub_to", List.class);
        System.out.printf("UserService: setSubscribedTo (id=%d): retrieved subscribed to: "
                + subToListEntity.getBody() + "%n", id);
        List<Map> subsToListMap = subToListEntity.getBody();
        List<User> subsToList = new ArrayList<>();

        for (Map map : subsToListMap) {
            User u = mapToUser(map);
            subsToList.add(u);
        }
        System.out.printf("UserService: setSubscribedTo (id=%d): setting subscribed to: "
                + subsToList + "%n", id);
        user.setSubscribedTo(subsToList);
    }

    private void setSubscribers(int id, User user) {
        ResponseEntity<List> subsListEntity =
                restTemplate.getForEntity("http://backend:8081/" + id + "/subscribe/subs", List.class);
        System.out.printf("UserService: setSubscribers (id=%d): retrieved subscribers: " + subsListEntity.getBody() + "%n", id);
        List<Map> subsListMap = subsListEntity.getBody();

        List<User> subsList = new ArrayList<>();

        for (Map map : subsListMap) {
            User u = mapToUser(map);
            subsList.add(u);
        }
        System.out.printf("UserService: setSubscribers (id=%d): setting subscribed to: "
                + subsList + "%n", id);

        user.setSubscribers(subsList);
    }

    private User mapToUser(Map map) {
        User user = new User();
        user.setId((Integer) map.get("id"));
        user.setName((String) map.get("name"));
        user.setSurname((String) map.get("surname"));
        List postsObj = (List) map.get("posts");
        List<Post> posts = new ArrayList<>();
        for (Object post : postsObj) {
            posts.add(mapToPost((Map)post, user)); // П
        }
        user.setPosts(posts);
        return user;
    }

    private Post mapToPost(Map postMap, User user) {
        Post post = new Post();
        post.setUser(new User(user.getId(), user.getName(), user.getSurname()));
        post.setId((Integer) postMap.get("id"));
        String dateStr = (String)postMap.get("time");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH);
        try {
            Date date = dateFormat.parse(dateStr);
            post.setTime(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        post.setText((String) postMap.get("text"));
        return post;
    }


}
