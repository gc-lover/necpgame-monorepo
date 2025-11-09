package com.necpgame.gameplayservice.service;

import com.necpgame.gameplayservice.model.CombatResult;
import com.necpgame.gameplayservice.model.CombatState;
import com.necpgame.gameplayservice.model.FleeCombat200Response;
import com.necpgame.gameplayservice.model.FleeCombatRequest;
import com.necpgame.gameplayservice.model.GetAvailableActions200Response;
import com.necpgame.gameplayservice.model.InitiateCombatRequest;
import org.springframework.lang.Nullable;
import com.necpgame.gameplayservice.model.PerformCombatActionRequest;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for CombatService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface CombatService {

    /**
     * POST /combat/{combatId}/flee : Сбежать из боя
     *
     * @param combatId  (required)
     * @param fleeCombatRequest  (optional)
     * @return FleeCombat200Response
     */
    FleeCombat200Response fleeCombat(UUID combatId, FleeCombatRequest fleeCombatRequest);

    /**
     * GET /combat/{combatId}/available-actions : Доступные действия
     *
     * @param combatId  (required)
     * @param characterId  (required)
     * @return GetAvailableActions200Response
     */
    GetAvailableActions200Response getAvailableActions(UUID combatId, UUID characterId);

    /**
     * GET /combat/{combatId}/result : Результат боя
     *
     * @param combatId  (required)
     * @return CombatResult
     */
    CombatResult getCombatResult(UUID combatId);

    /**
     * GET /combat/{combatId} : Состояние боя
     *
     * @param combatId  (required)
     * @return CombatState
     */
    CombatState getCombatState(UUID combatId);

    /**
     * POST /combat/initiate : Начать бой
     *
     * @param initiateCombatRequest  (optional)
     * @return CombatState
     */
    CombatState initiateCombat(InitiateCombatRequest initiateCombatRequest);

    /**
     * POST /combat/{combatId}/action : Выполнить действие в бою
     *
     * @param combatId  (required)
     * @param performCombatActionRequest  (optional)
     * @return CombatState
     */
    CombatState performCombatAction(UUID combatId, PerformCombatActionRequest performCombatActionRequest);
}

