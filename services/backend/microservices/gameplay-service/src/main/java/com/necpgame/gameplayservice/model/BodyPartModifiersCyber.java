package com.necpgame.gameplayservice.model;

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
 * Модификаторы для кибер-частей
 */

@Schema(name = "BodyPartModifiers_cyber", description = "Модификаторы для кибер-частей")
@JsonTypeName("BodyPartModifiers_cyber")

public class BodyPartModifiersCyber {

  private @Nullable BigDecimal cyberHead;

  private @Nullable BigDecimal cyberTorso;

  private @Nullable BigDecimal cyberArms;

  private @Nullable BigDecimal cyberLegs;

  public BodyPartModifiersCyber cyberHead(@Nullable BigDecimal cyberHead) {
    this.cyberHead = cyberHead;
    return this;
  }

  /**
   * Get cyberHead
   * @return cyberHead
   */
  @Valid 
  @Schema(name = "cyber_head", example = "1.5", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cyber_head")
  public @Nullable BigDecimal getCyberHead() {
    return cyberHead;
  }

  public void setCyberHead(@Nullable BigDecimal cyberHead) {
    this.cyberHead = cyberHead;
  }

  public BodyPartModifiersCyber cyberTorso(@Nullable BigDecimal cyberTorso) {
    this.cyberTorso = cyberTorso;
    return this;
  }

  /**
   * Get cyberTorso
   * @return cyberTorso
   */
  @Valid 
  @Schema(name = "cyber_torso", example = "0.8", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cyber_torso")
  public @Nullable BigDecimal getCyberTorso() {
    return cyberTorso;
  }

  public void setCyberTorso(@Nullable BigDecimal cyberTorso) {
    this.cyberTorso = cyberTorso;
  }

  public BodyPartModifiersCyber cyberArms(@Nullable BigDecimal cyberArms) {
    this.cyberArms = cyberArms;
    return this;
  }

  /**
   * Get cyberArms
   * @return cyberArms
   */
  @Valid 
  @Schema(name = "cyber_arms", example = "0.5", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cyber_arms")
  public @Nullable BigDecimal getCyberArms() {
    return cyberArms;
  }

  public void setCyberArms(@Nullable BigDecimal cyberArms) {
    this.cyberArms = cyberArms;
  }

  public BodyPartModifiersCyber cyberLegs(@Nullable BigDecimal cyberLegs) {
    this.cyberLegs = cyberLegs;
    return this;
  }

  /**
   * Get cyberLegs
   * @return cyberLegs
   */
  @Valid 
  @Schema(name = "cyber_legs", example = "0.5", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cyber_legs")
  public @Nullable BigDecimal getCyberLegs() {
    return cyberLegs;
  }

  public void setCyberLegs(@Nullable BigDecimal cyberLegs) {
    this.cyberLegs = cyberLegs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BodyPartModifiersCyber bodyPartModifiersCyber = (BodyPartModifiersCyber) o;
    return Objects.equals(this.cyberHead, bodyPartModifiersCyber.cyberHead) &&
        Objects.equals(this.cyberTorso, bodyPartModifiersCyber.cyberTorso) &&
        Objects.equals(this.cyberArms, bodyPartModifiersCyber.cyberArms) &&
        Objects.equals(this.cyberLegs, bodyPartModifiersCyber.cyberLegs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cyberHead, cyberTorso, cyberArms, cyberLegs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BodyPartModifiersCyber {\n");
    sb.append("    cyberHead: ").append(toIndentedString(cyberHead)).append("\n");
    sb.append("    cyberTorso: ").append(toIndentedString(cyberTorso)).append("\n");
    sb.append("    cyberArms: ").append(toIndentedString(cyberArms)).append("\n");
    sb.append("    cyberLegs: ").append(toIndentedString(cyberLegs)).append("\n");
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

