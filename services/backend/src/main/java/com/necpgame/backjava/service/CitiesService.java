package com.necpgame.backjava.service;

import com.necpgame.backjava.model.GetCities200Response;

import java.util.UUID;

/**
 * CitiesService - сервис для работы с городами.
 * 
 * Сгенерировано на основе: API-SWAGGER/api/v1/auth/character-creation-reference-models.yaml
 */
public interface CitiesService {

    /**
     * Получить список доступных городов.
     */
    GetCities200Response getCities(UUID factionId, String region);
}

