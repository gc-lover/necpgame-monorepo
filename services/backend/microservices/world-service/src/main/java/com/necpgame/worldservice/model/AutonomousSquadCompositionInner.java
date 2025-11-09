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
 * AutonomousSquadCompositionInner
 */

@JsonTypeName("AutonomousSquad_composition_inner")

public class AutonomousSquadCompositionInner {

  private @Nullable String unitType;

  private @Nullable Integer count;

  public AutonomousSquadCompositionInner unitType(@Nullable String unitType) {
    this.unitType = unitType;
    return this;
  }

  /**
   * Get unitType
   * @return unitType
   */
  
  @Schema(name = "unitType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unitType")
  public @Nullable String getUnitType() {
    return unitType;
  }

  public void setUnitType(@Nullable String unitType) {
    this.unitType = unitType;
  }

  public AutonomousSquadCompositionInner count(@Nullable Integer count) {
    this.count = count;
    return this;
  }

  /**
   * Get count
   * @return count
   */
  
  @Schema(name = "count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("count")
  public @Nullable Integer getCount() {
    return count;
  }

  public void setCount(@Nullable Integer count) {
    this.count = count;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AutonomousSquadCompositionInner autonomousSquadCompositionInner = (AutonomousSquadCompositionInner) o;
    return Objects.equals(this.unitType, autonomousSquadCompositionInner.unitType) &&
        Objects.equals(this.count, autonomousSquadCompositionInner.count);
  }

  @Override
  public int hashCode() {
    return Objects.hash(unitType, count);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AutonomousSquadCompositionInner {\n");
    sb.append("    unitType: ").append(toIndentedString(unitType)).append("\n");
    sb.append("    count: ").append(toIndentedString(count)).append("\n");
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

