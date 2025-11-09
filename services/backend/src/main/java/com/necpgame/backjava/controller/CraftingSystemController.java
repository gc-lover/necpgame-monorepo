package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.CraftingSystemApi;
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
import com.necpgame.backjava.service.CraftingSystemService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class CraftingSystemController implements CraftingSystemApi {

    private final CraftingSystemService craftingSystemService;

    @Override
    public ResponseEntity<CraftingCalculation> calculateCraftingChance(CalculateCraftingChanceRequest calculateCraftingChanceRequest) {
        log.info("POST /gameplay/economy/crafting/calculate [characterId={}, recipeId={}, stationId={}]",
            calculateCraftingChanceRequest != null ? calculateCraftingChanceRequest.getCharacterId() : null,
            calculateCraftingChanceRequest != null ? calculateCraftingChanceRequest.getRecipeId() : null,
            calculateCraftingChanceRequest != null && calculateCraftingChanceRequest.getStationId() != null && calculateCraftingChanceRequest.getStationId().isPresent()
                ? calculateCraftingChanceRequest.getStationId().get() : null);
        CraftingCalculation calculation = craftingSystemService.calculateCraftingChance(calculateCraftingChanceRequest);
        return ResponseEntity.ok(calculation);
    }

    @Override
    public ResponseEntity<CraftingResult> completeCrafting(UUID sessionId) {
        log.info("POST /gameplay/economy/crafting/sessions/{}/complete", sessionId);
        CraftingResult result = craftingSystemService.completeCrafting(sessionId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<CraftingSession> craftItem(CraftRequest craftRequest) {
        log.info("POST /gameplay/economy/crafting/craft [characterId={}, recipeId={}]",
            craftRequest != null ? craftRequest.getCharacterId() : null,
            craftRequest != null ? craftRequest.getRecipeId() : null);
        CraftingSession session = craftingSystemService.craftItem(craftRequest);
        return ResponseEntity.ok(session);
    }

    @Override
    public ResponseEntity<CraftingSession> getCraftingSession(UUID sessionId) {
        log.debug("GET /gameplay/economy/crafting/sessions/{}", sessionId);
        CraftingSession session = craftingSystemService.getCraftingSession(sessionId);
        return ResponseEntity.ok(session);
    }

    @Override
    public ResponseEntity<GetCraftingStations200Response> getCraftingStations() {
        log.debug("GET /gameplay/economy/crafting/stations");
        return ResponseEntity.ok(craftingSystemService.getCraftingStations());
    }

    @Override
    public ResponseEntity<GetKnownRecipes200Response> getKnownRecipes(UUID characterId, @Nullable String category) {
        log.debug("GET /gameplay/economy/crafting/character/{}/known-recipes [category={}]", characterId, category);
        GetKnownRecipes200Response response = craftingSystemService.getKnownRecipes(characterId, category);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CraftingRecipeDetailed> getRecipe(String recipeId) {
        log.debug("GET /gameplay/economy/crafting/recipes/{}", recipeId);
        CraftingRecipeDetailed recipe = craftingSystemService.getRecipe(recipeId);
        return ResponseEntity.ok(recipe);
    }

    @Override
    public ResponseEntity<Void> learnRecipe(UUID characterId, LearnRecipeRequest learnRecipeRequest) {
        log.info("POST /gameplay/economy/crafting/character/{}/learn-recipe [recipeId={}]", characterId,
            learnRecipeRequest != null ? learnRecipeRequest.getRecipeId() : null);
        craftingSystemService.learnRecipe(characterId, learnRecipeRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ListRecipes200Response> listRecipes(@Nullable String category, @Nullable String tier,
                                                               @Nullable Integer minSkillLevel, Integer page, Integer pageSize) {
        log.debug("GET /gameplay/economy/crafting/recipes [category={}, tier={}, minSkillLevel={}, page={}, pageSize={}]",
            category, tier, minSkillLevel, page, pageSize);
        ListRecipes200Response response = craftingSystemService.listRecipes(category, tier, minSkillLevel, page, pageSize);
        return ResponseEntity.ok(response);
    }
}
