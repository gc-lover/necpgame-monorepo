package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.ImplantLimitInfoBonuses;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Информация о лимите имплантов
 */

@Schema(name = "ImplantLimitInfo", description = "Информация о лимите имплантов")

public class ImplantLimitInfo {

  private Integer baseLimit;

  private @Nullable ImplantLimitInfoBonuses bonuses;

  private @Nullable Integer humanityPenalty;

  private Integer currentLimit;

  private Integer used;

  private Integer available;

  private @Nullable Boolean canExceedTemporarily;

  public ImplantLimitInfo() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ImplantLimitInfo(Integer baseLimit, Integer currentLimit, Integer used, Integer available) {
    this.baseLimit = baseLimit;
    this.currentLimit = currentLimit;
    this.used = used;
    this.available = available;
  }

  public ImplantLimitInfo baseLimit(Integer baseLimit) {
    this.baseLimit = baseLimit;
    return this;
  }

  /**
   * Базовый лимит
   * minimum: 0
   * @return baseLimit
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "base_limit", description = "Базовый лимит", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("base_limit")
  public Integer getBaseLimit() {
    return baseLimit;
  }

  public void setBaseLimit(Integer baseLimit) {
    this.baseLimit = baseLimit;
  }

  public ImplantLimitInfo bonuses(@Nullable ImplantLimitInfoBonuses bonuses) {
    this.bonuses = bonuses;
    return this;
  }

  /**
   * Get bonuses
   * @return bonuses
   */
  @Valid 
  @Schema(name = "bonuses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonuses")
  public @Nullable ImplantLimitInfoBonuses getBonuses() {
    return bonuses;
  }

  public void setBonuses(@Nullable ImplantLimitInfoBonuses bonuses) {
    this.bonuses = bonuses;
  }

  public ImplantLimitInfo humanityPenalty(@Nullable Integer humanityPenalty) {
    this.humanityPenalty = humanityPenalty;
    return this;
  }

  /**
   * Штраф от человечности
   * maximum: 0
   * @return humanityPenalty
   */
  @Max(value = 0) 
  @Schema(name = "humanity_penalty", description = "Штраф от человечности", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("humanity_penalty")
  public @Nullable Integer getHumanityPenalty() {
    return humanityPenalty;
  }

  public void setHumanityPenalty(@Nullable Integer humanityPenalty) {
    this.humanityPenalty = humanityPenalty;
  }

  public ImplantLimitInfo currentLimit(Integer currentLimit) {
    this.currentLimit = currentLimit;
    return this;
  }

  /**
   * Текущий лимит
   * minimum: 0
   * @return currentLimit
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "current_limit", description = "Текущий лимит", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("current_limit")
  public Integer getCurrentLimit() {
    return currentLimit;
  }

  public void setCurrentLimit(Integer currentLimit) {
    this.currentLimit = currentLimit;
  }

  public ImplantLimitInfo used(Integer used) {
    this.used = used;
    return this;
  }

  /**
   * Использовано
   * minimum: 0
   * @return used
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "used", description = "Использовано", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("used")
  public Integer getUsed() {
    return used;
  }

  public void setUsed(Integer used) {
    this.used = used;
  }

  public ImplantLimitInfo available(Integer available) {
    this.available = available;
    return this;
  }

  /**
   * Доступно
   * minimum: 0
   * @return available
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "available", description = "Доступно", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("available")
  public Integer getAvailable() {
    return available;
  }

  public void setAvailable(Integer available) {
    this.available = available;
  }

  public ImplantLimitInfo canExceedTemporarily(@Nullable Boolean canExceedTemporarily) {
    this.canExceedTemporarily = canExceedTemporarily;
    return this;
  }

  /**
   * Можно ли временно превысить
   * @return canExceedTemporarily
   */
  
  @Schema(name = "can_exceed_temporarily", description = "Можно ли временно превысить", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("can_exceed_temporarily")
  public @Nullable Boolean getCanExceedTemporarily() {
    return canExceedTemporarily;
  }

  public void setCanExceedTemporarily(@Nullable Boolean canExceedTemporarily) {
    this.canExceedTemporarily = canExceedTemporarily;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImplantLimitInfo implantLimitInfo = (ImplantLimitInfo) o;
    return Objects.equals(this.baseLimit, implantLimitInfo.baseLimit) &&
        Objects.equals(this.bonuses, implantLimitInfo.bonuses) &&
        Objects.equals(this.humanityPenalty, implantLimitInfo.humanityPenalty) &&
        Objects.equals(this.currentLimit, implantLimitInfo.currentLimit) &&
        Objects.equals(this.used, implantLimitInfo.used) &&
        Objects.equals(this.available, implantLimitInfo.available) &&
        Objects.equals(this.canExceedTemporarily, implantLimitInfo.canExceedTemporarily);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseLimit, bonuses, humanityPenalty, currentLimit, used, available, canExceedTemporarily);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImplantLimitInfo {\n");
    sb.append("    baseLimit: ").append(toIndentedString(baseLimit)).append("\n");
    sb.append("    bonuses: ").append(toIndentedString(bonuses)).append("\n");
    sb.append("    humanityPenalty: ").append(toIndentedString(humanityPenalty)).append("\n");
    sb.append("    currentLimit: ").append(toIndentedString(currentLimit)).append("\n");
    sb.append("    used: ").append(toIndentedString(used)).append("\n");
    sb.append("    available: ").append(toIndentedString(available)).append("\n");
    sb.append("    canExceedTemporarily: ").append(toIndentedString(canExceedTemporarily)).append("\n");
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

