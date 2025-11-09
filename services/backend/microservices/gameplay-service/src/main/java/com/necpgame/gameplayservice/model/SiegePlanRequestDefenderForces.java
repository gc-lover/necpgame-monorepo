package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SiegePlanRequestDefenderForces
 */

@JsonTypeName("SiegePlanRequest_defenderForces")

public class SiegePlanRequestDefenderForces {

  private @Nullable Integer reinforcementSlots;

  private @Nullable Integer turretLevel;

  public SiegePlanRequestDefenderForces reinforcementSlots(@Nullable Integer reinforcementSlots) {
    this.reinforcementSlots = reinforcementSlots;
    return this;
  }

  /**
   * Get reinforcementSlots
   * @return reinforcementSlots
   */
  
  @Schema(name = "reinforcementSlots", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reinforcementSlots")
  public @Nullable Integer getReinforcementSlots() {
    return reinforcementSlots;
  }

  public void setReinforcementSlots(@Nullable Integer reinforcementSlots) {
    this.reinforcementSlots = reinforcementSlots;
  }

  public SiegePlanRequestDefenderForces turretLevel(@Nullable Integer turretLevel) {
    this.turretLevel = turretLevel;
    return this;
  }

  /**
   * Get turretLevel
   * @return turretLevel
   */
  
  @Schema(name = "turretLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("turretLevel")
  public @Nullable Integer getTurretLevel() {
    return turretLevel;
  }

  public void setTurretLevel(@Nullable Integer turretLevel) {
    this.turretLevel = turretLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SiegePlanRequestDefenderForces siegePlanRequestDefenderForces = (SiegePlanRequestDefenderForces) o;
    return Objects.equals(this.reinforcementSlots, siegePlanRequestDefenderForces.reinforcementSlots) &&
        Objects.equals(this.turretLevel, siegePlanRequestDefenderForces.turretLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reinforcementSlots, turretLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SiegePlanRequestDefenderForces {\n");
    sb.append("    reinforcementSlots: ").append(toIndentedString(reinforcementSlots)).append("\n");
    sb.append("    turretLevel: ").append(toIndentedString(turretLevel)).append("\n");
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

