package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AbilitySlotAssignment
 */


public class AbilitySlotAssignment {

  private String slot;

  private String abilityId;

  public AbilitySlotAssignment() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AbilitySlotAssignment(String slot, String abilityId) {
    this.slot = slot;
    this.abilityId = abilityId;
  }

  public AbilitySlotAssignment slot(String slot) {
    this.slot = slot;
    return this;
  }

  /**
   * Слот способности (active_1, passive_1, ultimate)
   * @return slot
   */
  @NotNull 
  @Schema(name = "slot", description = "Слот способности (active_1, passive_1, ultimate)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("slot")
  public String getSlot() {
    return slot;
  }

  public void setSlot(String slot) {
    this.slot = slot;
  }

  public AbilitySlotAssignment abilityId(String abilityId) {
    this.abilityId = abilityId;
    return this;
  }

  /**
   * Идентификатор способности из каталога
   * @return abilityId
   */
  @NotNull 
  @Schema(name = "abilityId", description = "Идентификатор способности из каталога", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("abilityId")
  public String getAbilityId() {
    return abilityId;
  }

  public void setAbilityId(String abilityId) {
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
    AbilitySlotAssignment abilitySlotAssignment = (AbilitySlotAssignment) o;
    return Objects.equals(this.slot, abilitySlotAssignment.slot) &&
        Objects.equals(this.abilityId, abilitySlotAssignment.abilityId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(slot, abilityId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbilitySlotAssignment {\n");
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

