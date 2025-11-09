package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Лимиты имплантов игрока. Источник: .BRAIN/02-gameplay/combat/combat-implants-limits.md -&gt; Лимит имплантов 
 */

@Schema(name = "ImplantLimits", description = "Лимиты имплантов игрока. Источник: .BRAIN/02-gameplay/combat/combat-implants-limits.md -> Лимит имплантов ")

public class ImplantLimits {

  private Integer baseLimit;

  private @Nullable Integer bonusFromClass;

  private @Nullable Integer bonusFromProgression;

  private @Nullable Integer humanityPenalty;

  private Integer currentLimit;

  private Integer usedSlots;

  private Integer availableSlots;

  private @Nullable Boolean canExceedTemporarily;

  private JsonNullable<@DecimalMin(value = "0") Float> temporaryExceedDuration = JsonNullable.<Float>undefined();

  public ImplantLimits() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ImplantLimits(Integer baseLimit, Integer currentLimit, Integer usedSlots, Integer availableSlots) {
    this.baseLimit = baseLimit;
    this.currentLimit = currentLimit;
    this.usedSlots = usedSlots;
    this.availableSlots = availableSlots;
  }

  public ImplantLimits baseLimit(Integer baseLimit) {
    this.baseLimit = baseLimit;
    return this;
  }

