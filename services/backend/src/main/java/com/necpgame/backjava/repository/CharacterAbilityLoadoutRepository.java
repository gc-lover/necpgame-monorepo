package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterAbilityLoadoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CharacterAbilityLoadoutRepository extends JpaRepository<CharacterAbilityLoadoutEntity, UUID> {

    @Query("SELECT cal FROM CharacterAbilityLoadoutEntity cal WHERE cal.characterId = :characterId ORDER BY cal.slotType")
    List<CharacterAbilityLoadoutEntity> findByCharacterId(UUID characterId);

    @Query("SELECT cal FROM CharacterAbilityLoadoutEntity cal WHERE cal.characterId = :characterId AND cal.slotType = :slot")
    Optional<CharacterAbilityLoadoutEntity> findByCharacterIdAndSlot(UUID characterId, String slot);
}

