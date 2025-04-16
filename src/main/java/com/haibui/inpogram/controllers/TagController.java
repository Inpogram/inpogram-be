package com.haibui.inpogram.controllers;

import com.haibui.inpogram.models.dtos.TagRequest;
import com.haibui.inpogram.models.entities.Tag;
import com.haibui.inpogram.services.TagService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
@AllArgsConstructor
@Slf4j
public class TagController {
    private final TagService tagService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<Tag> results = tagService.findAll();
        return ResponseEntity.ok(results);
    }

    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody @Validated TagRequest tagRequest) {
        Tag createdTag = tagService.createTag(tagRequest);
        return ResponseEntity.ok(createdTag);
    }
}
