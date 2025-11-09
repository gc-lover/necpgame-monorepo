package com.necpgame.gameplayservice.service;

import com.necpgame.gameplayservice.model.Error;
import com.necpgame.gameplayservice.model.GetSkillCategories200Response;
import com.necpgame.gameplayservice.model.IncreaseSkill200Response;
import com.necpgame.gameplayservice.model.IncreaseSkillRequest;
import org.springframework.lang.Nullable;
import com.necpgame.gameplayservice.model.SkillDetails;
import com.necpgame.gameplayservice.model.SkillsResponse;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for GameplayService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface GameplayService {

    /**
     * GET /gameplay/progression/skills/{skill_id} : Получить детали навыка
     * Возвращает детальную информацию о навыке
     *
     * @param skillId  (required)
     * @return SkillDetails
     */
    SkillDetails getSkill(String skillId);

    /**
     * GET /gameplay/progression/skills/categories : Получить категории навыков
     * Возвращает все категории навыков с описаниями
     *
     * @return GetSkillCategories200Response
     */
    GetSkillCategories200Response getSkillCategories();

    /**
     * GET /gameplay/progression/skills : Получить навыки персонажа
     * Возвращает все навыки персонажа с текущими уровнями и прогрессом. Включает общие и классовые навыки. 
     *
     * @param characterId  (required)
     * @param category Фильтр по категории навыков (optional)
     * @return SkillsResponse
     */
    SkillsResponse getSkills(String characterId, String category);

    /**
     * POST /gameplay/progression/skills/increase : Повысить навык
     * Повышает навык на основе действий персонажа. Прокачка \&quot;через боль\&quot; (KENSHI стиль): использование в сложных ситуациях дает больше опыта. 
     *
     * @param increaseSkillRequest  (required)
     * @return IncreaseSkill200Response
     */
    IncreaseSkill200Response increaseSkill(IncreaseSkillRequest increaseSkillRequest);
}

