package com.necpgame.gameplayservice.service;

import com.necpgame.gameplayservice.model.Error;
import com.necpgame.gameplayservice.model.GetLeagueTypes200Response;
import com.necpgame.gameplayservice.model.Leaderboard;
import com.necpgame.gameplayservice.model.League;
import com.necpgame.gameplayservice.model.LeaguePhase;
import com.necpgame.gameplayservice.model.LeagueRewards;
import com.necpgame.gameplayservice.model.LeagueTime;
import com.necpgame.gameplayservice.model.MetaProgress;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for MetaService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface MetaService {

    /**
     * GET /meta/league/current : Получить текущую лигу
     * Возвращает информацию о текущей активной лиге. Включает параметры, фазу, игровое время, оставшееся время до сброса. 
     *
     * @return League
     */
    League getCurrentLeague();

    /**
     * GET /meta/league/phase : Получить текущую фазу лиги
     * Возвращает детали текущей фазы лиги (Start, Rise, Crisis, Endgame, Finale). Каждая фаза имеет свои особенности и контент. 
     *
     * @return LeaguePhase
     */
    LeaguePhase getCurrentPhase();

    /**
     * GET /meta/league/leaderboard : Получить рейтинг лиги
     * Возвращает рейтинг игроков в текущей лиге. Можно фильтровать по типу (overall, pvp, pve, wealth, faction). 
     *
     * @param rankingType Тип рейтинга (optional, default to overall)
     * @param limit Количество записей (optional, default to 100)
     * @param offset Смещение (пагинация) (optional, default to 0)
     * @return Leaderboard
     */
    Leaderboard getLeagueLeaderboard(String rankingType, Integer limit, Integer offset);

    /**
     * GET /meta/league/rewards : Получить награды лиги
     * Возвращает награды за текущую лигу (по рейтингу). Награды выдаются в конце лиги (Finale). 
     *
     * @param accountId  (required)
     * @return LeagueRewards
     */
    LeagueRewards getLeagueRewards(String accountId);

    /**
     * GET /meta/league/time : Получить игровое время
     * Возвращает текущее игровое время в лиге и параметры ускорения. Игровое время: 2020-2093 (73 года). 
     *
     * @return LeagueTime
     */
    LeagueTime getLeagueTime();

    /**
     * GET /meta/league/types : Получить типы лиг
     * Возвращает доступные типы лиг (Standard, Hardcore, Event). Игрок выбирает тип при создании персонажа. 
     *
     * @return GetLeagueTypes200Response
     */
    GetLeagueTypes200Response getLeagueTypes();

    /**
     * GET /meta/league/meta-progress/{account_id} : Получить мета-прогресс аккаунта
     * Возвращает мета-прогресс аккаунта (сохраняется между лигами). Включает: рейтинг, достижения, титулы, косметику. 
     *
     * @param accountId ID аккаунта (required)
     * @return MetaProgress
     */
    MetaProgress getMetaProgress(String accountId);
}

