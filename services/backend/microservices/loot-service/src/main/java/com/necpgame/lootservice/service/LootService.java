package com.necpgame.lootservice.service;

import com.necpgame.lootservice.model.GenerateLootRequest;
import com.necpgame.lootservice.model.GeneratedLoot;
import com.necpgame.lootservice.model.GetRollResult200Response;
import com.necpgame.lootservice.model.LootDrop;
import com.necpgame.lootservice.model.LootItem200Response;
import com.necpgame.lootservice.model.LootItemRequest;
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
     * POST /loot/generate : Сгенерировать лут
     * Генерирует лут из loot table. Используется при убийстве NPC, открытии контейнера. 
     *
     * @param generateLootRequest  (required)
     * @return GeneratedLoot
     */
    GeneratedLoot generateLoot(GenerateLootRequest generateLootRequest);

    /**
     * GET /loot/drops/{drop_id} : Получить информацию о дропе
     * Возвращает информацию о выпавшем луте. Для отображения в мире. 
     *
     * @param dropId  (required)
     * @return LootDrop
     */
    LootDrop getLootDrop(String dropId);

    /**
     * GET /loot/rolls/{roll_id}/result : Получить результат ролла
     * Возвращает результат ролла (после 60s или когда все проголосовали). Winner получает предмет. 
     *
     * @param rollId  (required)
     * @return GetRollResult200Response
     */
    GetRollResult200Response getRollResult(String rollId);

    /**
     * POST /loot/drops/{drop_id}/loot : Залутить предмет
     * Забирает предмет из дропа. Personal loot - мгновенно. Shared loot - начинает roll. 
     *
     * @param dropId  (required)
     * @param lootItemRequest  (required)
     * @return LootItem200Response
     */
    LootItem200Response lootItem(String dropId, LootItemRequest lootItemRequest);
}

