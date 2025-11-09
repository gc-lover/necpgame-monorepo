package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.MainGameUIDataInventorySlotsInner;
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
 * MainGameUIDataInventory
 */

@JsonTypeName("MainGameUIData_inventory")

public class MainGameUIDataInventory {

  @Valid
  private List<@Valid MainGameUIDataInventorySlotsInner> slots = new ArrayList<>();

  private @Nullable Float weight;

  public MainGameUIDataInventory slots(List<@Valid MainGameUIDataInventorySlotsInner> slots) {
    this.slots = slots;
    return this;
  }

  public MainGameUIDataInventory addSlotsItem(MainGameUIDataInventorySlotsInner slotsItem) {
    if (this.slots == null) {
      this.slots = new ArrayList<>();
    }
    this.slots.add(slotsItem);
    return this;
  }

  /**
   * Слоты инвентаря с предметами
   * @return slots
   */
  @Valid 
  @Schema(name = "slots", description = "Слоты инвентаря с предметами", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slots")
  public List<@Valid MainGameUIDataInventorySlotsInner> getSlots() {
    return slots;
  }

  public void setSlots(List<@Valid MainGameUIDataInventorySlotsInner> slots) {
    this.slots = slots;
  }

  public MainGameUIDataInventory weight(@Nullable Float weight) {
    this.weight = weight;
    return this;
  }

  /**
   * Get weight
   * @return weight
   */
  
  @Schema(name = "weight", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weight")
  public @Nullable Float getWeight() {
    return weight;
  }

  public void setWeight(@Nullable Float weight) {
    this.weight = weight;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MainGameUIDataInventory mainGameUIDataInventory = (MainGameUIDataInventory) o;
    return Objects.equals(this.slots, mainGameUIDataInventory.slots) &&
        Objects.equals(this.weight, mainGameUIDataInventory.weight);
  }

  @Override
  public int hashCode() {
    return Objects.hash(slots, weight);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MainGameUIDataInventory {\n");
    sb.append("    slots: ").append(toIndentedString(slots)).append("\n");
    sb.append("    weight: ").append(toIndentedString(weight)).append("\n");
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

