package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.SlotInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

@Schema(name = "ImplantSlots_slots_by_type", description = "Слоты по типам имплантов")
@JsonTypeName("ImplantSlots_slots_by_type")

public class ImplantSlotsSlotsByType {

  @Valid
  private List<@Valid SlotInfo> combat = new ArrayList<>();

  @Valid
  private List<@Valid SlotInfo> tactical = new ArrayList<>();

  @Valid
  private List<@Valid SlotInfo> defensive = new ArrayList<>();

  @Valid
  private List<@Valid SlotInfo> mobility = new ArrayList<>();

  @Valid
  private List<@Valid SlotInfo> os = new ArrayList<>();

  public ImplantSlotsSlotsByType combat(List<@Valid SlotInfo> combat) {
    this.combat = combat;
    return this;
  }

  public ImplantSlotsSlotsByType addCombatItem(SlotInfo combatItem) {
    if (this.combat == null) {
      this.combat = new ArrayList<>();
    }
    this.combat.add(combatItem);
    return this;
  }

  /**
   * Get combat
   * @return combat
   */
  @Valid 
  @Schema(name = "combat", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("combat")
  public List<@Valid SlotInfo> getCombat() {
    return combat;
  }

  public void setCombat(List<@Valid SlotInfo> combat) {
    this.combat = combat;
  }

  public ImplantSlotsSlotsByType tactical(List<@Valid SlotInfo> tactical) {
    this.tactical = tactical;
    return this;
  }

  public ImplantSlotsSlotsByType addTacticalItem(SlotInfo tacticalItem) {
    if (this.tactical == null) {
      this.tactical = new ArrayList<>();
    }
    this.tactical.add(tacticalItem);
    return this;
  }

  /**
   * Get tactical
   * @return tactical
   */
  @Valid 
  @Schema(name = "tactical", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tactical")
  public List<@Valid SlotInfo> getTactical() {
    return tactical;
  }

  public void setTactical(List<@Valid SlotInfo> tactical) {
    this.tactical = tactical;
  }

  public ImplantSlotsSlotsByType defensive(List<@Valid SlotInfo> defensive) {
    this.defensive = defensive;
    return this;
  }

  public ImplantSlotsSlotsByType addDefensiveItem(SlotInfo defensiveItem) {
    if (this.defensive == null) {
      this.defensive = new ArrayList<>();
    }
    this.defensive.add(defensiveItem);
    return this;
  }

  /**
   * Get defensive
   * @return defensive
   */
  @Valid 
  @Schema(name = "defensive", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("defensive")
  public List<@Valid SlotInfo> getDefensive() {
    return defensive;
  }

  public void setDefensive(List<@Valid SlotInfo> defensive) {
    this.defensive = defensive;
  }

  public ImplantSlotsSlotsByType mobility(List<@Valid SlotInfo> mobility) {
    this.mobility = mobility;
    return this;
  }

  public ImplantSlotsSlotsByType addMobilityItem(SlotInfo mobilityItem) {
    if (this.mobility == null) {
      this.mobility = new ArrayList<>();
    }
    this.mobility.add(mobilityItem);
    return this;
  }

  /**
   * Get mobility
   * @return mobility
   */
  @Valid 
  @Schema(name = "mobility", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mobility")
  public List<@Valid SlotInfo> getMobility() {
    return mobility;
  }

  public void setMobility(List<@Valid SlotInfo> mobility) {
    this.mobility = mobility;
  }

  public ImplantSlotsSlotsByType os(List<@Valid SlotInfo> os) {
    this.os = os;
    return this;
  }

  public ImplantSlotsSlotsByType addOsItem(SlotInfo osItem) {
    if (this.os == null) {
      this.os = new ArrayList<>();
    }
    this.os.add(osItem);
    return this;
  }

  /**
   * Get os
   * @return os
   */
  @Valid 
  @Schema(name = "os", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("os")
  public List<@Valid SlotInfo> getOs() {
    return os;
  }

  public void setOs(List<@Valid SlotInfo> os) {
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
    ImplantSlotsSlotsByType implantSlotsSlotsByType = (ImplantSlotsSlotsByType) o;
    return Objects.equals(this.combat, implantSlotsSlotsByType.combat) &&
        Objects.equals(this.tactical, implantSlotsSlotsByType.tactical) &&
        Objects.equals(this.defensive, implantSlotsSlotsByType.defensive) &&
        Objects.equals(this.mobility, implantSlotsSlotsByType.mobility) &&
        Objects.equals(this.os, implantSlotsSlotsByType.os);
  }

  @Override
  public int hashCode() {
    return Objects.hash(combat, tactical, defensive, mobility, os);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImplantSlotsSlotsByType {\n");
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

