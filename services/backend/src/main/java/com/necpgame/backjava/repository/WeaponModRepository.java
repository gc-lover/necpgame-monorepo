package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.WeaponModEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * WeaponModRepository - репозиторий для работы с модами оружия.
 * 
 * Источник: API-SWAGGER/api/v1/gameplay/combat/weapons.yaml
 */
@Repository
public interface WeaponModRepository extends JpaRepository<WeaponModEntity, String> {

    /**
     * Найти моды по типу.
     */
    @Query("SELECT wm FROM WeaponModEntity wm WHERE wm.modType = :modType AND wm.available = true ORDER BY wm.rarity DESC, wm.name")
    List<WeaponModEntity> findByModType(String modType);

    /**
     * Найти моды по типу слота.
     */
    @Query("SELECT wm FROM WeaponModEntity wm WHERE wm.slotType = :slotType AND wm.available = true ORDER BY wm.rarity DESC, wm.name")
    List<WeaponModEntity> findBySlotType(String slotType);

    /**
     * Найти все доступные моды.
     */
    @Query("SELECT wm FROM WeaponModEntity wm WHERE wm.available = true ORDER BY wm.slotType, wm.rarity DESC, wm.name")
    List<WeaponModEntity> findAllAvailable();
}

