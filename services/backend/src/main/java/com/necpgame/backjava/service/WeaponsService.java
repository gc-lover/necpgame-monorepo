package com.necpgame.backjava.service;

import com.necpgame.backjava.model.*;

import java.util.UUID;

/**
 * WeaponsService - сервис для работы с оружием.
 * 
 * Сгенерировано на основе: API-SWAGGER/api/v1/gameplay/combat/weapons.yaml
 */
public interface WeaponsService {

    /**
     * Получить каталог оружия.
     */
    GetWeaponsCatalog200Response getWeaponsCatalog(String weaponClass, String brand, String rarity);

    /**
     * Получить детальную информацию об оружии.
     */
    WeaponDetails getWeapon(String weaponId);

    /**
     * Получить оружие по бренду.
     */
    GetWeaponsByBrand200Response getWeaponsByBrand(String brand);

    /**
     * Получить оружие по классу.
     */
    GetWeaponsByClass200Response getWeaponsByClass(String propertyClass);

    /**
     * Получить прогресс мастерства оружия.
     */
    WeaponMasteryProgress getWeaponMastery(String characterId);

    /**
     * Обновить прогресс мастерства оружия.
     */
    WeaponMasteryProgress updateWeaponMastery(UpdateWeaponMasteryRequest request);

    /**
     * Получить доступные моды для оружия.
     */
    GetWeaponMods200Response getWeaponMods(String weaponClass);

    /**
     * Получить мета оружие (популярное в PvP).
     */
    GetMetaWeapons200Response getMetaWeapons(String contentType);
}

