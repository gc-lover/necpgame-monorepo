package com.necpgame.worldservice.model;

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
 * SettlementUpdateRequestDefenseAdjustments
 */

@JsonTypeName("SettlementUpdateRequest_defenseAdjustments")

public class SettlementUpdateRequestDefenseAdjustments {

  private @Nullable Integer turrets;

  private @Nullable Integer droneSupport;

  private @Nullable Integer shieldLevel;

  public SettlementUpdateRequestDefenseAdjustments turrets(@Nullable Integer turrets) {
    this.turrets = turrets;
    return this;
  }

  /**
   * Get turrets
   * @return turrets
   */
  
  @Schema(name = "turrets", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("turrets")
  public @Nullable Integer getTurrets() {
    return turrets;
  }

  public void setTurrets(@Nullable Integer turrets) {
    this.turrets = turrets;
  }

  public SettlementUpdateRequestDefenseAdjustments droneSupport(@Nullable Integer droneSupport) {
    this.droneSupport = droneSupport;
    return this;
  }

  /**
   * Get droneSupport
   * @return droneSupport
   */
  
  @Schema(name = "droneSupport", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("droneSupport")
  public @Nullable Integer getDroneSupport() {
    return droneSupport;
  }

  public void setDroneSupport(@Nullable Integer droneSupport) {
    this.droneSupport = droneSupport;
  }

  public SettlementUpdateRequestDefenseAdjustments shieldLevel(@Nullable Integer shieldLevel) {
    this.shieldLevel = shieldLevel;
    return this;
  }

  /**
   * Get shieldLevel
   * @return shieldLevel
   */
  
  @Schema(name = "shieldLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("shieldLevel")
  public @Nullable Integer getShieldLevel() {
    return shieldLevel;
  }

  public void setShieldLevel(@Nullable Integer shieldLevel) {
    this.shieldLevel = shieldLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SettlementUpdateRequestDefenseAdjustments settlementUpdateRequestDefenseAdjustments = (SettlementUpdateRequestDefenseAdjustments) o;
    return Objects.equals(this.turrets, settlementUpdateRequestDefenseAdjustments.turrets) &&
        Objects.equals(this.droneSupport, settlementUpdateRequestDefenseAdjustments.droneSupport) &&
        Objects.equals(this.shieldLevel, settlementUpdateRequestDefenseAdjustments.shieldLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(turrets, droneSupport, shieldLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SettlementUpdateRequestDefenseAdjustments {\n");
    sb.append("    turrets: ").append(toIndentedString(turrets)).append("\n");
    sb.append("    droneSupport: ").append(toIndentedString(droneSupport)).append("\n");
    sb.append("    shieldLevel: ").append(toIndentedString(shieldLevel)).append("\n");
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

