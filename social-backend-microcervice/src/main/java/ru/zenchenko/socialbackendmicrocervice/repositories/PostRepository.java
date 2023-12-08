package ru.zenchenko.socialbackendmicrocervice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.zenchenko.socialbackendmicrocervice.model.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByUserId(int id);

    @Query("SELECT MAX(p.id) FROM Post p")
    Integer findMaxId();
}
