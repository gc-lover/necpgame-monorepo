package com.necpgame.partymodule.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LootSettingsUpdateRequest
 */


public class LootSettingsUpdateRequest {

  /**
   * Gets or Sets mode
   */
  public enum ModeEnum {
    PERSONAL("PERSONAL"),
    
    NEED_GREED("NEED_GREED"),
    
    ROUND_ROBIN("ROUND_ROBIN"),
    
    MASTER_LOOTER("MASTER_LOOTER");

    private final String value;

    ModeEnum(String value) {
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
    public static ModeEnum fromValue(String value) {
      for (ModeEnum b : ModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ModeEnum mode = ModeEnum.PERSONAL;

  /**
   * Gets or Sets threshold
   */
  public enum ThresholdEnum {
    COMMON("COMMON"),
    
    RARE("RARE"),
    
    EPIC("EPIC"),
    
    LEGENDARY("LEGENDARY");

    private final String value;

    ThresholdEnum(String value) {
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
    public static ThresholdEnum fromValue(String value) {
      for (ThresholdEnum b : ThresholdEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ThresholdEnum threshold = ThresholdEnum.RARE;

  private @Nullable String masterLooter;

  private @Nullable Integer roundRobinIndex;

  private @Nullable Boolean autoDistribute;

  public LootSettingsUpdateRequest mode(ModeEnum mode) {
    this.mode = mode;
    return this;
  }

  /**
   * Get mode
   * @return mode
   */
  
  @Schema(name = "mode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mode")
  public ModeEnum getMode() {
    return mode;
  }

  public void setMode(ModeEnum mode) {
    this.mode = mode;
  }

  public LootSettingsUpdateRequest threshold(ThresholdEnum threshold) {
    this.threshold = threshold;
    return this;
  }

  /**
   * Get threshold
   * @return threshold
   */
  
  @Schema(name = "threshold", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("threshold")
  public ThresholdEnum getThreshold() {
    return threshold;
  }

  public void setThreshold(ThresholdEnum threshold) {
    this.threshold = threshold;
  }

  public LootSettingsUpdateRequest masterLooter(@Nullable String masterLooter) {
    this.masterLooter = masterLooter;
    return this;
  }

  /**
   * Get masterLooter
   * @return masterLooter
   */
  
  @Schema(name = "masterLooter", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("masterLooter")
  public @Nullable String getMasterLooter() {
    return masterLooter;
  }

  public void setMasterLooter(@Nullable String masterLooter) {
    this.masterLooter = masterLooter;
  }

  public LootSettingsUpdateRequest roundRobinIndex(@Nullable Integer roundRobinIndex) {
    this.roundRobinIndex = roundRobinIndex;
    return this;
  }

  /**
   * Get roundRobinIndex
   * @return roundRobinIndex
   */
  
  @Schema(name = "roundRobinIndex", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roundRobinIndex")
  public @Nullable Integer getRoundRobinIndex() {
    return roundRobinIndex;
  }

  public void setRoundRobinIndex(@Nullable Integer roundRobinIndex) {
    this.roundRobinIndex = roundRobinIndex;
  }

  public LootSettingsUpdateRequest autoDistribute(@Nullable Boolean autoDistribute) {
    this.autoDistribute = autoDistribute;
    return this;
  }

  /**
   * Get autoDistribute
   * @return autoDistribute
   */
  
  @Schema(name = "autoDistribute", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("autoDistribute")
  public @Nullable Boolean getAutoDistribute() {
    return autoDistribute;
  }

  public void setAutoDistribute(@Nullable Boolean autoDistribute) {
    this.autoDistribute = autoDistribute;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootSettingsUpdateRequest lootSettingsUpdateRequest = (LootSettingsUpdateRequest) o;
    return Objects.equals(this.mode, lootSettingsUpdateRequest.mode) &&
        Objects.equals(this.threshold, lootSettingsUpdateRequest.threshold) &&
        Objects.equals(this.masterLooter, lootSettingsUpdateRequest.masterLooter) &&
        Objects.equals(this.roundRobinIndex, lootSettingsUpdateRequest.roundRobinIndex) &&
        Objects.equals(this.autoDistribute, lootSettingsUpdateRequest.autoDistribute);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mode, threshold, masterLooter, roundRobinIndex, autoDistribute);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootSettingsUpdateRequest {\n");
    sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
    sb.append("    threshold: ").append(toIndentedString(threshold)).append("\n");
    sb.append("    masterLooter: ").append(toIndentedString(masterLooter)).append("\n");
    sb.append("    roundRobinIndex: ").append(toIndentedString(roundRobinIndex)).append("\n");
    sb.append("    autoDistribute: ").append(toIndentedString(autoDistribute)).append("\n");
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

