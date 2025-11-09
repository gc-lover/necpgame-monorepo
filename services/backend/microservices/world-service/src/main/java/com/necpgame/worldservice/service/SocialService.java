package com.necpgame.worldservice.service;

import com.necpgame.worldservice.model.Error;
import org.springframework.lang.Nullable;
import com.necpgame.worldservice.model.PlayerOrderNewsListResponse;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for SocialService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface SocialService {

    /**
     * GET /social/player-orders/news : Получить новостную ленту
     * Возвращает новости и уведомления, связанные с влиянием заказов игроков.
     *
     * @param cityId Фильтр по городу или региону. (optional)
     * @param effectType Тип эффекта влияния. (optional)
     * @param severity Уровень критичности эффекта. (optional)
     * @param locale Локализация текстов новостей и описаний. (optional)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return PlayerOrderNewsListResponse
     */
    PlayerOrderNewsListResponse listPlayerOrderNews(String cityId, String effectType, String severity, String locale, Integer page, Integer pageSize);
}

