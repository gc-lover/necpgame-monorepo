package com.necpgame.inventoryservice.service;

import com.necpgame.inventoryservice.model.DropItem200Response;
import com.necpgame.inventoryservice.model.EquipItem200Response;
import com.necpgame.inventoryservice.model.EquipRequest;
import com.necpgame.inventoryservice.model.Error;
import com.necpgame.inventoryservice.model.GetEquipment200Response;
import com.necpgame.inventoryservice.model.InventoryResponse;
import com.necpgame.inventoryservice.model.ItemCategory;
import org.springframework.lang.Nullable;
import java.util.UUID;
import com.necpgame.inventoryservice.model.UnequipItem200Response;
import com.necpgame.inventoryservice.model.UnequipItemRequest;
import com.necpgame.inventoryservice.model.UseItem200Response;
import com.necpgame.inventoryservice.model.UseItemRequest;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for InventoryService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface InventoryService {

    /**
     * DELETE /inventory/drop : Выбросить предмет
     * Удаляет предмет из инвентаря персонажа (выбрасывает).  **Бизнес-логика:** - Проверяет наличие предмета в инвентаре - Проверяет, что предмет не экипирован - Проверяет, что предмет не квестовый (нельзя выбросить) - Удаляет предмет (или уменьшает количество, если stackable)  **Источник:** &#x60;.BRAIN/05-technical/ui-main-game.md&#x60; (Раздел 3.1) 
     *
     * @param characterId ID персонажа (required)
     * @param itemId ID предмета для выбрасывания (required)
     * @param quantity Количество предметов для выбрасывания (для stackable) (optional, default to 1)
     * @return DropItem200Response
     */
    DropItem200Response dropItem(UUID characterId, String itemId, Integer quantity);

    /**
     * POST /inventory/equip : Экипировать предмет
     * Экипирует предмет из инвентаря в соответствующий слот экипировки.  **Бизнес-логика:** - Проверяет наличие предмета в инвентаре - Проверяет требования для экипировки (уровень, характеристики) - Если слот занят - автоматически снимает текущий предмет - Применяет бонусы от нового предмета  **Источник:** &#x60;.BRAIN/05-technical/ui-main-game.md&#x60; (Раздел 3.1) 
     *
     * @param equipRequest  (required)
     * @return EquipItem200Response
     */
    EquipItem200Response equipItem(EquipRequest equipRequest);

    /**
     * GET /inventory/equipment : Получить экипировку персонажа
     * Возвращает текущую экипировку персонажа (все слоты с предметами).  **Бизнес-логика:** - Возвращает все слоты экипировки - Показывает какие слоты заняты и какие свободны - Возвращает бонусы от экипированных предметов  **Источник:** &#x60;.BRAIN/05-technical/ui-main-game.md&#x60; (Раздел 3.1) 
     *
     * @param characterId ID персонажа (required)
     * @return GetEquipment200Response
     */
    GetEquipment200Response getEquipment(UUID characterId);

    /**
     * GET /inventory : Получить инвентарь персонажа
     * Возвращает инвентарь персонажа со всеми предметами, весом и лимитом.  **Бизнес-логика:** - Возвращает все предметы в инвентаре - Группирует предметы по категориям - Складывает stackable предметы - Показывает текущий вес и лимит  **Источник:** &#x60;.BRAIN/05-technical/ui-main-game.md&#x60; (Раздел 3.1) 
     *
     * @param characterId ID персонажа (required)
     * @param category Фильтр по категории предметов (optional)
     * @return InventoryResponse
     */
    InventoryResponse getInventory(UUID characterId, ItemCategory category);

    /**
     * POST /inventory/unequip : Снять экипированный предмет
     * Снимает предмет из слота экипировки и помещает в инвентарь.  **Бизнес-логика:** - Проверяет, что слот занят - Проверяет наличие места в инвентаре (вес) - Снимает бонусы от предмета - Помещает предмет в инвентарь  **Источник:** &#x60;.BRAIN/05-technical/ui-main-game.md&#x60; (Раздел 3.1) 
     *
     * @param unequipItemRequest  (required)
     * @return UnequipItem200Response
     */
    UnequipItem200Response unequipItem(UnequipItemRequest unequipItemRequest);

    /**
     * POST /inventory/use : Использовать предмет
     * Использует предмет из инвентаря (медикаменты, расходники).  **Бизнес-логика:** - Проверяет наличие предмета в инвентаре - Проверяет, что предмет можно использовать - Применяет эффекты от предмета - Уменьшает количество на 1 (или удаляет, если был последний)  **Источник:** &#x60;.BRAIN/05-technical/ui-main-game.md&#x60; (Раздел 3.1) 
     *
     * @param useItemRequest  (required)
     * @return UseItem200Response
     */
    UseItem200Response useItem(UseItemRequest useItemRequest);
}

