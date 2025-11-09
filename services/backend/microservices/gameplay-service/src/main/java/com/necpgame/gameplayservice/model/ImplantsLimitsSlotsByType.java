package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.ImplantSlots;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Слоты по типам имплантов
 */

@Schema(name = "ImplantsLimits_slots_by_type", description = "Слоты по типам имплантов")
@JsonTypeName("ImplantsLimits_slots_by_type")

public class ImplantsLimitsSlotsByType {

  private @Nullable ImplantSlots combat;

  private @Nullable ImplantSlots tactical;

  private @Nullable ImplantSlots defensive;

  private @Nullable ImplantSlots mobility;

  private @Nullable ImplantSlots os;

  public ImplantsLimitsSlotsByType combat(@Nullable ImplantSlots combat) {
    this.combat = combat;
    return this;
  }

  /**
   * Get combat
   * @return combat
   */
  @Valid 
  @Schema(name = "combat", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("combat")
  public @Nullable ImplantSlots getCombat() {
    return combat;
  }

  public void setCombat(@Nullable ImplantSlots combat) {
    this.combat = combat;
  }

  public ImplantsLimitsSlotsByType tactical(@Nullable ImplantSlots tactical) {
    this.tactical = tactical;
    return this;
  }

  /**
   * Get tactical
   * @return tactical
   */
  @Valid 
  @Schema(name = "tactical", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tactical")
  public @Nullable ImplantSlots getTactical() {
    return tactical;
  }

  public void setTactical(@Nullable ImplantSlots tactical) {
    this.tactical = tactical;
  }

  public ImplantsLimitsSlotsByType defensive(@Nullable ImplantSlots defensive) {
    this.defensive = defensive;
    return this;
  }

  /**
   * Get defensive
   * @return defensive
   */
  @Valid 
  @Schema(name = "defensive", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("defensive")
  public @Nullable ImplantSlots getDefensive() {
    return defensive;
  }

  public void setDefensive(@Nullable ImplantSlots defensive) {
    this.defensive = defensive;
  }

  public ImplantsLimitsSlotsByType mobility(@Nullable ImplantSlots mobility) {
    this.mobility = mobility;
    return this;
  }

  /**
   * Get mobility
   * @return mobility
   */
  @Valid 
  @Schema(name = "mobility", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mobility")
  public @Nullable ImplantSlots getMobility() {
    return mobility;
  }

  public void setMobility(@Nullable ImplantSlots mobility) {
    this.mobility = mobility;
  }

  public ImplantsLimitsSlotsByType os(@Nullable ImplantSlots os) {
    this.os = os;
    return this;
  }

  /**
   * Get os
   * @return os
   */
  @Valid 
  @Schema(name = "os", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("os")
  public @Nullable ImplantSlots getOs() {
    return os;
  }

  public void setOs(@Nullable ImplantSlots os) {
    this.os = os;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImplantsLimitsSlotsByType implantsLimitsSlotsByType = (ImplantsLimitsSlotsByType) o;
    return Objects.equals(this.combat, implantsLimitsSlotsByType.combat) &&
        Objects.equals(this.tactical, implantsLimitsSlotsByType.tactical) &&
        Objects.equals(this.defensive, implantsLimitsSlotsByType.defensive) &&
        Objects.equals(this.mobility, implantsLimitsSlotsByType.mobility) &&
        Objects.equals(this.os, implantsLimitsSlotsByType.os);
  }

  @Override
  public int hashCode() {
    return Objects.hash(combat, tactical, defensive, mobility, os);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImplantsLimitsSlotsByType {\n");
    sb.append("    combat: ").append(toIndentedString(combat)).append("\n");
    sb.append("    tactical: ").append(toIndentedString(tactical)).append("\n");
    sb.append("    defensive: ").append(toIndentedString(defensive)).append("\n");
    sb.append("    mobility: ").append(toIndentedString(mobility)).append("\n");
    sb.append("    os: ").append(toIndentedString(os)).append("\n");
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

