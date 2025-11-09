package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.ImplantLimitInfoBonuses;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * РРЅС„РѕСЂРјР°С†РёСЏ Рѕ Р»РёРјРёС‚Рµ РёРјРїР»Р°РЅС‚РѕРІ
 */

@Schema(name = "ImplantLimitInfo", description = "РРЅС„РѕСЂРјР°С†РёСЏ Рѕ Р»РёРјРёС‚Рµ РёРјРїР»Р°РЅС‚РѕРІ")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:51:47.912860600+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
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
   * Р‘Р°Р·РѕРІС‹Р№ Р»РёРјРёС‚
   * minimum: 0
   * @return baseLimit
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "base_limit", description = "Р‘Р°Р·РѕРІС‹Р№ Р»РёРјРёС‚", requiredMode = Schema.RequiredMode.REQUIRED)
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
   * РЁС‚СЂР°С„ РѕС‚ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё
   * maximum: 0
   * @return humanityPenalty
   */
  @Max(value = 0) 
  @Schema(name = "humanity_penalty", description = "РЁС‚СЂР°С„ РѕС‚ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
   * РўРµРєСѓС‰РёР№ Р»РёРјРёС‚
   * minimum: 0
   * @return currentLimit
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "current_limit", description = "РўРµРєСѓС‰РёР№ Р»РёРјРёС‚", requiredMode = Schema.RequiredMode.REQUIRED)
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
   * РСЃРїРѕР»СЊР·РѕРІР°РЅРѕ
   * minimum: 0
   * @return used
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "used", description = "РСЃРїРѕР»СЊР·РѕРІР°РЅРѕ", requiredMode = Schema.RequiredMode.REQUIRED)
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
   * Р”РѕСЃС‚СѓРїРЅРѕ
   * minimum: 0
   * @return available
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "available", description = "Р”РѕСЃС‚СѓРїРЅРѕ", requiredMode = Schema.RequiredMode.REQUIRED)
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
   * РњРѕР¶РЅРѕ Р»Рё РІСЂРµРјРµРЅРЅРѕ РїСЂРµРІС‹СЃРёС‚СЊ
   * @return canExceedTemporarily
   */
  
  @Schema(name = "can_exceed_temporarily", description = "РњРѕР¶РЅРѕ Р»Рё РІСЂРµРјРµРЅРЅРѕ РїСЂРµРІС‹СЃРёС‚СЊ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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

