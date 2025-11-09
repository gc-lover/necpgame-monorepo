package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.ImplantSlotsSlotsByType;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * РЎР»РѕС‚С‹ РёРјРїР»Р°РЅС‚РѕРІ РёРіСЂРѕРєР° РїРѕ С‚РёРїР°Рј. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-implants-limits.md -&gt; РћРіСЂР°РЅРёС‡РµРЅРёСЏ РёРјРїР»Р°РЅС‚РѕРІ 
 */

@Schema(name = "ImplantSlots", description = "РЎР»РѕС‚С‹ РёРјРїР»Р°РЅС‚РѕРІ РёРіСЂРѕРєР° РїРѕ С‚РёРїР°Рј. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-implants-limits.md -> РћРіСЂР°РЅРёС‡РµРЅРёСЏ РёРјРїР»Р°РЅС‚РѕРІ ")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:51:47.912860600+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
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

