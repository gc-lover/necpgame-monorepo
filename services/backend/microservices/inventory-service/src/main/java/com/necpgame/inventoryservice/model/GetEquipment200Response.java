package com.necpgame.inventoryservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.inventoryservice.model.EquipmentSlot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetEquipment200Response
 */

@JsonTypeName("getEquipment_200_response")

public class GetEquipment200Response {

  @Valid
  private List<@Valid EquipmentSlot> slots = new ArrayList<>();

  @Valid
  private Map<String, Integer> totalBonuses = new HashMap<>();

  public GetEquipment200Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GetEquipment200Response(List<@Valid EquipmentSlot> slots, Map<String, Integer> totalBonuses) {
    this.slots = slots;
    this.totalBonuses = totalBonuses;
  }

  public GetEquipment200Response slots(List<@Valid EquipmentSlot> slots) {
    this.slots = slots;
    return this;
  }

  public GetEquipment200Response addSlotsItem(EquipmentSlot slotsItem) {
    if (this.slots == null) {
      this.slots = new ArrayList<>();
    }
    this.slots.add(slotsItem);
    return this;
  }

  /**
   * Слоты экипировки
   * @return slots
   */
  @NotNull @Valid 
  @Schema(name = "slots", description = "Слоты экипировки", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("slots")
  public List<@Valid EquipmentSlot> getSlots() {
    return slots;
  }

  public void setSlots(List<@Valid EquipmentSlot> slots) {
    this.slots = slots;
  }

  public GetEquipment200Response totalBonuses(Map<String, Integer> totalBonuses) {
    this.totalBonuses = totalBonuses;
    return this;
  }

  public GetEquipment200Response putTotalBonusesItem(String key, Integer totalBonusesItem) {
    if (this.totalBonuses == null) {
      this.totalBonuses = new HashMap<>();
    }
    this.totalBonuses.put(key, totalBonusesItem);
    return this;
  }

  /**
   * Суммарные бонусы от всей экипировки
   * @return totalBonuses
   */
  @NotNull 
  @Schema(name = "totalBonuses", example = "{\"health\":50,\"armor\":25,\"damage\":15}", description = "Суммарные бонусы от всей экипировки", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("totalBonuses")
  public Map<String, Integer> getTotalBonuses() {
    return totalBonuses;
  }

  public void setTotalBonuses(Map<String, Integer> totalBonuses) {
    this.totalBonuses = totalBonuses;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetEquipment200Response getEquipment200Response = (GetEquipment200Response) o;
    return Objects.equals(this.slots, getEquipment200Response.slots) &&
        Objects.equals(this.totalBonuses, getEquipment200Response.totalBonuses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(slots, totalBonuses);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetEquipment200Response {\n");
    sb.append("    slots: ").append(toIndentedString(slots)).append("\n");
    sb.append("    totalBonuses: ").append(toIndentedString(totalBonuses)).append("\n");
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

