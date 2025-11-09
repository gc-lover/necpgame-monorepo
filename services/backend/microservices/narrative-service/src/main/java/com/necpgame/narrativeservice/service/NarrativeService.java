package com.necpgame.narrativeservice.service;

import com.necpgame.narrativeservice.model.ChooseFloorApproach200Response;
import com.necpgame.narrativeservice.model.ChooseFloorApproachRequest;
import com.necpgame.narrativeservice.model.CompleteCorpoTowerRaidRequest;
import com.necpgame.narrativeservice.model.CorpoTowerRaidStatus;
import com.necpgame.narrativeservice.model.CorpoTowerRequirements;
import com.necpgame.narrativeservice.model.RaidCompletion;
import com.necpgame.narrativeservice.model.StartCEOFight200Response;
import com.necpgame.narrativeservice.model.StartCEOFightRequest;
import com.necpgame.narrativeservice.model.StartCorpoTowerRaid200Response;
import com.necpgame.narrativeservice.model.StartCorpoTowerRaidRequest;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for NarrativeService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface NarrativeService {

    /**
     * GET /narrative/raids/corpo-tower/requirements : Проверить требования для рейда
     * Проверяет требования для доступа к рейду. Уровень, Gear Score, завершенные квесты, выбор стороны. 
     *
     * @param characterId  (required)
     * @return CorpoTowerRequirements
     */
    CorpoTowerRequirements checkCorpoTowerRequirements(String characterId);

    /**
     * POST /narrative/raids/corpo-tower/{raid_id}/floor/{floor_number}/approach : Выбрать подход к этажу
     * Выбирает подход к текущему этажу. Stealth (скрытное проникновение) или Combat (прямой бой). 
     *
     * @param raidId  (required)
     * @param floorNumber  (required)
     * @param chooseFloorApproachRequest  (required)
     * @return ChooseFloorApproach200Response
     */
    ChooseFloorApproach200Response chooseFloorApproach(String raidId, Integer floorNumber, ChooseFloorApproachRequest chooseFloorApproachRequest);

    /**
     * POST /narrative/raids/corpo-tower/{raid_id}/complete : Завершить рейд
     * Завершает рейд после победы над CEO. Распределяет награды. 
     *
     * @param raidId  (required)
     * @param completeCorpoTowerRaidRequest  (required)
     * @return RaidCompletion
     */
    RaidCompletion completeCorpoTowerRaid(String raidId, CompleteCorpoTowerRaidRequest completeCorpoTowerRaidRequest);

    /**
     * GET /narrative/raids/corpo-tower/{raid_id}/status : Получить статус рейда
     * Возвращает текущий статус рейда. Фаза, прогресс, состояние участников, текущий этаж. 
     *
     * @param raidId  (required)
     * @return CorpoTowerRaidStatus
     */
    CorpoTowerRaidStatus getCorpoTowerRaidStatus(String raidId);

    /**
     * POST /narrative/raids/corpo-tower/{raid_id}/ceo-fight : Начать бой с CEO
     * Начинает финальный босс-файт с CEO корпорации. Требует завершения всех предыдущих фаз. 
     *
     * @param raidId  (required)
     * @param startCEOFightRequest  (required)
     * @return StartCEOFight200Response
     */
    StartCEOFight200Response startCEOFight(String raidId, StartCEOFightRequest startCEOFightRequest);

    /**
     * POST /narrative/raids/corpo-tower/start : Начать рейд Corpo Tower Assault
     * Начинает рейд для группы. Требует выбор корпорации (Arasaka или Militech). 
     *
     * @param startCorpoTowerRaidRequest  (required)
     * @return StartCorpoTowerRaid200Response
     */
    StartCorpoTowerRaid200Response startCorpoTowerRaid(StartCorpoTowerRaidRequest startCorpoTowerRaidRequest);
}

