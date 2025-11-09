package com.necpgame.authservice.service;

import com.necpgame.authservice.model.Error;
import com.necpgame.authservice.model.GetFactions200Response;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for FactionsService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface FactionsService {

    /**
     * GET /factions : Список доступных фракций
     * Получает список всех доступных фракций. Может быть отфильтрован по происхождению.
     *
     * @param origin Фильтр по происхождению (опционально) (optional)
     * @return GetFactions200Response
     */
    GetFactions200Response getFactions(String origin);
}

