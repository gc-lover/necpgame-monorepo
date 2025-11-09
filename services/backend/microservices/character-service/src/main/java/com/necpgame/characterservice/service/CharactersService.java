package com.necpgame.characterservice.service;

import com.necpgame.characterservice.model.CharacterStats;
import com.necpgame.characterservice.model.CharacterStatus;
import com.necpgame.characterservice.model.GetCharacterSkills200Response;
import org.springframework.lang.Nullable;
import java.util.UUID;
import com.necpgame.characterservice.model.UpdateCharacterStatusRequest;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for CharactersService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface CharactersService {

    /**
     * GET /characters/{characterId}/skills : Навыки персонажа
     *
     * @param characterId  (required)
     * @return GetCharacterSkills200Response
     */
    GetCharacterSkills200Response getCharacterSkills(UUID characterId);

    /**
     * GET /characters/{characterId}/stats : Характеристики персонажа
     *
     * @param characterId  (required)
     * @return CharacterStats
     */
    CharacterStats getCharacterStats(UUID characterId);

    /**
     * GET /characters/{characterId}/status : Текущий статус персонажа
     *
     * @param characterId  (required)
     * @return CharacterStatus
     */
    CharacterStatus getCharacterStatus(UUID characterId);

    /**
     * POST /characters/{characterId}/status/update : Обновить статус персонажа
     *
     * @param characterId  (required)
     * @param updateCharacterStatusRequest  (optional)
     * @return CharacterStatus
     */
    CharacterStatus updateCharacterStatus(UUID characterId, UpdateCharacterStatusRequest updateCharacterStatusRequest);
}

