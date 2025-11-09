package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.CosmeticSettingsGiftingLimits;
import com.necpgame.gameplayservice.model.CosmeticSettingsPromoSettings;
import com.necpgame.gameplayservice.model.CosmeticSettingsRegionLocksInner;
import com.necpgame.gameplayservice.model.CosmeticSettingsRotationSchedule;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CosmeticSettings
 */


public class CosmeticSettings {

  private @Nullable CosmeticSettingsGiftingLimits giftingLimits;

  @Valid
  private List<@Valid CosmeticSettingsRegionLocksInner> regionLocks = new ArrayList<>();

  /**
   * Gets or Sets duplicatePolicy
   */
  public enum DuplicatePolicyEnum {
    CONVERT_CURRENCY("convert_currency"),
    
    GRANT_TOKENS("grant_tokens"),
    
    BLOCK_PURCHASE("block_purchase");

    private final String value;

    DuplicatePolicyEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static DuplicatePolicyEnum fromValue(String value) {
      for (DuplicatePolicyEnum b : DuplicatePolicyEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable DuplicatePolicyEnum duplicatePolicy;

  private @Nullable CosmeticSettingsRotationSchedule rotationSchedule;

  private @Nullable CosmeticSettingsPromoSettings promoSettings;

  public CosmeticSettings giftingLimits(@Nullable CosmeticSettingsGiftingLimits giftingLimits) {
    this.giftingLimits = giftingLimits;
    return this;
  }

  /**
   * Get giftingLimits
   * @return giftingLimits
   */
  @Valid 
  @Schema(name = "giftingLimits", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("giftingLimits")
  public @Nullable CosmeticSettingsGiftingLimits getGiftingLimits() {
    return giftingLimits;
  }

  public void setGiftingLimits(@Nullable CosmeticSettingsGiftingLimits giftingLimits) {
    this.giftingLimits = giftingLimits;
  }

  public CosmeticSettings regionLocks(List<@Valid CosmeticSettingsRegionLocksInner> regionLocks) {
    this.regionLocks = regionLocks;
    return this;
  }

  public CosmeticSettings addRegionLocksItem(CosmeticSettingsRegionLocksInner regionLocksItem) {
    if (this.regionLocks == null) {
      this.regionLocks = new ArrayList<>();
    }
    this.regionLocks.add(regionLocksItem);
    return this;
  }

  /**
   * Get regionLocks
   * @return regionLocks
   */
  @Valid 
  @Schema(name = "regionLocks", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("regionLocks")
  public List<@Valid CosmeticSettingsRegionLocksInner> getRegionLocks() {
    return regionLocks;
  }

  public void setRegionLocks(List<@Valid CosmeticSettingsRegionLocksInner> regionLocks) {
    this.regionLocks = regionLocks;
  }

  public CosmeticSettings duplicatePolicy(@Nullable DuplicatePolicyEnum duplicatePolicy) {
    this.duplicatePolicy = duplicatePolicy;
    return this;
  }

  /**
   * Get duplicatePolicy
   * @return duplicatePolicy
   */
  
  @Schema(name = "duplicatePolicy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duplicatePolicy")
  public @Nullable DuplicatePolicyEnum getDuplicatePolicy() {
    return duplicatePolicy;
  }

  public void setDuplicatePolicy(@Nullable DuplicatePolicyEnum duplicatePolicy) {
    this.duplicatePolicy = duplicatePolicy;
  }

  public CosmeticSettings rotationSchedule(@Nullable CosmeticSettingsRotationSchedule rotationSchedule) {
    this.rotationSchedule = rotationSchedule;
    return this;
  }

  /**
   * Get rotationSchedule
   * @return rotationSchedule
   */
  @Valid 
  @Schema(name = "rotationSchedule", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rotationSchedule")
  public @Nullable CosmeticSettingsRotationSchedule getRotationSchedule() {
    return rotationSchedule;
  }

  public void setRotationSchedule(@Nullable CosmeticSettingsRotationSchedule rotationSchedule) {
    this.rotationSchedule = rotationSchedule;
  }

  public CosmeticSettings promoSettings(@Nullable CosmeticSettingsPromoSettings promoSettings) {
    this.promoSettings = promoSettings;
    return this;
  }

  /**
   * Get promoSettings
   * @return promoSettings
   */
  @Valid 
  @Schema(name = "promoSettings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("promoSettings")
  public @Nullable CosmeticSettingsPromoSettings getPromoSettings() {
    return promoSettings;
  }

  public void setPromoSettings(@Nullable CosmeticSettingsPromoSettings promoSettings) {
    this.promoSettings = promoSettings;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CosmeticSettings cosmeticSettings = (CosmeticSettings) o;
    return Objects.equals(this.giftingLimits, cosmeticSettings.giftingLimits) &&
        Objects.equals(this.regionLocks, cosmeticSettings.regionLocks) &&
        Objects.equals(this.duplicatePolicy, cosmeticSettings.duplicatePolicy) &&
        Objects.equals(this.rotationSchedule, cosmeticSettings.rotationSchedule) &&
        Objects.equals(this.promoSettings, cosmeticSettings.promoSettings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(giftingLimits, regionLocks, duplicatePolicy, rotationSchedule, promoSettings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CosmeticSettings {\n");
    sb.append("    giftingLimits: ").append(toIndentedString(giftingLimits)).append("\n");
    sb.append("    regionLocks: ").append(toIndentedString(regionLocks)).append("\n");
    sb.append("    duplicatePolicy: ").append(toIndentedString(duplicatePolicy)).append("\n");
    sb.append("    rotationSchedule: ").append(toIndentedString(rotationSchedule)).append("\n");
    sb.append("    promoSettings: ").append(toIndentedString(promoSettings)).append("\n");
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

