package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterSlotEntity;
import com.necpgame.backjava.entity.CharacterSlotId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для управления слотами персонажей игрока.
 */
@Repository
public interface CharacterSlotRepository extends JpaRepository<CharacterSlotEntity, CharacterSlotId> {

    List<CharacterSlotEntity> findByIdPlayerIdOrderByIdSlotNumber(UUID playerId);

    Optional<CharacterSlotEntity> findByCharacterId(UUID characterId);

    Optional<CharacterSlotEntity> findFirstByIdPlayerIdAndUnlockedTrueAndCharacterIdIsNullOrderByIdSlotNumber(UUID playerId);

    long countByIdPlayerIdAndUnlockedTrue(UUID playerId);

    long countByIdPlayerIdAndUnlockedTrueAndCharacterIdIsNull(UUID playerId);

    @Query("SELECT COUNT(cs) > 0 FROM CharacterSlotEntity cs WHERE cs.id.playerId = :playerId AND cs.characterId = :characterId")
    boolean isCharacterAssigned(UUID playerId, UUID characterId);
}

