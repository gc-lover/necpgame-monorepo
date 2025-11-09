package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.GameplayCombatWeaponsApi;
import com.necpgame.backjava.model.*;
import com.necpgame.backjava.service.WeaponsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST Controller для работы с оружием.
 * 
 * Реализует контракт {@link GameplayCombatWeaponsApi}, сгенерированный из OpenAPI спецификации.
 * Источник: API-SWAGGER/api/v1/gameplay/combat/weapons.yaml
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class WeaponsController implements GameplayCombatWeaponsApi {
    
    private final WeaponsService service;
    
    @Override
    public ResponseEntity<GetWeaponsCatalog200Response> getWeaponsCatalog(String weaponClass, String brand, String rarity) {
        log.info("GET /gameplay/combat/weapons");
        return ResponseEntity.ok(service.getWeaponsCatalog(weaponClass, brand, rarity));
    }
    
    @Override
    public ResponseEntity<WeaponDetails> getWeapon(String weaponId) {
        log.info("GET /gameplay/combat/weapons/{}", weaponId);
        return ResponseEntity.ok(service.getWeapon(weaponId));
    }
    
    @Override
    public ResponseEntity<GetWeaponsByBrand200Response> getWeaponsByBrand(String brand) {
        log.info("GET /gameplay/combat/weapons/by-brand/{}", brand);
        return ResponseEntity.ok(service.getWeaponsByBrand(brand));
    }
    
    @Override
    public ResponseEntity<GetWeaponsByClass200Response> getWeaponsByClass(String propertyClass) {
        log.info("GET /gameplay/combat/weapons/by-class/{}", propertyClass);
        return ResponseEntity.ok(service.getWeaponsByClass(propertyClass));
    }
    
    @Override
    public ResponseEntity<WeaponMasteryProgress> getWeaponMastery(String characterId) {
        log.info("GET /gameplay/combat/weapons/mastery/{}", characterId);
        return ResponseEntity.ok(service.getWeaponMastery(characterId));
    }
    
    @Override
    public ResponseEntity<WeaponMasteryProgress> updateWeaponMastery(UpdateWeaponMasteryRequest updateWeaponMasteryRequest) {
        log.info("POST /gameplay/combat/weapons/mastery/update");
        return ResponseEntity.ok(service.updateWeaponMastery(updateWeaponMasteryRequest));
    }
    
    @Override
    public ResponseEntity<GetWeaponMods200Response> getWeaponMods(String weaponClass) {
        log.info("GET /gameplay/combat/weapons/mods?weaponClass={}", weaponClass);
        return ResponseEntity.ok(service.getWeaponMods(weaponClass));
    }
    
    @Override
    public ResponseEntity<GetMetaWeapons200Response> getMetaWeapons(String contentType) {
        log.info("GET /gameplay/combat/weapons/meta/{}", contentType);
        return ResponseEntity.ok(service.getMetaWeapons(contentType));
    }
}

