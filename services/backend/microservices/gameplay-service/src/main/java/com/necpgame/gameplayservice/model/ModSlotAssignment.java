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
 * ModSlotAssignment
 */


public class ModSlotAssignment {

  private String slot;

  private String modId;

  private @Nullable String rarityRequirement;

  public ModSlotAssignment() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ModSlotAssignment(String slot, String modId) {
    this.slot = slot;
    this.modId = modId;
  }

  public ModSlotAssignment slot(String slot) {
    this.slot = slot;
    return this;
  }

  /**
   * Слот модуля (core, augment, chip)
   * @return slot
   */
  @NotNull 
  @Schema(name = "slot", description = "Слот модуля (core, augment, chip)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("slot")
  public String getSlot() {
    return slot;
  }

  public void setSlot(String slot) {
    this.slot = slot;
  }

  public ModSlotAssignment modId(String modId) {
    this.modId = modId;
    return this;
  }

  /**
   * Get modId
   * @return modId
   */
  @NotNull 
  @Schema(name = "modId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("modId")
  public String getModId() {
    return modId;
  }

  public void setModId(String modId) {
    this.modId = modId;
  }

  public ModSlotAssignment rarityRequirement(@Nullable String rarityRequirement) {
    this.rarityRequirement = rarityRequirement;
    return this;
  }

  /**
   * Get rarityRequirement
   * @return rarityRequirement
   */
  
  @Schema(name = "rarityRequirement", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rarityRequirement")
  public @Nullable String getRarityRequirement() {
    return rarityRequirement;
  }

  public void setRarityRequirement(@Nullable String rarityRequirement) {
    this.rarityRequirement = rarityRequirement;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModSlotAssignment modSlotAssignment = (ModSlotAssignment) o;
    return Objects.equals(this.slot, modSlotAssignment.slot) &&
        Objects.equals(this.modId, modSlotAssignment.modId) &&
        Objects.equals(this.rarityRequirement, modSlotAssignment.rarityRequirement);
  }

  @Override
  public int hashCode() {
    return Objects.hash(slot, modId, rarityRequirement);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModSlotAssignment {\n");
    sb.append("    slot: ").append(toIndentedString(slot)).append("\n");
    sb.append("    modId: ").append(toIndentedString(modId)).append("\n");
    sb.append("    rarityRequirement: ").append(toIndentedString(rarityRequirement)).append("\n");
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

