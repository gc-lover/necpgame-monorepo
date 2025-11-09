package com.necpgame.lootservice.model;

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
 * PityTimerConfig
 */


public class PityTimerConfig {

  private @Nullable Boolean enabled;

  private @Nullable Integer threshold;

  private @Nullable String rewardTemplateId;

  /**
   * Gets or Sets resetMode
   */
  public enum ResetModeEnum {
    ON_DROP("ON_DROP"),
    
    ON_SEASON("ON_SEASON"),
    
    MANUAL("MANUAL");

    private final String value;

    ResetModeEnum(String value) {
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
    public static ResetModeEnum fromValue(String value) {
      for (ResetModeEnum b : ResetModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ResetModeEnum resetMode;

  public PityTimerConfig enabled(@Nullable Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * Get enabled
   * @return enabled
   */
  
  @Schema(name = "enabled", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("enabled")
  public @Nullable Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(@Nullable Boolean enabled) {
    this.enabled = enabled;
  }

  public PityTimerConfig threshold(@Nullable Integer threshold) {
    this.threshold = threshold;
    return this;
  }

  /**
   * Get threshold
   * @return threshold
   */
  
  @Schema(name = "threshold", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("threshold")
  public @Nullable Integer getThreshold() {
    return threshold;
  }

  public void setThreshold(@Nullable Integer threshold) {
    this.threshold = threshold;
  }

  public PityTimerConfig rewardTemplateId(@Nullable String rewardTemplateId) {
    this.rewardTemplateId = rewardTemplateId;
    return this;
  }

  /**
   * Get rewardTemplateId
   * @return rewardTemplateId
   */
  
  @Schema(name = "rewardTemplateId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewardTemplateId")
  public @Nullable String getRewardTemplateId() {
    return rewardTemplateId;
  }

  public void setRewardTemplateId(@Nullable String rewardTemplateId) {
    this.rewardTemplateId = rewardTemplateId;
  }

  public PityTimerConfig resetMode(@Nullable ResetModeEnum resetMode) {
    this.resetMode = resetMode;
    return this;
  }

  /**
   * Get resetMode
   * @return resetMode
   */
  
  @Schema(name = "resetMode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resetMode")
  public @Nullable ResetModeEnum getResetMode() {
    return resetMode;
  }

  public void setResetMode(@Nullable ResetModeEnum resetMode) {
    this.resetMode = resetMode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PityTimerConfig pityTimerConfig = (PityTimerConfig) o;
    return Objects.equals(this.enabled, pityTimerConfig.enabled) &&
        Objects.equals(this.threshold, pityTimerConfig.threshold) &&
        Objects.equals(this.rewardTemplateId, pityTimerConfig.rewardTemplateId) &&
        Objects.equals(this.resetMode, pityTimerConfig.resetMode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(enabled, threshold, rewardTemplateId, resetMode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PityTimerConfig {\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    threshold: ").append(toIndentedString(threshold)).append("\n");
    sb.append("    rewardTemplateId: ").append(toIndentedString(rewardTemplateId)).append("\n");
    sb.append("    resetMode: ").append(toIndentedString(resetMode)).append("\n");
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

