package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.InventoryApi;
import com.necpgame.backjava.model.CharacterInventory;
import com.necpgame.backjava.model.DropItemRequest;
import com.necpgame.backjava.model.EquipItemRequest;
import com.necpgame.backjava.model.PickupItem200Response;
import com.necpgame.backjava.model.PickupItemRequest;
import com.necpgame.backjava.model.UseItemRequest;
import com.necpgame.backjava.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class InventoryController implements InventoryApi {

    private final InventoryService inventoryService;

    @Override
    public ResponseEntity<CharacterInventory> getInventory(String characterId) {
        log.info("GET /inventory/{}", characterId);
        return ResponseEntity.ok(inventoryService.getInventory(characterId));
    }

    @Override
    public ResponseEntity<PickupItem200Response> pickupItem(String characterId, PickupItemRequest pickupItemRequest) {
        log.info("POST /inventory/{}/pickup", characterId);
        return ResponseEntity.ok(inventoryService.pickupItem(characterId, pickupItemRequest));
    }

    @Override
    public ResponseEntity<Object> dropItem(String characterId, DropItemRequest dropItemRequest) {
        log.info("POST /inventory/{}/drop", characterId);
        return ResponseEntity.ok(inventoryService.dropItem(characterId, dropItemRequest));
    }

    @Override
    public ResponseEntity<Object> useItem(String characterId, UseItemRequest useItemRequest) {
        log.info("POST /inventory/{}/use", characterId);
        return ResponseEntity.ok(inventoryService.useItem(characterId, useItemRequest));
    }

    @Override
    public ResponseEntity<Object> equipItem(String characterId, EquipItemRequest equipItemRequest) {
        log.info("POST /inventory/{}/equip", characterId);
        return ResponseEntity.ok(inventoryService.equipItem(characterId, equipItemRequest));
    }

    @Override
    public ResponseEntity<Object> getEquipment(String characterId) {
        log.info("GET /inventory/{}/equipment", characterId);
        return ResponseEntity.ok(inventoryService.getEquipment(characterId));
    }

    @Override
    public ResponseEntity<Object> getBankStorage(String playerId) {
        log.info("GET /inventory/bank/{}", playerId);
        return ResponseEntity.ok(inventoryService.getBankStorage(playerId));
    }
}


