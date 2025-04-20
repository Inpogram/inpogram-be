package com.haibui.inpogram.controllers;

import com.haibui.inpogram.exceptions.PostNotFoundException;
import com.haibui.inpogram.models.entities.Post;
import com.haibui.inpogram.services.PostService;
import com.haibui.inpogram.utils.AuthenticatedControllerTestBase;
import com.haibui.inpogram.utils.TestSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class PostControllerTest extends AuthenticatedControllerTestBase {
    @MockBean
    private PostService postService;

    private Post post;

    @BeforeEach
    void setUp() {
        // Sample post for testing
        post = new Post();
        post.setSlug("test-slug");
        post.setTitle("Test Post");
        post.setContent("This is a test post.");
    }

    @Test
    void getBySlug_WhenPostExists_ShouldReturnPost() throws Exception {
        // Arrange
        when(postService.getBySlug("test-slug")).thenReturn(post);

        // Act & Assert
        mockMvc.perform(get("/api/v1/posts/test-slug")
                    .with(withAuth())
                    .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> {
                    System.out.println("Status: " + result.getResponse().getStatus());
                    System.out.println("Response: " + result.getResponse().getContentAsString());
                })
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug").value("test-slug"))
                .andExpect(jsonPath("$.title").value("Test Post"))
                .andExpect(jsonPath("$.content").value("This is a test post."));
    }

    @Test
    void getBySlug_WhenPostDoesNotExist_ShouldThrowNotFound() throws Exception {
        // Arrange
        when(postService.getBySlug("non-existent-slug"))
                .thenThrow(new PostNotFoundException("Post with slug 'non-existent-slug' not found"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/posts/non-existent-slug")
                        .with(withAuth())
                        .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Post with slug 'non-existent-slug' not found"));
    }

    @Test
    void getBySlug_WhenNotAuthenticated_ShouldReturnUnauthorized() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/posts/test-slug")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
