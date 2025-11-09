package com.necpgame.backjava.service.impl;

import com.necpgame.backjava.entity.AccountEntity;
import com.necpgame.backjava.entity.CharacterEntity;
import com.necpgame.backjava.entity.CharacterEquipmentEntity;
import com.necpgame.backjava.entity.CharacterInventoryEntity;
import com.necpgame.backjava.entity.InventoryItemEntity;
import com.necpgame.backjava.model.CharacterInventory;
import com.necpgame.backjava.model.EquipItemRequest;
import com.necpgame.backjava.model.PickupItem200Response;
import com.necpgame.backjava.model.PickupItemRequest;
import com.necpgame.backjava.repository.CharacterEquipmentRepository;
import com.necpgame.backjava.repository.CharacterInventoryRepository;
import com.necpgame.backjava.repository.CharacterRepository;
import com.necpgame.backjava.repository.InventoryItemRepository;
import com.necpgame.backjava.repository.PlayerBankSlotRepository;
import com.necpgame.backjava.repository.PlayerRepository;
import com.necpgame.backjava.util.SecurityUtil;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private CharacterInventoryRepository characterInventoryRepository;
    @Mock
    private InventoryItemRepository inventoryItemRepository;
    @Mock
    private CharacterEquipmentRepository characterEquipmentRepository;
    @Mock
    private PlayerBankSlotRepository playerBankSlotRepository;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private MockedStatic<SecurityUtil> securityMock;

    @AfterEach
    void tearDown() {
        if (securityMock != null) {
            securityMock.close();
        }
    }

    @Test
    void getInventoryAggregatesBackpackItems() {
        UUID accountId = UUID.randomUUID();
        UUID characterId = UUID.randomUUID();
        String itemId = "item-1";

        securityMock = Mockito.mockStatic(SecurityUtil.class);
        securityMock.when(SecurityUtil::getCurrentAccountId).thenReturn(accountId);

        CharacterEntity character = new CharacterEntity();
        character.setId(characterId);
        AccountEntity account = new AccountEntity();
        account.setId(accountId);
        character.setAccount(account);

        CharacterInventoryEntity entry = new CharacterInventoryEntity();
        entry.setId(UUID.randomUUID());
        entry.setCharacterId(characterId);
        entry.setItemId(itemId);
        entry.setQuantity(2);
        entry.setSlotPosition(3);
        entry.setStorageType(CharacterInventoryEntity.StorageType.BACKPACK);

        InventoryItemEntity template = new InventoryItemEntity();
        template.setId(itemId);
        template.setName("Test Item");
        template.setWeight(BigDecimal.ONE);

        when(characterRepository.findByIdWithDetails(characterId)).thenReturn(Optional.of(character));
        when(characterInventoryRepository.findByCharacterIdAndStorageTypeOrderBySlotPosition(characterId, CharacterInventoryEntity.StorageType.BACKPACK))
            .thenReturn(Collections.singletonList(entry));
        when(characterInventoryRepository.calculateTotalWeight(eq(characterId), any()))
            .thenReturn(BigDecimal.valueOf(2));
        when(inventoryItemRepository.findById(itemId)).thenReturn(Optional.of(template));
        when(characterEquipmentRepository.findByCharacterId(characterId)).thenReturn(Collections.emptyList());

        CharacterInventory inventory = inventoryService.getInventory(characterId.toString());

        assertThat(inventory.getCharacterId()).isEqualTo(characterId.toString());
        assertThat(inventory.getSlotsTotal()).isEqualTo(50);
        assertThat(inventory.getSlotsUsed()).isEqualTo(1);
        assertThat(inventory.getWeightCurrent()).isEqualByComparingTo("2");
        assertThat(inventory.getItems()).hasSize(1);
        assertThat(inventory.getItems().get(0).getItemId()).isEqualTo(itemId);
        assertThat(inventory.getItems().get(0).getSlot()).isEqualTo(3);
    }

    @Test
    void pickupItemCreatesNewEntry() {
        UUID accountId = UUID.randomUUID();
        UUID characterId = UUID.randomUUID();
        String itemId = "item-2";

        securityMock = Mockito.mockStatic(SecurityUtil.class);
        securityMock.when(SecurityUtil::getCurrentAccountId).thenReturn(accountId);

        CharacterEntity character = new CharacterEntity();
        character.setId(characterId);
        AccountEntity account = new AccountEntity();
        account.setId(accountId);
        character.setAccount(account);

        InventoryItemEntity template = new InventoryItemEntity();
        template.setId(itemId);
        template.setName("Medkit");
        template.setWeight(BigDecimal.ONE);
        template.setStackable(true);
        template.setMaxStackSize(99);

        when(characterRepository.findByIdWithDetails(characterId)).thenReturn(Optional.of(character));
        when(characterInventoryRepository.calculateTotalWeight(eq(characterId), any()))
            .thenReturn(BigDecimal.ZERO);
        when(characterInventoryRepository.findByCharacterIdAndItemIdAndStorageType(characterId, itemId, CharacterInventoryEntity.StorageType.BACKPACK))
            .thenReturn(Collections.emptyList());
        when(characterInventoryRepository.findByCharacterIdAndStorageTypeOrderBySlotPosition(characterId, CharacterInventoryEntity.StorageType.BACKPACK))
            .thenReturn(Collections.emptyList());
        when(inventoryItemRepository.findById(itemId)).thenReturn(Optional.of(template));
        when(characterEquipmentRepository.findByCharacterId(characterId)).thenReturn(Collections.emptyList());
        when(characterInventoryRepository.save(any(CharacterInventoryEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PickupItemRequest request = new PickupItemRequest().itemId(itemId).quantity(1);
        PickupItem200Response response = inventoryService.pickupItem(characterId.toString(), request);

        assertThat(response.getSuccess()).isTrue();
        assertThat(response.getSlot()).isEqualTo(0);
        verify(characterInventoryRepository, times(1)).save(any(CharacterInventoryEntity.class));
    }

    @Test
    void equipItemMovesStackToEquipment() {
        UUID accountId = UUID.randomUUID();
        UUID characterId = UUID.randomUUID();
        String itemId = "weapon-1";

        securityMock = Mockito.mockStatic(SecurityUtil.class);
        securityMock.when(SecurityUtil::getCurrentAccountId).thenReturn(accountId);

        CharacterEntity character = new CharacterEntity();
        character.setId(characterId);
        AccountEntity account = new AccountEntity();
        account.setId(accountId);
        character.setAccount(account);

        CharacterInventoryEntity entry = new CharacterInventoryEntity();
        entry.setId(UUID.randomUUID());
        entry.setCharacterId(characterId);
        entry.setItemId(itemId);
        entry.setQuantity(1);
        entry.setSlotPosition(0);
        entry.setStorageType(CharacterInventoryEntity.StorageType.BACKPACK);

        InventoryItemEntity template = new InventoryItemEntity();
        template.setId(itemId);
        template.setName("Blade");
        template.setWeight(BigDecimal.ONE);
        template.setEquippable(true);

        when(characterRepository.findByIdWithDetails(characterId)).thenReturn(Optional.of(character));
        when(inventoryItemRepository.findById(itemId)).thenReturn(Optional.of(template));
        when(characterInventoryRepository.findByCharacterIdAndItemIdAndStorageType(characterId, itemId, CharacterInventoryEntity.StorageType.BACKPACK))
            .thenReturn(Collections.singletonList(entry));
        when(characterEquipmentRepository.findByCharacterIdAndSlotType(characterId, CharacterEquipmentEntity.SlotType.WEAPON_PRIMARY))
            .thenReturn(Optional.empty());
        when(characterEquipmentRepository.save(any(CharacterEquipmentEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(characterInventoryRepository.save(any(CharacterInventoryEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EquipItemRequest request = new EquipItemRequest()
            .itemId(itemId)
            .slotType("weapon_primary");

        Object response = inventoryService.equipItem(characterId.toString(), request);

        assertThat(entry.getStorageType()).isEqualTo(CharacterInventoryEntity.StorageType.EQUIPPED);
        assertThat(entry.getSlotPosition()).isNull();
        assertThat(response).isInstanceOf(Map.class);
        verify(characterEquipmentRepository, times(1)).save(any(CharacterEquipmentEntity.class));
        verify(characterInventoryRepository, never()).delete(any());
    }
}

