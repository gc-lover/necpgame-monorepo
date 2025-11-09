package com.necpgame.backjava.service;

import com.necpgame.backjava.model.CharacterInventory;
import com.necpgame.backjava.model.DropItemRequest;
import com.necpgame.backjava.model.EquipItemRequest;
import com.necpgame.backjava.model.PickupItem200Response;
import com.necpgame.backjava.model.PickupItemRequest;
import com.necpgame.backjava.model.UseItemRequest;

public interface InventoryService {

    CharacterInventory getInventory(String characterId);

    PickupItem200Response pickupItem(String characterId, PickupItemRequest request);

    Object dropItem(String characterId, DropItemRequest request);

    Object equipItem(String characterId, EquipItemRequest request);

    Object useItem(String characterId, UseItemRequest request);

    Object getEquipment(String characterId);

    Object getBankStorage(String playerId);
}


