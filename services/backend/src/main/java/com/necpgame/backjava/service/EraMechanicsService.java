package com.necpgame.backjava.service;

import com.necpgame.backjava.model.DCScaling;
import com.necpgame.backjava.model.EraInfo;
import com.necpgame.backjava.model.EraMechanics;
import com.necpgame.backjava.model.GetFactionAISliders200Response;
import com.necpgame.backjava.model.GetTechnologyAccess200Response;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for EraMechanicsService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface EraMechanicsService {

    /**
     * GET /gameplay/world/era/current : Получить информацию о текущей эре
     *
     * @return EraInfo
     */
    EraInfo getCurrentEra();

    /**
     * GET /gameplay/world/era/{era}/dc-scaling : Получить DC скейлинг для эры
     * Сложность проверок (D&amp;D DC) зависит от эры
     *
     * @param era  (required)
     * @return DCScaling
     */
    DCScaling getDCScaling(String era);

    /**
     * GET /gameplay/world/era/{era}/mechanics : Получить механики эры
     *
     * @param era  (required)
     * @return EraMechanics
     */
    EraMechanics getEraMechanics(String era);

    /**
     * GET /gameplay/world/factions/ai-sliders : Получить AI-слайдеры фракций
     * Влияние и агрессивность фракций
     *
     * @return GetFactionAISliders200Response
     */
    GetFactionAISliders200Response getFactionAISliders();

    /**
     * GET /gameplay/world/events/technology/access : Получить доступные технологии
     * Зависит от текущей эры
     *
     * @return GetTechnologyAccess200Response
     */
    GetTechnologyAccess200Response getTechnologyAccess();
}

