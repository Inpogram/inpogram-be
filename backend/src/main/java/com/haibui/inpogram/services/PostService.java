package com.haibui.inpogram.services;

import com.haibui.inpogram.exceptions.FileTooLargeException;
import com.haibui.inpogram.exceptions.InvalidFileExtensionException;
import com.haibui.inpogram.exceptions.PostTitleAlreadyExistsException;
import com.haibui.inpogram.models.dtos.PostRequest;
import com.haibui.inpogram.models.entities.Post;
import com.haibui.inpogram.models.entities.Tag;
import com.haibui.inpogram.models.repositories.PostRepository;
import com.haibui.inpogram.models.repositories.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostService {
    private static final long MAX_BANNER_IMAGE_SIZE = 3 * 1024 * 1024; // 3MB

    private static final Set<String> VALID_BANNER_IMAGE_EXTENSIONS = Set.of(".png", ".jpg", ".jpeg");

    private final PostRepository postRepo;

    private final TagRepository tagRepo;

    private final S3Service s3Service;

    public List<Post> findAll() {
        return postRepo.findAll();
    }

    @Transactional
    public Post createPost(PostRequest postRequest) throws Exception {
        Post post = postRepo.findOneByTitle(postRequest.title());
        if (post != null) {
            throw new PostTitleAlreadyExistsException("Post title already exists");
        }

        // Check validity of banner file
        MultipartFile bannerImage = postRequest.bannerImage();
        validateBannerImage(bannerImage);

        String newBannerImageName = s3Service.uploadFile(bannerImage);


        // Check validity of tags
        Set<Tag> insertedTags = new HashSet<>();

        for (String tagName : postRequest.tags()) {
            Tag existingTag = tagRepo.findOneByName(tagName);

            // create new tag if not found
            Tag tag = new Tag();
            if (existingTag == null) {
                tag.setName(tagName);
                tag.setUsageCount(0);
            } else {
                tag = existingTag;
            }

            insertedTags.add(tag);
            tag.setUsageCount(tag.getUsageCount() + 1);
        }
        tagRepo.saveAll(insertedTags);

        Post createdPost = Post.builder()
                .title(postRequest.title())
                .bannerImageName(newBannerImageName)
                .content(postRequest.content())
                .createdAt(LocalDateTime.now())
                .tags(insertedTags)
                .build();

        return postRepo.save(createdPost);
    }

    private String getFileExtension(String originalFileName) {
        if (originalFileName != null) {
            int dotPos = originalFileName.lastIndexOf(".");
            if (dotPos >= 0) {
                return originalFileName.substring(dotPos);
            }
        }

        return null;
    }

    private void validateBannerImage(MultipartFile bannerImage) throws InvalidFileExtensionException, FileTooLargeException {
        if (bannerImage.getSize() > MAX_BANNER_IMAGE_SIZE) {
            throw new FileTooLargeException("Banner image should be less than 3MB in size");
        }

        String bannerImageExtension = getFileExtension(bannerImage.getOriginalFilename());
        if (bannerImageExtension == null || !VALID_BANNER_IMAGE_EXTENSIONS.contains(bannerImageExtension.toLowerCase())) {
            throw new InvalidFileExtensionException("The system only supports .png, .jpg, .jpeg extension for the banner image");
        }
    }

    public Post findByTitle(String title) {
        return postRepo.findOneByTitle(title);
    }
}
