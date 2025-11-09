package com.necpgame.backjava.service.impl;

import com.necpgame.backjava.model.CharacterInventory;
import com.necpgame.backjava.model.DropItemRequest;
import com.necpgame.backjava.model.EquipItemRequest;
import com.necpgame.backjava.model.PickupItem200Response;
import com.necpgame.backjava.model.PickupItemRequest;
import com.necpgame.backjava.model.UseItemRequest;
import com.necpgame.backjava.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {

    @Override
    public CharacterInventory getInventory(String characterId) {
        throw new UnsupportedOperationException("Inventory service is not implemented yet");
    }

    @Override
    public PickupItem200Response pickupItem(String characterId, PickupItemRequest request) {
        throw new UnsupportedOperationException("Inventory service is not implemented yet");
    }

    @Override
    public Object dropItem(String characterId, DropItemRequest request) {
        throw new UnsupportedOperationException("Inventory service is not implemented yet");
    }

    @Override
    public Object equipItem(String characterId, EquipItemRequest request) {
        throw new UnsupportedOperationException("Inventory service is not implemented yet");
    }

    @Override
    public Object useItem(String characterId, UseItemRequest request) {
        throw new UnsupportedOperationException("Inventory service is not implemented yet");
    }

    @Override
    public Object getEquipment(String characterId) {
        throw new UnsupportedOperationException("Inventory service is not implemented yet");
    }

    @Override
    public Object getBankStorage(String playerId) {
        throw new UnsupportedOperationException("Inventory service is not implemented yet");
    }
}

