package com.haibui.inpogram.services;

import com.haibui.inpogram.models.dtos.TagRequest;
import com.haibui.inpogram.models.entities.Tag;
import com.haibui.inpogram.models.repositories.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {
    private final TagRepository tagRepo;

    public List<Tag> findAll() {
        return tagRepo.findAllByOrderByUsageCountDescNameAsc();
    }

    public Tag createTag(TagRequest tagRequest) {
        Tag createdTag = Tag.builder().name(tagRequest.name()).build();
        return tagRepo.save(createdTag);
    }
}
