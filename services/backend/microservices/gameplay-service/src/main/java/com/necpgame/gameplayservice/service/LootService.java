package com.necpgame.gameplayservice.service;

import com.necpgame.gameplayservice.model.ActiveRollsResponse;
import com.necpgame.gameplayservice.model.AdminRerollRequest;
import com.necpgame.gameplayservice.model.BadLuckProtection;
import java.math.BigDecimal;
import com.necpgame.gameplayservice.model.BossLootDistributeRequest;
import com.necpgame.gameplayservice.model.BossLootDistributeResponse;
import com.necpgame.gameplayservice.model.BossLootInfo;
import com.necpgame.gameplayservice.model.DuplicateCheckRequest;
import com.necpgame.gameplayservice.model.DuplicateCheckResponse;
import com.necpgame.gameplayservice.model.Error;
import com.necpgame.gameplayservice.model.LootAdvancedError;
import com.necpgame.gameplayservice.model.LootClaimRequest;
import com.necpgame.gameplayservice.model.LootDropsResponse;
import com.necpgame.gameplayservice.model.LootHistoryResponse;
import com.necpgame.gameplayservice.model.LootRoll;
import com.necpgame.gameplayservice.model.LootSettingsResponse;
import com.necpgame.gameplayservice.model.LootSettingsUpdateRequest;
import org.springframework.lang.Nullable;
import com.necpgame.gameplayservice.model.RollResolveRequest;
import com.necpgame.gameplayservice.model.RollSubmissionRequest;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for LootService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface LootService {

    /**
     * POST /loot/admin/reroll : Административное перераспределение лута
     *
     * @param adminRerollRequest  (required)
     * @return Void
     */
    Void lootAdminRerollPost(AdminRerollRequest adminRerollRequest);

    /**
     * GET /loot/bad-luck : Статус bad luck protection
     *
     * @return BadLuckProtection
     */
    BadLuckProtection lootBadLuckGet();

    /**
     * POST /loot/boss/{bossId}/distribute : Распределить босcовый лут
     *
     * @param bossId Идентификатор босса или encounter (required)
     * @param bossLootDistributeRequest  (required)
     * @return BossLootDistributeResponse
     */
    BossLootDistributeResponse lootBossBossIdDistributePost(String bossId, BossLootDistributeRequest bossLootDistributeRequest);

    /**
     * GET /loot/boss/{bossId} : Получить информацию о босcовом луте
     *
     * @param bossId Идентификатор босса или encounter (required)
     * @return BossLootInfo
     */
    BossLootInfo lootBossBossIdGet(String bossId);

    /**
     * POST /loot/drops/{dropId}/claim : Инициировать распределение дропа
     *
     * @param dropId Идентификатор дропа (required)
     * @param lootClaimRequest  (required)
     * @return LootRoll
     */
    LootRoll lootDropsDropIdClaimPost(String dropId, LootClaimRequest lootClaimRequest);

    /**
     * GET /loot/drops/nearby : Получить доступные поблизости дропы
     *
     * @param partyId  (optional)
     * @param radius  (optional, default to 30)
     * @return LootDropsResponse
     */
    LootDropsResponse lootDropsNearbyGet(String partyId, BigDecimal radius);

    /**
     * POST /loot/duplicate-check : Проверка дубликатов перед выдачей
     *
     * @param duplicateCheckRequest  (required)
     * @return DuplicateCheckResponse
     */
    DuplicateCheckResponse lootDuplicateCheckPost(DuplicateCheckRequest duplicateCheckRequest);

    /**
     * GET /loot/history : История полученного лута и роллов
     *
     * @param source  (optional)
     * @param rarity  (optional)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return LootHistoryResponse
     */
    LootHistoryResponse lootHistoryGet(String source, String rarity, Integer page, Integer pageSize);

    /**
     * GET /loot/rolls/active : Список активных роллов игрока
     *
     * @return ActiveRollsResponse
     */
    ActiveRollsResponse lootRollsActiveGet();

    /**
     * GET /loot/rolls/{rollId} : Получить состояние ролла
     *
     * @param rollId Идентификатор ролла (required)
     * @return LootRoll
     */
    LootRoll lootRollsRollIdGet(String rollId);

    /**
     * POST /loot/rolls/{rollId}/resolve : Завершить ролл принудительно
     *
     * @param rollId Идентификатор ролла (required)
     * @param rollResolveRequest  (optional)
     * @return Void
     */
    Void lootRollsRollIdResolvePost(String rollId, RollResolveRequest rollResolveRequest);

    /**
     * POST /loot/rolls/{rollId}/submit : Отправить ставку Need/Greed/Pass
     *
     * @param rollId Идентификатор ролла (required)
     * @param rollSubmissionRequest  (required)
     * @return Void
     */
    Void lootRollsRollIdSubmitPost(String rollId, RollSubmissionRequest rollSubmissionRequest);

    /**
     * GET /loot/settings : Получить настройки smart loot и autoloot
     *
     * @return LootSettingsResponse
     */
    LootSettingsResponse lootSettingsGet();

    /**
     * PUT /loot/settings : Обновить настройки smart loot и autoloot
     *
     * @param lootSettingsUpdateRequest  (required)
     * @return Void
     */
    Void lootSettingsPut(LootSettingsUpdateRequest lootSettingsUpdateRequest);
}

