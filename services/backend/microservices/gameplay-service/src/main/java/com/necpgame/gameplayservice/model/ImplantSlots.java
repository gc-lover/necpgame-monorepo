package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.ImplantSlotsSlotsByType;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Слоты имплантов игрока по типам. Источник: .BRAIN/02-gameplay/combat/combat-implants-limits.md -&gt; Ограничения имплантов 
 */

@Schema(name = "ImplantSlots", description = "Слоты имплантов игрока по типам. Источник: .BRAIN/02-gameplay/combat/combat-implants-limits.md -> Ограничения имплантов ")

public class ImplantSlots {

  private ImplantSlotsSlotsByType slotsByType;

  public ImplantSlots() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ImplantSlots(ImplantSlotsSlotsByType slotsByType) {
    this.slotsByType = slotsByType;
  }

  public ImplantSlots slotsByType(ImplantSlotsSlotsByType slotsByType) {
    this.slotsByType = slotsByType;
    return this;
  }

  /**
   * Get slotsByType
   * @return slotsByType
   */
  @NotNull @Valid 
  @Schema(name = "slots_by_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("slots_by_type")
  public ImplantSlotsSlotsByType getSlotsByType() {
    return slotsByType;
  }

  public void setSlotsByType(ImplantSlotsSlotsByType slotsByType) {
    this.slotsByType = slotsByType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImplantSlots implantSlots = (ImplantSlots) o;
    return Objects.equals(this.slotsByType, implantSlots.slotsByType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(slotsByType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImplantSlots {\n");
    sb.append("    slotsByType: ").append(toIndentedString(slotsByType)).append("\n");
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

