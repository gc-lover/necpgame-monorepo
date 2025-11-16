package com.necpgame.workqueue.web.dto.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ItemCommandRequestDto(
        @NotBlank String contentCode,
        @NotBlank String rarityCode,
        @NotBlank String bindTypeCode,
        String categoryCode,
        String slotCode,
        BigDecimal weight,
        @Min(0) Integer levelRequirement,
        @Min(1) Integer stackSize,
        BigDecimal vendorPrice,
        Integer durabilityMax,
        BigDecimal powerScore,
        Boolean tradeable,
        @NotNull Map<String, Object> metadata,
        @Valid WeaponStatsDto weapon,
        @Valid ArmorStatsDto armor,
        @Valid List<ConsumableEffectDto> consumableEffects,
        @Valid List<ItemModSlotDto> modSlots,
        @Valid List<ComponentRequirementDto> componentRequirements
) {
    public record WeaponStatsDto(
            String weaponClassCode,
            String damageTypeCode,
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
            @NotNull Map<String, Object> metadata
    ) {
    }

    public record ArmorStatsDto(
            String armorTypeCode,
            BigDecimal armorValue,
            @NotNull Map<String, Object> resistances,
            BigDecimal mobilityPenalty,
            @NotNull Map<String, Object> metadata
    ) {
    }

    public record ConsumableEffectDto(
            @NotNull UUID effectEntityId,
            BigDecimal durationSeconds,
            BigDecimal cooldownSeconds,
            @NotNull Map<String, Object> metadata
    ) {
    }

    public record ItemModSlotDto(
            @NotBlank String slotCode,
            @Min(1) Integer capacity,
            @NotNull Map<String, Object> metadata
    ) {
    }

    public record ComponentRequirementDto(
            @NotNull UUID componentItemId,
            @NotNull @Min(1) Integer quantity,
            String notes
    ) {
    }
}

