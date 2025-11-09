package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.LoreCodexEntryEntity;
import com.necpgame.backjava.entity.LoreCodexProgressEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoreCodexProgressRepository extends JpaRepository<LoreCodexProgressEntity, UUID> {

    Optional<LoreCodexProgressEntity> findByCharacterIdAndEntry(UUID characterId, LoreCodexEntryEntity entry);

    List<LoreCodexProgressEntity> findByCharacterId(UUID characterId);
}


