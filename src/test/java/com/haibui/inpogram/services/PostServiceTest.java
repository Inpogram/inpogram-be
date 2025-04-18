package com.haibui.inpogram.services;

import com.haibui.inpogram.models.entities.Post;
import com.haibui.inpogram.models.repositories.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepo;

    @InjectMocks
    private PostService postService;

    @Test
    void getBySlug_ReturnsPost_WhenPostExists() {
        // Arrange
        String slug = "test-slug";
        Post post = new Post();
        post.setSlug(slug);
        when(postRepo.findOneBySlug(slug)).thenReturn(post);

        // Act
        Post result = postService.getBySlug(slug);

        // Assert
        assertEquals(post, result);
        assertEquals(slug, result.getSlug());
    }

    @Test
    void getBySlug_ReturnsNull_WhenPostDoesNotExist() {
        // Arrange
        String slug = "non-existent-slug";
        when(postRepo.findOneBySlug(slug)).thenReturn(null);

        // Act
        Post result = postService.getBySlug(slug);

        // Assert
        assertNull(result);
    }
}