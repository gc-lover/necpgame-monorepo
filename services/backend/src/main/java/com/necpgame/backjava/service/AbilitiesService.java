package com.necpgame.backjava.service;

import com.necpgame.backjava.model.*;

/**
 * AbilitiesService - сервис для работы со способностями.
 * 
 * Сгенерировано на основе: API-SWAGGER/api/v1/gameplay/combat/abilities.yaml
 */
public interface AbilitiesService {

    GetAbilities200Response getAbilities(String characterId, String source);

    Ability getAbility(String abilityId);

    AbilityUseResult useAbility(AbilityUseRequest request);

    AbilityLoadout getAbilityLoadout(String characterId);

    AbilityLoadout updateAbilityLoadout(AbilityLoadout request);

    GetAbilitySynergies200Response getAbilitySynergies(String characterId);

    GetAbilityCooldowns200Response getAbilityCooldowns(String characterId);
}

