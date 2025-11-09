package com.necpgame.backjava.service;

import com.necpgame.backjava.model.CalculateCraftingChanceRequest;
import com.necpgame.backjava.model.CraftRequest;
import com.necpgame.backjava.model.CraftingCalculation;
import com.necpgame.backjava.model.CraftingRecipeDetailed;
import com.necpgame.backjava.model.CraftingResult;
import com.necpgame.backjava.model.CraftingSession;
import com.necpgame.backjava.model.GetCraftingStations200Response;
import com.necpgame.backjava.model.GetKnownRecipes200Response;
import com.necpgame.backjava.model.LearnRecipeRequest;
import com.necpgame.backjava.model.ListRecipes200Response;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for CraftingSystemService.
 * Generated from OpenAPI specification.
 *
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface CraftingSystemService {

    /**
     * POST /gameplay/economy/crafting/calculate : Рассчитать шанс успеха и время крафта
     * Для UI - показать перед крафтом
     *
     * @param calculateCraftingChanceRequest  (required)
     * @return CraftingCalculation
     */
    CraftingCalculation calculateCraftingChance(CalculateCraftingChanceRequest calculateCraftingChanceRequest);

    /**
     * POST /gameplay/economy/crafting/sessions/{session_id}/complete : Завершить крафтинг
     * Вызывается когда время истекло
     *
     * @param sessionId  (required)
     * @return CraftingResult
     */
    CraftingResult completeCrafting(UUID sessionId);

    /**
     * POST /gameplay/economy/crafting/craft : Крафтнуть предмет
     *
     * @param craftRequest  (required)
     * @return CraftingSession
     */
    CraftingSession craftItem(CraftRequest craftRequest);

    /**
     * GET /gameplay/economy/crafting/sessions/{session_id} : Получить статус крафтинга
     *
     * @param sessionId  (required)
     * @return CraftingSession
     */
    CraftingSession getCraftingSession(UUID sessionId);

    /**
     * GET /gameplay/economy/crafting/stations : Получить доступные крафтинг станции
     *
     * @return GetCraftingStations200Response
     */
    GetCraftingStations200Response getCraftingStations();

    /**
     * GET /gameplay/economy/crafting/character/{character_id}/known-recipes : Получить известные рецепты персонажа
     *
     * @param characterId  (required)
     * @param category  (optional)
     * @return GetKnownRecipes200Response
     */
    GetKnownRecipes200Response getKnownRecipes(UUID characterId, String category);

    /**
     * GET /gameplay/economy/crafting/recipes/{recipe_id} : Получить детали рецепта
     *
     * @param recipeId  (required)
     * @return CraftingRecipeDetailed
     */
    CraftingRecipeDetailed getRecipe(String recipeId);

    /**
     * POST /gameplay/economy/crafting/character/{character_id}/learn-recipe : Изучить новый рецепт
     *
     * @param characterId  (required)
     * @param learnRecipeRequest  (required)
     * @return Void
     */
    Void learnRecipe(UUID characterId, LearnRecipeRequest learnRecipeRequest);

    /**
     * GET /gameplay/economy/crafting/recipes : Получить список рецептов
     *
     * @param category  (optional)
     * @param tier Tier рецепта (optional)
     * @param minSkillLevel  (optional)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return ListRecipes200Response
     */
    ListRecipes200Response listRecipes(String category, String tier, Integer minSkillLevel, Integer page, Integer pageSize);
}

