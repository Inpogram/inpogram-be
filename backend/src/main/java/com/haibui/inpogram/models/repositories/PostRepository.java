package com.haibui.inpogram.models.repositories;

import com.haibui.inpogram.models.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAll();

    Post findOneBySlug(String slug);
}
