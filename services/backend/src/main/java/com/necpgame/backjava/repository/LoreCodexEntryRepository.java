package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.LoreCodexEntryEntity;
import com.necpgame.backjava.entity.enums.LoreCodexCategory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoreCodexEntryRepository extends JpaRepository<LoreCodexEntryEntity, UUID> {

    Optional<LoreCodexEntryEntity> findByEntryId(String entryId);

    List<LoreCodexEntryEntity> findByCategory(LoreCodexCategory category);
}


