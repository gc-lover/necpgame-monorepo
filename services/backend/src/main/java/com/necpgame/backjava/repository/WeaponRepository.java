package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.WeaponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * WeaponRepository - репозиторий для работы с оружием.
 * 
 * Источник: API-SWAGGER/api/v1/gameplay/combat/weapons.yaml
 */
@Repository
public interface WeaponRepository extends JpaRepository<WeaponEntity, String> {

    /**
     * Найти оружие по классу.
     */
    @Query("SELECT w FROM WeaponEntity w WHERE w.weaponClass = :weaponClass AND w.available = true ORDER BY w.rarity DESC, w.name")
    List<WeaponEntity> findByWeaponClass(String weaponClass);

    /**
     * Найти оружие по бренду.
     */
    @Query("SELECT w FROM WeaponEntity w WHERE w.brand = :brand AND w.available = true ORDER BY w.rarity DESC, w.name")
    List<WeaponEntity> findByBrand(String brand);

    /**
     * Найти оружие по редкости.
     */
    @Query("SELECT w FROM WeaponEntity w WHERE w.rarity = :rarity AND w.available = true ORDER BY w.price DESC, w.name")
    List<WeaponEntity> findByRarity(String rarity);

    /**
     * Найти доступное оружие для персонажа (по уровню).
     */
    @Query("SELECT w FROM WeaponEntity w WHERE w.minLevel <= :level AND w.available = true ORDER BY w.weaponClass, w.rarity DESC")
    List<WeaponEntity> findAvailableForLevel(Integer level);

    /**
     * Найти все доступное оружие.
     */
    @Query("SELECT w FROM WeaponEntity w WHERE w.available = true ORDER BY w.weaponClass, w.rarity DESC, w.name")
    List<WeaponEntity> findAllAvailable();
}

