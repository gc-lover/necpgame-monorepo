package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.ImplantsLimitsClassBonuses;
import com.necpgame.gameplayservice.model.ImplantsLimitsHumanityImpact;
import com.necpgame.gameplayservice.model.ImplantsLimitsQualityImpact;
import com.necpgame.gameplayservice.model.ImplantsLimitsSlotsByType;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ImplantsLimits
 */


public class ImplantsLimits {

  private @Nullable String characterId;

  private @Nullable Integer totalSlots;

  private @Nullable Integer usedSlots;

  private @Nullable Integer availableSlots;

  private @Nullable ImplantsLimitsSlotsByType slotsByType;

  private @Nullable BigDecimal humanityCurrent;

  private @Nullable BigDecimal humanityMax;

  private @Nullable ImplantsLimitsHumanityImpact humanityImpact;

  private @Nullable ImplantsLimitsClassBonuses classBonuses;

  private @Nullable ImplantsLimitsQualityImpact qualityImpact;

  public ImplantsLimits characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public ImplantsLimits totalSlots(@Nullable Integer totalSlots) {
    this.totalSlots = totalSlots;
    return this;
  }

  /**
   * Общее количество слотов для имплантов
   * @return totalSlots
   */
  
  @Schema(name = "total_slots", description = "Общее количество слотов для имплантов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_slots")
  public @Nullable Integer getTotalSlots() {
    return totalSlots;
  }

  public void setTotalSlots(@Nullable Integer totalSlots) {
    this.totalSlots = totalSlots;
  }

  public ImplantsLimits usedSlots(@Nullable Integer usedSlots) {
    this.usedSlots = usedSlots;
    return this;
  }

  /**
   * Использованные слоты
   * @return usedSlots
   */
  
