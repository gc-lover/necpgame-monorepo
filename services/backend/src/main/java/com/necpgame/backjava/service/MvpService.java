package com.necpgame.backjava.service;

import com.necpgame.backjava.model.ContentOverview;
import com.necpgame.backjava.model.ContentStatus;
import com.necpgame.backjava.model.GetMVPEndpoints200Response;
import com.necpgame.backjava.model.GetMVPHealth200Response;
import com.necpgame.backjava.model.GetMVPModels200Response;
import com.necpgame.backjava.model.InitialGameData;
import com.necpgame.backjava.model.MainGameUIData;
import org.springframework.lang.Nullable;
import com.necpgame.backjava.model.TextVersionState;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for MvpService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface MvpService {

    /**
     * GET /mvp/content/overview : Получить обзор контента
     *
     * @param period  (optional)
     * @return ContentOverview
     */
    ContentOverview getContentOverview(String period);

    /**
     * GET /mvp/content/status : Получить статус готовности контента
     *
     * @return ContentStatus
     */
    ContentStatus getContentStatus();

    /**
     * GET /mvp/initial-data : Получить начальные данные игры
     * Данные для первого запуска игры
     *
     * @return InitialGameData
     */
    InitialGameData getInitialData();

    /**
     * GET /mvp/endpoints : Получить список MVP endpoints
     *
     * @return GetMVPEndpoints200Response
     */
    GetMVPEndpoints200Response getMVPEndpoints();

    /**
     * GET /mvp/health : Проверка здоровья MVP систем
     *
     * @return GetMVPHealth200Response
     */
    GetMVPHealth200Response getMVPHealth();

    /**
     * GET /mvp/models : Получить data models для MVP
     *
     * @return GetMVPModels200Response
     */
    GetMVPModels200Response getMVPModels();

    /**
     * GET /mvp/ui/main-game : Получить данные для основного UI
     *
     * @param characterId  (required)
     * @return MainGameUIData
     */
    MainGameUIData getMainGameUI(UUID characterId);

    /**
     * GET /mvp/text-version/state : Получить состояние игры для текстовой версии
     * Упрощенное состояние для текстового frontend
     *
     * @param characterId  (required)
     * @return TextVersionState
     */
    TextVersionState getTextVersionState(UUID characterId);
}

