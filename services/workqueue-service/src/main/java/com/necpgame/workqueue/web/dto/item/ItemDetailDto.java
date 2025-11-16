package com.necpgame.workqueue.web.dto.item;

import com.necpgame.workqueue.web.dto.content.ContentSummaryDto;
import com.necpgame.workqueue.web.dto.content.EnumValueDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ItemDetailDto(
        ContentSummaryDto summary,
        EnumValueDto category,
        EnumValueDto slot,
        EnumValueDto rarity,
        EnumValueDto bindType,
        BigDecimal weight,
        Integer levelRequirement,
        Integer stackSize,
        BigDecimal vendorPrice,
        Integer durabilityMax,
        boolean tradeable,
        BigDecimal powerScore,
        Map<String, Object> metadata,
        WeaponStatsDetail weapon,
        ArmorStatsDetail armor,
        List<ConsumableEffectDetail> consumableEffects,
        List<ItemModSlotDetail> modSlots,
        List<ComponentRequirementDetail> components
) {
    public record WeaponStatsDetail(
            EnumValueDto weaponClass,
            EnumValueDto damageType,
            BigDecimal damageMin,
            BigDecimal damageMax,
            BigDecimal fireRate,
            Integer magazineSize,
            BigDecimal reloadTimeSeconds,
            BigDecimal rangeMin,
            BigDecimal rangeMax,
            BigDecimal criticalChance,
            BigDecimal criticalMultiplier,
            BigDecimal accuracy,
            BigDecimal recoil,
            Map<String, Object> metadata
    ) {
    }

    public record ArmorStatsDetail(
            EnumValueDto armorType,
            BigDecimal armorValue,
            Map<String, Object> resistances,
            BigDecimal mobilityPenalty,
            Map<String, Object> metadata
    ) {
    }

    public record ConsumableEffectDetail(
            UUID id,
            UUID effectEntityId,
            BigDecimal durationSeconds,
            BigDecimal cooldownSeconds,
            Map<String, Object> metadata
    ) {
    }

    public record ItemModSlotDetail(
            UUID id,
            String slotCode,
            Integer capacity,
            Map<String, Object> metadata
    ) {
    }

    public record ComponentRequirementDetail(
            UUID id,
            UUID componentItemId,
            Integer quantity,
            String notes
    ) {
    }
}

