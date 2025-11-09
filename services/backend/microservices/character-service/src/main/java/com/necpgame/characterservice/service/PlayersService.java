package com.necpgame.characterservice.service;

import com.necpgame.characterservice.model.CreatePlayerCharacterRequest;
import com.necpgame.characterservice.model.DeletePlayerCharacter200Response;
import com.necpgame.characterservice.model.Error;
import com.necpgame.characterservice.model.GetCharacters200Response;
import org.springframework.lang.Nullable;
import com.necpgame.characterservice.model.PlayerCharacter;
import com.necpgame.characterservice.model.PlayerCharacterDetails;
import com.necpgame.characterservice.model.PlayerProfile;
import com.necpgame.characterservice.model.SwitchCharacter200Response;
import com.necpgame.characterservice.model.SwitchCharacterRequest;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for PlayersService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface PlayersService {

    /**
     * POST /players/characters/create : Создать персонажа
     * Создает нового персонажа. Требует свободный слот (3 базовых + 2 premium). 
     *
     * @param createPlayerCharacterRequest  (required)
     * @return PlayerCharacter
     */
    PlayerCharacter createPlayerCharacter(CreatePlayerCharacterRequest createPlayerCharacterRequest);

    /**
     * DELETE /players/characters/{character_id} : Удалить персонажа
     * Удаляет персонажа (soft delete). Grace period 30 дней - можно восстановить. 
     *
     * @param characterId  (required)
     * @return DeletePlayerCharacter200Response
     */
    DeletePlayerCharacter200Response deletePlayerCharacter(String characterId);

    /**
     * GET /players/characters/{character_id} : Получить детали персонажа
     * Возвращает полную информацию о персонаже
     *
     * @param characterId  (required)
     * @return PlayerCharacterDetails
     */
    PlayerCharacterDetails getCharacter(String characterId);

    /**
     * GET /players/characters : Получить список персонажей
     * Возвращает список всех персонажей игрока. Включая удаленных (в grace period). 
     *
     * @param includeDeleted  (optional, default to false)
     * @return GetCharacters200Response
     */
    GetCharacters200Response getCharacters(Boolean includeDeleted);

    /**
     * GET /players/profile : Получить профиль игрока
     * Возвращает профиль игрока (account-wide данные). Premium currency, settings, статистика. 
     *
     * @return PlayerProfile
     */
    PlayerProfile getPlayerProfile();

    /**
     * POST /players/characters/{character_id}/restore : Восстановить персонажа
     * Восстанавливает удаленного персонажа. Доступно в течение 30 дней после удаления. 
     *
     * @param characterId  (required)
     * @return PlayerCharacter
     */
    PlayerCharacter restoreCharacter(String characterId);

    /**
     * POST /players/characters/switch : Переключиться на персонажа
     * Переключает активного персонажа. Завершает текущую сессию, начинает новую. 
     *
     * @param switchCharacterRequest  (required)
     * @return SwitchCharacter200Response
     */
    SwitchCharacter200Response switchCharacter(SwitchCharacterRequest switchCharacterRequest);
}

