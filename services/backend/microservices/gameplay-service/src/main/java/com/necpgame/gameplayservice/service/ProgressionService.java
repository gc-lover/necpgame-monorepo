package com.necpgame.gameplayservice.service;

import com.necpgame.gameplayservice.model.AddSkillExperienceRequest;
import com.necpgame.gameplayservice.model.AwardExperienceRequest;
import com.necpgame.gameplayservice.model.CharacterAttributes;
import com.necpgame.gameplayservice.model.CharacterExperience;
import com.necpgame.gameplayservice.model.CharacterSkills;
import com.necpgame.gameplayservice.model.Error;
import com.necpgame.gameplayservice.model.ExperienceAwardResult;
import com.necpgame.gameplayservice.model.GetProgressionMilestones200Response;
import com.necpgame.gameplayservice.model.LevelUpResult;
import com.necpgame.gameplayservice.model.SkillExperienceResult;
import com.necpgame.gameplayservice.model.SpendAttributePointsRequest;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for ProgressionService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface ProgressionService {

    /**
     * POST /progression/characters/{character_id}/skills/{skill_id}/experience : Добавить опыт навыку
     * Навык прокачивается использованием
     *
     * @param characterId  (required)
     * @param skillId  (required)
     * @param addSkillExperienceRequest  (required)
     * @return SkillExperienceResult
     */
    SkillExperienceResult addSkillExperience(UUID characterId, String skillId, AddSkillExperienceRequest addSkillExperienceRequest);

    /**
     * POST /progression/characters/{character_id}/experience/award : Выдать опыт персонажу
     * Используется backend системами (quest, combat) для выдачи опыта
     *
     * @param characterId  (required)
     * @param awardExperienceRequest  (required)
     * @return ExperienceAwardResult
     */
    ExperienceAwardResult awardExperience(UUID characterId, AwardExperienceRequest awardExperienceRequest);

    /**
     * GET /progression/characters/{character_id}/attributes : Получить атрибуты персонажа
     *
     * @param characterId  (required)
     * @return CharacterAttributes
     */
    CharacterAttributes getCharacterAttributes(UUID characterId);

    /**
     * GET /progression/characters/{character_id}/experience : Получить информацию об опыте персонажа
     *
     * @param characterId  (required)
     * @return CharacterExperience
     */
    CharacterExperience getCharacterExperience(UUID characterId);

    /**
     * GET /progression/characters/{character_id}/skills : Получить навыки персонажа
     *
     * @param characterId  (required)
     * @return CharacterSkills
     */
    CharacterSkills getCharacterSkills(UUID characterId);

    /**
     * GET /progression/characters/{character_id}/milestones : Получить прогрессионные вехи персонажа
     *
     * @param characterId  (required)
     * @return GetProgressionMilestones200Response
     */
    GetProgressionMilestones200Response getProgressionMilestones(UUID characterId);

    /**
     * POST /progression/characters/{character_id}/level-up : Повысить уровень персонажа
     * Автоматически вызывается когда достаточно опыта
     *
     * @param characterId  (required)
     * @return LevelUpResult
     */
    LevelUpResult levelUp(UUID characterId);

    /**
     * POST /progression/characters/{character_id}/attributes/spend : Потратить очки атрибутов
     *
     * @param characterId  (required)
     * @param spendAttributePointsRequest  (required)
     * @return CharacterAttributes
     */
    CharacterAttributes spendAttributePoints(UUID characterId, SpendAttributePointsRequest spendAttributePointsRequest);
}

