package com.haibui.inpogram.models.repositories;

import com.haibui.inpogram.models.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    List<Tag> findAllByOrderByUsageCountDescNameAsc();

    Tag findOneByName(String name);
}
