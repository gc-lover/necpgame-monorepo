package com.necpgame.adminservice.model;

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
 * HUDDataQuickSlotsInner
 */

@JsonTypeName("HUDData_quick_slots_inner")

public class HUDDataQuickSlotsInner {

  private @Nullable Integer slot;

  private @Nullable String abilityId;

  public HUDDataQuickSlotsInner slot(@Nullable Integer slot) {
    this.slot = slot;
    return this;
  }

  /**
   * Get slot
   * @return slot
   */
  
  @Schema(name = "slot", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slot")
  public @Nullable Integer getSlot() {
    return slot;
  }

  public void setSlot(@Nullable Integer slot) {
    this.slot = slot;
  }

  public HUDDataQuickSlotsInner abilityId(@Nullable String abilityId) {
    this.abilityId = abilityId;
    return this;
  }

  /**
   * Get abilityId
   * @return abilityId
   */
  
  @Schema(name = "ability_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ability_id")
  public @Nullable String getAbilityId() {
    return abilityId;
  }

  public void setAbilityId(@Nullable String abilityId) {
    this.abilityId = abilityId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HUDDataQuickSlotsInner huDDataQuickSlotsInner = (HUDDataQuickSlotsInner) o;
    return Objects.equals(this.slot, huDDataQuickSlotsInner.slot) &&
        Objects.equals(this.abilityId, huDDataQuickSlotsInner.abilityId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(slot, abilityId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HUDDataQuickSlotsInner {\n");
    sb.append("    slot: ").append(toIndentedString(slot)).append("\n");
    sb.append("    abilityId: ").append(toIndentedString(abilityId)).append("\n");
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

