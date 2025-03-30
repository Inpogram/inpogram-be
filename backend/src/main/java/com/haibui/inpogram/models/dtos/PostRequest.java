package com.haibui.inpogram.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public record PostRequest(@NotBlank(message = "Title must not be blank")
                          @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
                          String title,
                          MultipartFile bannerImage,
                          @NotBlank(message = "Content must not be blank")
                          @Size(min = 100, message = "Content must consist of at least 100 words")
                          String content,
                          Set<String> tags) {
}
