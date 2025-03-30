package com.haibui.inpogram.models.repositories;

import com.haibui.inpogram.models.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    List<Tag> findAllByOrderByUsageCountDescNameAsc();

    Tag findOneByName(String name);
}
