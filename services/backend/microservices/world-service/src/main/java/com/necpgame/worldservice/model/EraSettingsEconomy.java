package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * EraSettingsEconomy
 */

@JsonTypeName("EraSettings_economy")

public class EraSettingsEconomy {

  private @Nullable BigDecimal resourceMultiplier;

  private @Nullable String volatility;

  private @Nullable BigDecimal tariffsModifier;

  public EraSettingsEconomy resourceMultiplier(@Nullable BigDecimal resourceMultiplier) {
    this.resourceMultiplier = resourceMultiplier;
    return this;
  }

  /**
   * Get resourceMultiplier
   * @return resourceMultiplier
   */
  @Valid 
  @Schema(name = "resource_multiplier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resource_multiplier")
  public @Nullable BigDecimal getResourceMultiplier() {
    return resourceMultiplier;
  }

  public void setResourceMultiplier(@Nullable BigDecimal resourceMultiplier) {
    this.resourceMultiplier = resourceMultiplier;
  }

  public EraSettingsEconomy volatility(@Nullable String volatility) {
    this.volatility = volatility;
    return this;
  }

  /**
   * Get volatility
   * @return volatility
   */
  
  @Schema(name = "volatility", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("volatility")
  public @Nullable String getVolatility() {
    return volatility;
  }

  public void setVolatility(@Nullable String volatility) {
    this.volatility = volatility;
  }

  public EraSettingsEconomy tariffsModifier(@Nullable BigDecimal tariffsModifier) {
    this.tariffsModifier = tariffsModifier;
    return this;
  }

  /**
   * Get tariffsModifier
   * @return tariffsModifier
   */
  @Valid 
  @Schema(name = "tariffs_modifier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tariffs_modifier")
  public @Nullable BigDecimal getTariffsModifier() {
    return tariffsModifier;
  }

  public void setTariffsModifier(@Nullable BigDecimal tariffsModifier) {
    this.tariffsModifier = tariffsModifier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EraSettingsEconomy eraSettingsEconomy = (EraSettingsEconomy) o;
    return Objects.equals(this.resourceMultiplier, eraSettingsEconomy.resourceMultiplier) &&
        Objects.equals(this.volatility, eraSettingsEconomy.volatility) &&
        Objects.equals(this.tariffsModifier, eraSettingsEconomy.tariffsModifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resourceMultiplier, volatility, tariffsModifier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EraSettingsEconomy {\n");
    sb.append("    resourceMultiplier: ").append(toIndentedString(resourceMultiplier)).append("\n");
    sb.append("    volatility: ").append(toIndentedString(volatility)).append("\n");
    sb.append("    tariffsModifier: ").append(toIndentedString(tariffsModifier)).append("\n");
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

