package com.necpgame.workqueue.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.workqueue.domain.AgentEntity;
import com.necpgame.workqueue.domain.content.ContentEntryEntity;
import com.necpgame.workqueue.domain.item.ArmorStatsEntity;
import com.necpgame.workqueue.domain.item.ConsumableEffectEntity;
import com.necpgame.workqueue.domain.item.ItemComponentRequirementEntity;
import com.necpgame.workqueue.domain.item.ItemDataEntity;
import com.necpgame.workqueue.domain.item.ItemModSlotEntity;
import com.necpgame.workqueue.domain.item.WeaponStatsEntity;
import com.necpgame.workqueue.domain.reference.EnumValueEntity;
import com.necpgame.workqueue.repository.ContentEntryRepository;
import com.necpgame.workqueue.repository.item.ArmorStatsRepository;
import com.necpgame.workqueue.repository.item.ConsumableEffectRepository;
import com.necpgame.workqueue.repository.item.ItemComponentRequirementRepository;
import com.necpgame.workqueue.repository.item.ItemDataRepository;
import com.necpgame.workqueue.repository.item.ItemModSlotRepository;
import com.necpgame.workqueue.repository.item.WeaponStatsRepository;
import com.necpgame.workqueue.security.AgentPrincipal;
import com.necpgame.workqueue.service.exception.ApiErrorDetail;
import com.necpgame.workqueue.service.exception.ApiErrorException;
import com.necpgame.workqueue.service.model.ContentTaskContext;
import com.necpgame.workqueue.web.dto.content.EnumValueDto;
import com.necpgame.workqueue.web.dto.item.ItemCommandRequestDto;
import com.necpgame.workqueue.web.dto.item.ItemDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Locale;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class ItemCommandService {
    private static final String REQUIREMENT = "policy:content";
    private final ContentEntryRepository contentEntryRepository;
    private final ItemDataRepository itemDataRepository;
    private final WeaponStatsRepository weaponStatsRepository;
    private final ArmorStatsRepository armorStatsRepository;
    private final ConsumableEffectRepository consumableEffectRepository;
    private final ItemModSlotRepository itemModSlotRepository;
    private final ItemComponentRequirementRepository componentRequirementRepository;
    private final EnumLookupService enumLookupService;
    private final AgentDirectoryService agentDirectoryService;
    private final ActivityLogService activityLogService;
    private final ContentQueryService contentQueryService;
    private final ObjectMapper objectMapper;
    private final ContentTaskCoordinator contentTaskCoordinator;

    @Transactional
    public ItemDetailDto create(AgentPrincipal principal, ItemCommandRequestDto request) {
        ContentEntryEntity content = requireItemContent(request.contentCode());
        if (itemDataRepository.findById(content.getId()).isPresent()) {
            throw conflict("item.exists", "Для данного контента уже есть типизированные данные");
        }
        AgentEntity actor = agentDirectoryService.requireAgent(principal.id());
        ItemDataEntity data = new ItemDataEntity();
        data.setEntity(content);
        applyTopLevel(data, request);
        itemDataRepository.save(data);
        syncWeapon(content, request.weapon());
        syncArmor(content, request.armor());
        replaceCollections(content, request);
        recordEvent(actor, content, "item.created");
        return detail(content.getId());
    }

    @Transactional
    public ItemDetailDto update(AgentPrincipal principal, ItemCommandRequestDto request) {
        ContentEntryEntity content = requireItemContent(request.contentCode());
        ItemDataEntity data = itemDataRepository.findById(content.getId())
                .orElseThrow(() -> conflict("item.missing", "Сначала создайте запись для предмета"));
        AgentEntity actor = agentDirectoryService.requireAgent(principal.id());
        applyTopLevel(data, request);
        itemDataRepository.save(data);
        syncWeapon(content, request.weapon());
        syncArmor(content, request.armor());
        replaceCollections(content, request);
        recordEvent(actor, content, "item.updated");
        return detail(content.getId());
    }

    @Transactional(readOnly = true)
    public ItemDetailDto detail(UUID contentId) {
        var contentDetail = contentQueryService.getDetail(contentId);
        ItemDataEntity data = itemDataRepository.findById(contentId)
                .orElseThrow(() -> notFound("item.not_found", "Предмет не найден"));
        WeaponStatsEntity weapon = weaponStatsRepository.findById(contentId).orElse(null);
        ArmorStatsEntity armor = armorStatsRepository.findById(contentId).orElse(null);
        List<ConsumableEffectEntity> effects = consumableEffectRepository.findByItem_Id(contentId);
        List<ItemModSlotEntity> modSlots = itemModSlotRepository.findByItem_Id(contentId);
        List<ItemComponentRequirementEntity> components = componentRequirementRepository.findByItem_Id(contentId);

        return new ItemDetailDto(
                contentDetail.summary(),
                toEnum(data.getCategory()),
                toEnum(data.getSlot()),
                toEnum(data.getRarity()),
                toEnum(data.getBindType()),
                data.getWeight(),
                data.getLevelRequirement(),
                data.getStackSize(),
                data.getVendorPrice(),
                data.getDurabilityMax(),
                data.isTradeable(),
                data.getPowerScore(),
                readMap(data.getMetadata()),
                weapon == null ? null : new ItemDetailDto.WeaponStatsDetail(
                        toEnum(weapon.getWeaponClass()),
                        toEnum(weapon.getDamageType()),
                        weapon.getDamageMin(),
                        weapon.getDamageMax(),
                        weapon.getFireRate(),
                        weapon.getMagazineSize(),
                        weapon.getReloadTimeSeconds(),
                        weapon.getRangeMin(),
                        weapon.getRangeMax(),
                        weapon.getCriticalChance(),
                        weapon.getCriticalMultiplier(),
                        weapon.getAccuracy(),
                        weapon.getRecoil(),
                        readMap(weapon.getMetadata())
                ),
                armor == null ? null : new ItemDetailDto.ArmorStatsDetail(
                        toEnum(armor.getArmorType()),
                        armor.getArmorValue(),
                        readMap(armor.getResistances()),
                        armor.getMobilityPenalty(),
                        readMap(armor.getMetadata())
                ),
                effects.stream()
                        .map(effect -> new ItemDetailDto.ConsumableEffectDetail(
                                effect.getId(),
                                effect.getEffectEntity() == null ? null : effect.getEffectEntity().getId(),
                                effect.getDurationSeconds(),
                                effect.getCooldownSeconds(),
                                readMap(effect.getMetadata())
                        ))
                        .toList(),
                modSlots.stream()
                        .map(slot -> new ItemDetailDto.ItemModSlotDetail(
                                slot.getId(),
                                slot.getSlotCode(),
                                slot.getCapacity(),
                                readMap(slot.getMetadata())
                        ))
                        .toList(),
                components.stream()
                        .map(component -> new ItemDetailDto.ComponentRequirementDetail(
                                component.getId(),
                                component.getComponentItem().getId(),
                                component.getQuantity(),
                                component.getNotes()
                        ))
                        .toList()
        );
    }

    private void applyTopLevel(ItemDataEntity entity, ItemCommandRequestDto request) {
        entity.setCategory(resolveEnum("item_category", request.categoryCode()));
        entity.setSlot(resolveEnum("item_slot", request.slotCode()));
        entity.setRarity(resolveEnumRequired("rarity", request.rarityCode(), "rarityCode"));
        entity.setBindType(resolveEnumRequired("bind_type", request.bindTypeCode(), "bindTypeCode"));
        entity.setTradeable(request.tradeable() == null || request.tradeable());
        entity.setWeight(request.weight());
        entity.setLevelRequirement(request.levelRequirement());
        entity.setStackSize(request.stackSize());
        entity.setVendorPrice(request.vendorPrice());
        entity.setDurabilityMax(request.durabilityMax());
        entity.setPowerScore(request.powerScore());
        entity.setMetadata(writeJson(request.metadata(), "metadata"));
    }

    private void syncWeapon(ContentEntryEntity content, ItemCommandRequestDto.WeaponStatsDto dto) {
        if (dto == null) {
            weaponStatsRepository.findById(content.getId()).ifPresent(weaponStatsRepository::delete);
            return;
        }
        WeaponStatsEntity entity = weaponStatsRepository.findById(content.getId()).orElseGet(() -> {
            WeaponStatsEntity created = new WeaponStatsEntity();
            created.setItem(content);
            return created;
        });
        entity.setWeaponClass(resolveEnum("weapon_class", dto.weaponClassCode()));
        entity.setDamageType(resolveEnum("damage_type", dto.damageTypeCode()));
        entity.setDamageMin(dto.damageMin());
        entity.setDamageMax(dto.damageMax());
        entity.setFireRate(dto.fireRate());
        entity.setMagazineSize(dto.magazineSize());
        entity.setReloadTimeSeconds(dto.reloadTimeSeconds());
        entity.setRangeMin(dto.rangeMin());
        entity.setRangeMax(dto.rangeMax());
        entity.setCriticalChance(dto.criticalChance());
        entity.setCriticalMultiplier(dto.criticalMultiplier());
        entity.setAccuracy(dto.accuracy());
        entity.setRecoil(dto.recoil());
        entity.setMetadata(writeJson(dto.metadata(), "weapon.metadata"));
        weaponStatsRepository.save(entity);
    }

    private void syncArmor(ContentEntryEntity content, ItemCommandRequestDto.ArmorStatsDto dto) {
        if (dto == null) {
            armorStatsRepository.findById(content.getId()).ifPresent(armorStatsRepository::delete);
            return;
        }
        ArmorStatsEntity entity = armorStatsRepository.findById(content.getId()).orElseGet(() -> {
            ArmorStatsEntity created = new ArmorStatsEntity();
            created.setItem(content);
            return created;
        });
        entity.setArmorType(resolveEnum("armor_type", dto.armorTypeCode()));
        entity.setArmorValue(dto.armorValue());
        entity.setResistances(writeJson(dto.resistances(), "armor.resistances"));
        entity.setMobilityPenalty(dto.mobilityPenalty());
        entity.setMetadata(writeJson(dto.metadata(), "armor.metadata"));
        armorStatsRepository.save(entity);
    }

    private void replaceCollections(ContentEntryEntity content, ItemCommandRequestDto request) {
        UUID itemId = content.getId();
        consumableEffectRepository.deleteByItem_Id(itemId);
        consumableEffectRepository.flush();
        if (request.consumableEffects() != null) {
            request.consumableEffects().forEach(dto -> {
                ConsumableEffectEntity entity = new ConsumableEffectEntity();
                entity.setItem(content);
                entity.setEffectEntity(requireContent(dto.effectEntityId()));
                entity.setDurationSeconds(dto.durationSeconds());
                entity.setCooldownSeconds(dto.cooldownSeconds());
                entity.setMetadata(writeJson(dto.metadata(), "consumable.metadata"));
                consumableEffectRepository.save(entity);
            });
        }

        itemModSlotRepository.deleteByItem_Id(itemId);
        itemModSlotRepository.flush();
        if (request.modSlots() != null) {
            request.modSlots().forEach(dto -> {
                ItemModSlotEntity entity = new ItemModSlotEntity();
                entity.setItem(content);
                entity.setSlotCode(dto.slotCode().trim().toLowerCase(Locale.ROOT));
                entity.setCapacity(dto.capacity());
                entity.setMetadata(writeJson(dto.metadata(), "modslot.metadata"));
                itemModSlotRepository.save(entity);
            });
        }

        componentRequirementRepository.deleteByItem_Id(itemId);
        componentRequirementRepository.flush();
        if (request.componentRequirements() != null) {
            request.componentRequirements().forEach(dto -> {
                ItemComponentRequirementEntity entity = new ItemComponentRequirementEntity();
                entity.setItem(content);
                entity.setComponentItem(requireContent(dto.componentItemId()));
                entity.setQuantity(dto.quantity());
                entity.setNotes(dto.notes());
                componentRequirementRepository.save(entity);
            });
        }
    }

    private void recordEvent(AgentEntity actor, ContentEntryEntity content, String eventType) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("contentId", content.getId());
        payload.put("contentCode", content.getCode());
        activityLogService.recordContentEvent(actor, content, eventType, payload);
        contentTaskCoordinator.enqueueContentChange(
                actor,
                content,
                new ContentTaskContext(
                        eventType,
                        "Работа над предметом готова для следующего сегмента",
                        List.of(
                                "/api/content/entities/" + content.getId(),
                                "/api/items/" + content.getId()
                        ),
                        Map.of(
                                "domain", "items",
                                "operation", eventType.endsWith(".created") ? "create" : "update"
                        ),
                        eventType.endsWith(".created") ? 4 : 3
                )
        );
    }

    private ContentEntryEntity requireItemContent(String code) {
        ContentEntryEntity entity = contentEntryRepository.findByCodeIgnoreCase(code)
                .orElseThrow(() -> notFound("content.not_found", "Контент не найден"));
        if (entity.getEntityType() == null || !"item".equalsIgnoreCase(entity.getEntityType().getCode())) {
            throw new ApiErrorException(
                    HttpStatus.BAD_REQUEST,
                    "item.invalid_type",
                    "Контент должен иметь тип item",
                    List.of(REQUIREMENT),
                    List.of(new ApiErrorDetail("contentCode", "Укажите контент типа item"))
            );
        }
        return entity;
    }

    private ContentEntryEntity requireContent(UUID id) {
        return contentEntryRepository.findById(id)
                .orElseThrow(() -> notFound("content.not_found", "Связанный контент не найден"));
    }

    private EnumValueEntity resolveEnum(String group, String code) {
        if (code == null || code.isBlank()) {
            return null;
        }
        return enumLookupService.require(group, code);
    }

    private EnumValueEntity resolveEnumRequired(String group, String code, String field) {
        if (code == null || code.isBlank()) {
            throw new ApiErrorException(
                    HttpStatus.BAD_REQUEST,
                    "item.missing_enum",
                    "Поле " + field + " обязательно",
                    List.of(REQUIREMENT),
                    List.of(new ApiErrorDetail(field, "Укажите значение"))
            );
        }
        return enumLookupService.require(group, code);
    }

    private Map<String, Object> readMap(String json) {
        if (json == null || json.isBlank()) {
            return Map.of();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            return Map.of();
        }
    }

    private String writeJson(Map<String, Object> source, String field) {
        Map<String, Object> safe = source == null ? Map.of() : source;
        try {
            return objectMapper.writeValueAsString(safe);
        } catch (JsonProcessingException e) {
            throw new ApiErrorException(
                    HttpStatus.BAD_REQUEST,
                    "item.serialization_error",
                    "Некорректный JSON объект",
                    List.of(REQUIREMENT),
                    List.of(new ApiErrorDetail(field, e.getMessage()))
            );
        }
    }

    private ApiErrorException notFound(String code, String message) {
        return new ApiErrorException(HttpStatus.NOT_FOUND, code, message, List.of(REQUIREMENT), List.of());
    }

    private ApiErrorException conflict(String code, String message) {
        return new ApiErrorException(HttpStatus.CONFLICT, code, message, List.of(REQUIREMENT), List.of());
    }

    private EnumValueDto toEnum(EnumValueEntity entity) {
        if (entity == null) {
            return null;
        }
        return new EnumValueDto(
                entity.getId(),
                entity.getCode(),
                entity.getDisplayName(),
                entity.getDescription()
        );
    }
}

