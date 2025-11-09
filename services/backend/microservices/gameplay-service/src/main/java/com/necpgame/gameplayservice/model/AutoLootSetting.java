package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * AutoLootSetting
 */


public class AutoLootSetting {

  private @Nullable Boolean enabled;

  private @Nullable BigDecimal pickupRadius;

  private @Nullable String rarityThreshold;

  public AutoLootSetting enabled(@Nullable Boolean enabled) {
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

  public AutoLootSetting pickupRadius(@Nullable BigDecimal pickupRadius) {
    this.pickupRadius = pickupRadius;
    return this;
  }

  /**
   * Get pickupRadius
   * @return pickupRadius
   */
  @Valid 
  @Schema(name = "pickupRadius", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pickupRadius")
  public @Nullable BigDecimal getPickupRadius() {
    return pickupRadius;
  }

  public void setPickupRadius(@Nullable BigDecimal pickupRadius) {
    this.pickupRadius = pickupRadius;
  }

  public AutoLootSetting rarityThreshold(@Nullable String rarityThreshold) {
    this.rarityThreshold = rarityThreshold;
    return this;
  }

  /**
   * Get rarityThreshold
   * @return rarityThreshold
   */
  
  @Schema(name = "rarityThreshold", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rarityThreshold")
  public @Nullable String getRarityThreshold() {
    return rarityThreshold;
  }

  public void setRarityThreshold(@Nullable String rarityThreshold) {
    this.rarityThreshold = rarityThreshold;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AutoLootSetting autoLootSetting = (AutoLootSetting) o;
    return Objects.equals(this.enabled, autoLootSetting.enabled) &&
        Objects.equals(this.pickupRadius, autoLootSetting.pickupRadius) &&
        Objects.equals(this.rarityThreshold, autoLootSetting.rarityThreshold);
  }

  @Override
  public int hashCode() {
    return Objects.hash(enabled, pickupRadius, rarityThreshold);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AutoLootSetting {\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    pickupRadius: ").append(toIndentedString(pickupRadius)).append("\n");
    sb.append("    rarityThreshold: ").append(toIndentedString(rarityThreshold)).append("\n");
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

