package com.haibui.inpogram.controllers;

import com.haibui.inpogram.exceptions.EmptyFeaturedImageException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/posts")
@AllArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<?> getPosts(@RequestParam(required = false) Long cursor,
                                      @RequestParam(defaultValue = "10") int limit) {
        List<Post> posts = postService.getPosts(cursor, limit);
        Long nextCursor = posts.isEmpty() ? null : posts.get(posts.size() - 1).getId();

        Map<String, Object> response = new HashMap<>();
        response.put("posts", posts);
        response.put("nextCursor", nextCursor);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Post> getBySlug(@PathVariable String slug) {
        Post post = postService.getBySlug(slug);
        if (post == null) {
            throw new PostNotFoundException(String.format("Post with slug '%s' not found", slug));
        }
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@ModelAttribute @Valid PostRequest postRequest) throws Exception {
        if (postRequest.featuredImage().isEmpty()) {
            throw new EmptyFeaturedImageException("Featured image must not be empty");
        }
        Post createdPost = postService.createPost(postRequest);
        return ResponseEntity.ok(createdPost);
    }
}
