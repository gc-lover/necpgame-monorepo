package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.GameplayCombatAbilitiesApi;
import com.necpgame.backjava.model.*;
import com.necpgame.backjava.service.AbilitiesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AbilitiesController implements GameplayCombatAbilitiesApi {
    
    private final AbilitiesService service;
    
    @Override
    public ResponseEntity<GetAbilities200Response> getAbilities(String characterId, String source) {
        log.info("GET /gameplay/combat/abilities?characterId={}", characterId);
        return ResponseEntity.ok(service.getAbilities(characterId, source));
    }
    
    @Override
    public ResponseEntity<Ability> getAbility(String abilityId) {
        log.info("GET /gameplay/combat/abilities/{}", abilityId);
        return ResponseEntity.ok(service.getAbility(abilityId));
    }
    
    @Override
    public ResponseEntity<AbilityUseResult> useAbility(AbilityUseRequest abilityUseRequest) {
        log.info("POST /gameplay/combat/abilities/use");
        return ResponseEntity.ok(service.useAbility(abilityUseRequest));
    }
    
    @Override
    public ResponseEntity<AbilityLoadout> getAbilityLoadout(String characterId) {
        log.info("GET /gameplay/combat/abilities/loadout?characterId={}", characterId);
        return ResponseEntity.ok(service.getAbilityLoadout(characterId));
    }
    
    @Override
    public ResponseEntity<AbilityLoadout> updateAbilityLoadout(AbilityLoadout abilityLoadout) {
        log.info("POST /gameplay/combat/abilities/loadout");
        return ResponseEntity.ok(service.updateAbilityLoadout(abilityLoadout));
    }
    
    @Override
    public ResponseEntity<GetAbilitySynergies200Response> getAbilitySynergies(String characterId) {
        log.info("GET /gameplay/combat/abilities/synergies?characterId={}", characterId);
        return ResponseEntity.ok(service.getAbilitySynergies(characterId));
    }
    
    @Override
    public ResponseEntity<GetAbilityCooldowns200Response> getAbilityCooldowns(String characterId) {
        log.info("GET /gameplay/combat/abilities/cooldowns?characterId={}", characterId);
        return ResponseEntity.ok(service.getAbilityCooldowns(characterId));
    }
}

