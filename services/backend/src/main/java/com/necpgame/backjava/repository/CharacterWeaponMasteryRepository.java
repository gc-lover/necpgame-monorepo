package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterWeaponMasteryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * CharacterWeaponMasteryRepository - репозиторий для работы с мастерством оружия.
 * 
 * Источник: API-SWAGGER/api/v1/gameplay/combat/weapons.yaml
 */
@Repository
public interface CharacterWeaponMasteryRepository extends JpaRepository<CharacterWeaponMasteryEntity, UUID> {

    /**
     * Найти мастерство персонажа по классу оружия.
     */
    @Query("SELECT cwm FROM CharacterWeaponMasteryEntity cwm WHERE cwm.characterId = :characterId AND cwm.weaponClass = :weaponClass")
    Optional<CharacterWeaponMasteryEntity> findByCharacterIdAndWeaponClass(UUID characterId, String weaponClass);

    /**
     * Найти все мастерства персонажа.
     */
    @Query("SELECT cwm FROM CharacterWeaponMasteryEntity cwm WHERE cwm.characterId = :characterId ORDER BY cwm.masteryRank DESC, cwm.experience DESC")
    List<CharacterWeaponMasteryEntity> findByCharacterId(UUID characterId);

    /**
     * Проверить существование мастерства.
     */
    @Query("SELECT COUNT(cwm) > 0 FROM CharacterWeaponMasteryEntity cwm WHERE cwm.characterId = :characterId AND cwm.weaponClass = :weaponClass")
    boolean existsByCharacterIdAndWeaponClass(UUID characterId, String weaponClass);
}

