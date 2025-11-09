package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterSnapshotEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterSnapshotRepository extends JpaRepository<CharacterSnapshotEntity, UUID> {

    List<CharacterSnapshotEntity> findTop10ByCharacterIdOrderByTakenAtDesc(UUID characterId);
}
