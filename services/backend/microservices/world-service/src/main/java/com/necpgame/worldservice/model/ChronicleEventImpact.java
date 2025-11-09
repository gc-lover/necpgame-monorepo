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
 * ChronicleEventImpact
 */

@JsonTypeName("ChronicleEvent_impact")

public class ChronicleEventImpact {

  private @Nullable Integer controlScoreDelta;

  private @Nullable BigDecimal economyShift;

  private @Nullable BigDecimal xpModifier;

  public ChronicleEventImpact controlScoreDelta(@Nullable Integer controlScoreDelta) {
    this.controlScoreDelta = controlScoreDelta;
    return this;
  }

  /**
   * Get controlScoreDelta
   * @return controlScoreDelta
   */
  
  @Schema(name = "controlScoreDelta", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("controlScoreDelta")
  public @Nullable Integer getControlScoreDelta() {
    return controlScoreDelta;
  }

  public void setControlScoreDelta(@Nullable Integer controlScoreDelta) {
    this.controlScoreDelta = controlScoreDelta;
  }

  public ChronicleEventImpact economyShift(@Nullable BigDecimal economyShift) {
    this.economyShift = economyShift;
    return this;
  }

  /**
   * Get economyShift
   * @return economyShift
   */
  @Valid 
  @Schema(name = "economyShift", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("economyShift")
  public @Nullable BigDecimal getEconomyShift() {
    return economyShift;
  }

  public void setEconomyShift(@Nullable BigDecimal economyShift) {
    this.economyShift = economyShift;
  }

  public ChronicleEventImpact xpModifier(@Nullable BigDecimal xpModifier) {
    this.xpModifier = xpModifier;
    return this;
  }

  /**
   * Get xpModifier
   * @return xpModifier
   */
  @Valid 
  @Schema(name = "xpModifier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("xpModifier")
  public @Nullable BigDecimal getXpModifier() {
    return xpModifier;
  }

  public void setXpModifier(@Nullable BigDecimal xpModifier) {
    this.xpModifier = xpModifier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChronicleEventImpact chronicleEventImpact = (ChronicleEventImpact) o;
    return Objects.equals(this.controlScoreDelta, chronicleEventImpact.controlScoreDelta) &&
        Objects.equals(this.economyShift, chronicleEventImpact.economyShift) &&
        Objects.equals(this.xpModifier, chronicleEventImpact.xpModifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(controlScoreDelta, economyShift, xpModifier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChronicleEventImpact {\n");
    sb.append("    controlScoreDelta: ").append(toIndentedString(controlScoreDelta)).append("\n");
    sb.append("    economyShift: ").append(toIndentedString(economyShift)).append("\n");
    sb.append("    xpModifier: ").append(toIndentedString(xpModifier)).append("\n");
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

