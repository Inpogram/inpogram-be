package com.haibui.inpogram.controllers;

import com.haibui.inpogram.exceptions.EmptyBannerImageException;
import com.haibui.inpogram.exceptions.PostNotFoundException;
import com.haibui.inpogram.models.dtos.PostRequest;
import com.haibui.inpogram.models.entities.Post;
import com.haibui.inpogram.services.PostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@AllArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<Post> results = postService.findAll();
        return ResponseEntity.ok(results);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@ModelAttribute @Valid PostRequest postRequest) throws Exception {
        if (postRequest.bannerImage().isEmpty()) {
            throw new EmptyBannerImageException("Banner image must not be empty");
        }
        Post createdPost = postService.createPost(postRequest);
        return ResponseEntity.ok(createdPost);
    }

    @GetMapping("/{title}")
    public ResponseEntity<Post> findByTitle(@PathVariable String title) {
        Post post = postService.findByTitle(title);

        if (post == null) {
            throw new PostNotFoundException(String.format("Post with title '%s' not found", title));
        }

        return ResponseEntity.ok(post);
    }
}
