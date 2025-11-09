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
 * Бонусы от класса персонажа
 */

@Schema(name = "ImplantsLimits_class_bonuses", description = "Бонусы от класса персонажа")
@JsonTypeName("ImplantsLimits_class_bonuses")

public class ImplantsLimitsClassBonuses {

  private @Nullable Integer additionalSlots;

  private @Nullable String slotTypeBonus;

  public ImplantsLimitsClassBonuses additionalSlots(@Nullable Integer additionalSlots) {
    this.additionalSlots = additionalSlots;
    return this;
  }

  /**
   * Get additionalSlots
   * @return additionalSlots
   */
  
  @Schema(name = "additional_slots", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("additional_slots")
  public @Nullable Integer getAdditionalSlots() {
    return additionalSlots;
  }

  public void setAdditionalSlots(@Nullable Integer additionalSlots) {
    this.additionalSlots = additionalSlots;
  }

  public ImplantsLimitsClassBonuses slotTypeBonus(@Nullable String slotTypeBonus) {
    this.slotTypeBonus = slotTypeBonus;
    return this;
  }

  /**
   * Тип слотов с бонусом
   * @return slotTypeBonus
   */
  
  @Schema(name = "slot_type_bonus", description = "Тип слотов с бонусом", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slot_type_bonus")
  public @Nullable String getSlotTypeBonus() {
    return slotTypeBonus;
  }

  public void setSlotTypeBonus(@Nullable String slotTypeBonus) {
    this.slotTypeBonus = slotTypeBonus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImplantsLimitsClassBonuses implantsLimitsClassBonuses = (ImplantsLimitsClassBonuses) o;
    return Objects.equals(this.additionalSlots, implantsLimitsClassBonuses.additionalSlots) &&
        Objects.equals(this.slotTypeBonus, implantsLimitsClassBonuses.slotTypeBonus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(additionalSlots, slotTypeBonus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImplantsLimitsClassBonuses {\n");
    sb.append("    additionalSlots: ").append(toIndentedString(additionalSlots)).append("\n");
    sb.append("    slotTypeBonus: ").append(toIndentedString(slotTypeBonus)).append("\n");
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