  /**
   * Базовый лимит имплантов
   * minimum: 0
   * @return baseLimit
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "base_limit", description = "Базовый лимит имплантов", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("base_limit")
  public Integer getBaseLimit() {
    return baseLimit;
  }

  public void setBaseLimit(Integer baseLimit) {
    this.baseLimit = baseLimit;
  }

  public ImplantLimits bonusFromClass(@Nullable Integer bonusFromClass) {
    this.bonusFromClass = bonusFromClass;
    return this;
  }

  /**
   * Бонус от класса
   * minimum: 0
   * @return bonusFromClass
   */
  @Min(value = 0) 
  @Schema(name = "bonus_from_class", description = "Бонус от класса", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonus_from_class")
  public @Nullable Integer getBonusFromClass() {
    return bonusFromClass;
  }

  public void setBonusFromClass(@Nullable Integer bonusFromClass) {
    this.bonusFromClass = bonusFromClass;
  }

  public ImplantLimits bonusFromProgression(@Nullable Integer bonusFromProgression) {
    this.bonusFromProgression = bonusFromProgression;
    return this;
  }

  /**
   * Бонус от прокачки
   * minimum: 0
   * @return bonusFromProgression
   */
  @Min(value = 0) 
  @Schema(name = "bonus_from_progression", description = "Бонус от прокачки", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonus_from_progression")
  public @Nullable Integer getBonusFromProgression() {
    return bonusFromProgression;
  }

  public void setBonusFromProgression(@Nullable Integer bonusFromProgression) {
    this.bonusFromProgression = bonusFromProgression;
  }

  public ImplantLimits humanityPenalty(@Nullable Integer humanityPenalty) {
    this.humanityPenalty = humanityPenalty;
    return this;
  }

  /**
   * Штраф от человечности (отрицательное значение)
   * maximum: 0
   * @return humanityPenalty
   */
  @Max(value = 0) 
  @Schema(name = "humanity_penalty", description = "Штраф от человечности (отрицательное значение)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("humanity_penalty")
  public @Nullable Integer getHumanityPenalty() {
    return humanityPenalty;
  }

  public void setHumanityPenalty(@Nullable Integer humanityPenalty) {
    this.humanityPenalty = humanityPenalty;
  }

  public ImplantLimits currentLimit(Integer currentLimit) {
    this.currentLimit = currentLimit;
    return this;
  }

  /**
   * Текущий лимит имплантов
   * minimum: 0
   * @return currentLimit
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "current_limit", description = "Текущий лимит имплантов", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("current_limit")
  public Integer getCurrentLimit() {
    return currentLimit;
  }

  public void setCurrentLimit(Integer currentLimit) {
    this.currentLimit = currentLimit;
  }

  public ImplantLimits usedSlots(Integer usedSlots) {
    this.usedSlots = usedSlots;
    return this;
  }

  /**
   * Использовано слотов
   * minimum: 0
   * @return usedSlots
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "used_slots", description = "Использовано слотов", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("used_slots")
  public Integer getUsedSlots() {
    return usedSlots;
  }

  public void setUsedSlots(Integer usedSlots) {
    this.usedSlots = usedSlots;
  }

  public ImplantLimits availableSlots(Integer availableSlots) {
    this.availableSlots = availableSlots;
    return this;
  }

  /**
   * Доступно слотов
   * minimum: 0
   * @return availableSlots
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "available_slots", description = "Доступно слотов", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("available_slots")
  public Integer getAvailableSlots() {
    return availableSlots;
  }

  public void setAvailableSlots(Integer availableSlots) {
    this.availableSlots = availableSlots;
  }

  public ImplantLimits canExceedTemporarily(@Nullable Boolean canExceedTemporarily) {
    this.canExceedTemporarily = canExceedTemporarily;
    return this;
  }

  /**
   * Можно ли временно превысить лимит
   * @return canExceedTemporarily
   */
  
  @Schema(name = "can_exceed_temporarily", description = "Можно ли временно превысить лимит", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("can_exceed_temporarily")
  public @Nullable Boolean getCanExceedTemporarily() {
    return canExceedTemporarily;
  }

  public void setCanExceedTemporarily(@Nullable Boolean canExceedTemporarily) {
    this.canExceedTemporarily = canExceedTemporarily;
  }

  public ImplantLimits temporaryExceedDuration(Float temporaryExceedDuration) {
    this.temporaryExceedDuration = JsonNullable.of(temporaryExceedDuration);
    return this;
  }

  /**
   * Длительность временного превышения в секундах (если превышен)
   * minimum: 0
   * @return temporaryExceedDuration
   */
  @DecimalMin(value = "0") 
  @Schema(name = "temporary_exceed_duration", description = "Длительность временного превышения в секундах (если превышен)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("temporary_exceed_duration")
  public JsonNullable<@DecimalMin(value = "0") Float> getTemporaryExceedDuration() {
    return temporaryExceedDuration;
  }

  public void setTemporaryExceedDuration(JsonNullable<Float> temporaryExceedDuration) {
    this.temporaryExceedDuration = temporaryExceedDuration;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImplantLimits implantLimits = (ImplantLimits) o;
    return Objects.equals(this.baseLimit, implantLimits.baseLimit) &&
        Objects.equals(this.bonusFromClass, implantLimits.bonusFromClass) &&
        Objects.equals(this.bonusFromProgression, implantLimits.bonusFromProgression) &&
        Objects.equals(this.humanityPenalty, implantLimits.humanityPenalty) &&
        Objects.equals(this.currentLimit, implantLimits.currentLimit) &&
        Objects.equals(this.usedSlots, implantLimits.usedSlots) &&
        Objects.equals(this.availableSlots, implantLimits.availableSlots) &&
        Objects.equals(this.canExceedTemporarily, implantLimits.canExceedTemporarily) &&
        equalsNullable(this.temporaryExceedDuration, implantLimits.temporaryExceedDuration);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseLimit, bonusFromClass, bonusFromProgression, humanityPenalty, currentLimit, usedSlots, availableSlots, canExceedTemporarily, hashCodeNullable(temporaryExceedDuration));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImplantLimits {\n");
    sb.append("    baseLimit: ").append(toIndentedString(baseLimit)).append("\n");
    sb.append("    bonusFromClass: ").append(toIndentedString(bonusFromClass)).append("\n");
    sb.append("    bonusFromProgression: ").append(toIndentedString(bonusFromProgression)).append("\n");
    sb.append("    humanityPenalty: ").append(toIndentedString(humanityPenalty)).append("\n");
    sb.append("    currentLimit: ").append(toIndentedString(currentLimit)).append("\n");
    sb.append("    usedSlots: ").append(toIndentedString(usedSlots)).append("\n");
    sb.append("    availableSlots: ").append(toIndentedString(availableSlots)).append("\n");
    sb.append("    canExceedTemporarily: ").append(toIndentedString(canExceedTemporarily)).append("\n");
    sb.append("    temporaryExceedDuration: ").append(toIndentedString(temporaryExceedDuration)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

