package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterAbilityCooldownEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CharacterAbilityCooldownRepository extends JpaRepository<CharacterAbilityCooldownEntity, UUID> {

    @Query("SELECT cac FROM CharacterAbilityCooldownEntity cac WHERE cac.characterId = :characterId ORDER BY cac.readyAt")
    List<CharacterAbilityCooldownEntity> findByCharacterId(UUID characterId);

    @Query("SELECT cac FROM CharacterAbilityCooldownEntity cac WHERE cac.characterId = :characterId AND cac.abilityId = :abilityId AND cac.readyAt > :now")
    Optional<CharacterAbilityCooldownEntity> findActiveByCharacterIdAndAbilityId(UUID characterId, String abilityId, LocalDateTime now);

    @Query("DELETE FROM CharacterAbilityCooldownEntity cac WHERE cac.readyAt < :now")
    void deleteExpired(LocalDateTime now);
}

