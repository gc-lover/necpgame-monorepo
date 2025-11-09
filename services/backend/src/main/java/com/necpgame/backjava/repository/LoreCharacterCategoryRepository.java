package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.LoreCharacterCategoryEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoreCharacterCategoryRepository extends JpaRepository<LoreCharacterCategoryEntity, UUID> {

    List<LoreCharacterCategoryEntity> findAllByOrderByNameAsc();

    List<LoreCharacterCategoryEntity> findTop10ByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
}


