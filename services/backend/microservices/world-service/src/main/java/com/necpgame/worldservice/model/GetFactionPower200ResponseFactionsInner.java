package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
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
 * GetFactionPower200ResponseFactionsInner
 */

@JsonTypeName("getFactionPower_200_response_factions_inner")

public class GetFactionPower200ResponseFactionsInner {

  private @Nullable String factionId;

  private @Nullable BigDecimal powerLevel;

  @Valid
  private List<String> controlledTerritories = new ArrayList<>();

  public GetFactionPower200ResponseFactionsInner factionId(@Nullable String factionId) {
    this.factionId = factionId;
    return this;
  }

  /**
   * Get factionId
   * @return factionId
   */
  
  @Schema(name = "faction_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_id")
  public @Nullable String getFactionId() {
    return factionId;
  }

  public void setFactionId(@Nullable String factionId) {
    this.factionId = factionId;
  }

  public GetFactionPower200ResponseFactionsInner powerLevel(@Nullable BigDecimal powerLevel) {
    this.powerLevel = powerLevel;
    return this;
  }

  /**
   * Get powerLevel
   * @return powerLevel
   */
  @Valid 
  @Schema(name = "power_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("power_level")
  public @Nullable BigDecimal getPowerLevel() {
    return powerLevel;
  }

  public void setPowerLevel(@Nullable BigDecimal powerLevel) {
    this.powerLevel = powerLevel;
  }

  public GetFactionPower200ResponseFactionsInner controlledTerritories(List<String> controlledTerritories) {
    this.controlledTerritories = controlledTerritories;
    return this;
  }

  public GetFactionPower200ResponseFactionsInner addControlledTerritoriesItem(String controlledTerritoriesItem) {
    if (this.controlledTerritories == null) {
      this.controlledTerritories = new ArrayList<>();
    }
    this.controlledTerritories.add(controlledTerritoriesItem);
    return this;
  }

  /**
   * Get controlledTerritories
   * @return controlledTerritories
   */
  
  @Schema(name = "controlled_territories", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("controlled_territories")
  public List<String> getControlledTerritories() {
    return controlledTerritories;
  }

  public void setControlledTerritories(List<String> controlledTerritories) {
    this.controlledTerritories = controlledTerritories;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetFactionPower200ResponseFactionsInner getFactionPower200ResponseFactionsInner = (GetFactionPower200ResponseFactionsInner) o;
    return Objects.equals(this.factionId, getFactionPower200ResponseFactionsInner.factionId) &&
        Objects.equals(this.powerLevel, getFactionPower200ResponseFactionsInner.powerLevel) &&
        Objects.equals(this.controlledTerritories, getFactionPower200ResponseFactionsInner.controlledTerritories);
  }

  @Override
  public int hashCode() {
    return Objects.hash(factionId, powerLevel, controlledTerritories);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetFactionPower200ResponseFactionsInner {\n");
    sb.append("    factionId: ").append(toIndentedString(factionId)).append("\n");
    sb.append("    powerLevel: ").append(toIndentedString(powerLevel)).append("\n");
    sb.append("    controlledTerritories: ").append(toIndentedString(controlledTerritories)).append("\n");
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

