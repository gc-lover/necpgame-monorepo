package com.necpgame.adminservice.service;

import com.necpgame.adminservice.model.AppearanceOptions;
import com.necpgame.adminservice.model.CharacterCreationFlow;
import com.necpgame.adminservice.model.GetCharacterSelectData200Response;
import com.necpgame.adminservice.model.GetServerList200Response;
import com.necpgame.adminservice.model.GetUIFeatures200Response;
import com.necpgame.adminservice.model.HUDData;
import com.necpgame.adminservice.model.LoginScreenData;
import com.necpgame.adminservice.model.UISettings;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for TechnicalService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface TechnicalService {

    /**
     * GET /technical/ui/character-creation/appearance-options : Получить опции внешности
     *
     * @return AppearanceOptions
     */
    AppearanceOptions getAppearanceOptions();

    /**
     * GET /technical/ui/character-creation/flow : Получить flow создания персонажа
     *
     * @return CharacterCreationFlow
     */
    CharacterCreationFlow getCharacterCreationFlow();

    /**
     * GET /technical/ui/character-select/{account_id} : Получить персонажей для выбора
     *
     * @param accountId  (required)
     * @return GetCharacterSelectData200Response
     */
    GetCharacterSelectData200Response getCharacterSelectData(UUID accountId);

    /**
     * GET /technical/ui/hud/{character_id} : Получить данные для HUD
     *
     * @param characterId  (required)
     * @return HUDData
     */
    HUDData getHUDData(UUID characterId);

    /**
     * GET /technical/ui/login : Получить данные для экрана входа
     *
     * @return LoginScreenData
     */
    LoginScreenData getLoginScreenData();

    /**
     * GET /technical/ui/servers : Получить список серверов
     *
     * @return GetServerList200Response
     */
    GetServerList200Response getServerList();

    /**
     * GET /technical/ui/features : Получить доступные UI features
     *
     * @return GetUIFeatures200Response
     */
    GetUIFeatures200Response getUIFeatures();

    /**
     * GET /technical/ui/settings/{character_id} : Получить UI настройки персонажа
     *
     * @param characterId  (required)
     * @return UISettings
     */
    UISettings getUISettings(UUID characterId);

    /**
     * PUT /technical/ui/settings/{character_id} : Обновить UI настройки
     *
     * @param characterId  (required)
     * @param uiSettings  (required)
     * @return Void
     */
    Void updateUISettings(UUID characterId, UISettings uiSettings);
}