  @Schema(name = "used_slots", description = "Использованные слоты", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("used_slots")
  public @Nullable Integer getUsedSlots() {
    return usedSlots;
  }

  public void setUsedSlots(@Nullable Integer usedSlots) {
    this.usedSlots = usedSlots;
  }

  public ImplantsLimits availableSlots(@Nullable Integer availableSlots) {
    this.availableSlots = availableSlots;
    return this;
  }

  /**
   * Доступные слоты
   * @return availableSlots
   */
  
  @Schema(name = "available_slots", description = "Доступные слоты", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("available_slots")
  public @Nullable Integer getAvailableSlots() {
    return availableSlots;
  }

  public void setAvailableSlots(@Nullable Integer availableSlots) {
    this.availableSlots = availableSlots;
  }

  public ImplantsLimits slotsByType(@Nullable ImplantsLimitsSlotsByType slotsByType) {
    this.slotsByType = slotsByType;
    return this;
  }

  /**
   * Get slotsByType
   * @return slotsByType
   */
  @Valid 
  @Schema(name = "slots_by_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slots_by_type")
  public @Nullable ImplantsLimitsSlotsByType getSlotsByType() {
    return slotsByType;
  }

  public void setSlotsByType(@Nullable ImplantsLimitsSlotsByType slotsByType) {
    this.slotsByType = slotsByType;
  }

  public ImplantsLimits humanityCurrent(@Nullable BigDecimal humanityCurrent) {
    this.humanityCurrent = humanityCurrent;
    return this;
  }

  /**
   * Текущий уровень человечности
   * @return humanityCurrent
   */
  @Valid 
  @Schema(name = "humanity_current", description = "Текущий уровень человечности", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("humanity_current")
  public @Nullable BigDecimal getHumanityCurrent() {
    return humanityCurrent;
  }

  public void setHumanityCurrent(@Nullable BigDecimal humanityCurrent) {
    this.humanityCurrent = humanityCurrent;
  }

  public ImplantsLimits humanityMax(@Nullable BigDecimal humanityMax) {
    this.humanityMax = humanityMax;
    return this;
  }

  /**
   * Максимальный уровень человечности
   * @return humanityMax
   */
  @Valid 
  @Schema(name = "humanity_max", description = "Максимальный уровень человечности", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("humanity_max")
  public @Nullable BigDecimal getHumanityMax() {
    return humanityMax;
  }

  public void setHumanityMax(@Nullable BigDecimal humanityMax) {
    this.humanityMax = humanityMax;
  }

  public ImplantsLimits humanityImpact(@Nullable ImplantsLimitsHumanityImpact humanityImpact) {
    this.humanityImpact = humanityImpact;
    return this;
  }

  /**
   * Get humanityImpact
   * @return humanityImpact
   */
  @Valid 
  @Schema(name = "humanity_impact", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("humanity_impact")
  public @Nullable ImplantsLimitsHumanityImpact getHumanityImpact() {
    return humanityImpact;
  }

  public void setHumanityImpact(@Nullable ImplantsLimitsHumanityImpact humanityImpact) {
    this.humanityImpact = humanityImpact;
  }

  public ImplantsLimits classBonuses(@Nullable ImplantsLimitsClassBonuses classBonuses) {
    this.classBonuses = classBonuses;
    return this;
  }

  /**
   * Get classBonuses
   * @return classBonuses
   */
  @Valid 
  @Schema(name = "class_bonuses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("class_bonuses")
  public @Nullable ImplantsLimitsClassBonuses getClassBonuses() {
    return classBonuses;
  }

  public void setClassBonuses(@Nullable ImplantsLimitsClassBonuses classBonuses) {
    this.classBonuses = classBonuses;
  }

  public ImplantsLimits qualityImpact(@Nullable ImplantsLimitsQualityImpact qualityImpact) {
    this.qualityImpact = qualityImpact;
    return this;
  }

  /**
   * Get qualityImpact
   * @return qualityImpact
   */
  @Valid 
  @Schema(name = "quality_impact", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quality_impact")
  public @Nullable ImplantsLimitsQualityImpact getQualityImpact() {
    return qualityImpact;
  }

  public void setQualityImpact(@Nullable ImplantsLimitsQualityImpact qualityImpact) {
    this.qualityImpact = qualityImpact;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImplantsLimits implantsLimits = (ImplantsLimits) o;
    return Objects.equals(this.characterId, implantsLimits.characterId) &&
        Objects.equals(this.totalSlots, implantsLimits.totalSlots) &&
        Objects.equals(this.usedSlots, implantsLimits.usedSlots) &&
        Objects.equals(this.availableSlots, implantsLimits.availableSlots) &&
        Objects.equals(this.slotsByType, implantsLimits.slotsByType) &&
        Objects.equals(this.humanityCurrent, implantsLimits.humanityCurrent) &&
        Objects.equals(this.humanityMax, implantsLimits.humanityMax) &&
        Objects.equals(this.humanityImpact, implantsLimits.humanityImpact) &&
        Objects.equals(this.classBonuses, implantsLimits.classBonuses) &&
        Objects.equals(this.qualityImpact, implantsLimits.qualityImpact);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, totalSlots, usedSlots, availableSlots, slotsByType, humanityCurrent, humanityMax, humanityImpact, classBonuses, qualityImpact);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImplantsLimits {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    totalSlots: ").append(toIndentedString(totalSlots)).append("\n");
    sb.append("    usedSlots: ").append(toIndentedString(usedSlots)).append("\n");
    sb.append("    availableSlots: ").append(toIndentedString(availableSlots)).append("\n");
    sb.append("    slotsByType: ").append(toIndentedString(slotsByType)).append("\n");
    sb.append("    humanityCurrent: ").append(toIndentedString(humanityCurrent)).append("\n");
    sb.append("    humanityMax: ").append(toIndentedString(humanityMax)).append("\n");
    sb.append("    humanityImpact: ").append(toIndentedString(humanityImpact)).append("\n");
    sb.append("    classBonuses: ").append(toIndentedString(classBonuses)).append("\n");
    sb.append("    qualityImpact: ").append(toIndentedString(qualityImpact)).append("\n");
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

