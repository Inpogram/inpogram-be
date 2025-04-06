package com.haibui.inpogram.models.repositories;

import com.haibui.inpogram.models.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAll();

    Post findOneBySlug(String slug);
}
