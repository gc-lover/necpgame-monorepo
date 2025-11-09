package com.necpgame.inventoryservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.inventoryservice.model.Slot;
import java.math.BigDecimal;
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
 * Container
 */


public class Container {

  private @Nullable String containerId;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    BACKPACK("BACKPACK"),
    
    EQUIPMENT("EQUIPMENT"),
    
    STASH("STASH"),
    
    COSMETIC("COSMETIC"),
    
    TEMPORARY("TEMPORARY");

    private final String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  private @Nullable String name;

  private @Nullable Integer capacitySlots;

  private @Nullable BigDecimal capacityWeight;

  @Valid
  private List<String> filters = new ArrayList<>();

  @Valid
  private List<@Valid Slot> slots = new ArrayList<>();

  public Container containerId(@Nullable String containerId) {
    this.containerId = containerId;
    return this;
  }

  /**
   * Get containerId
   * @return containerId
   */
  
  @Schema(name = "containerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("containerId")
  public @Nullable String getContainerId() {
    return containerId;
  }

  public void setContainerId(@Nullable String containerId) {
    this.containerId = containerId;
  }

  public Container type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public Container name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public Container capacitySlots(@Nullable Integer capacitySlots) {
    this.capacitySlots = capacitySlots;
    return this;
  }

  /**
   * Get capacitySlots
   * @return capacitySlots
   */
  
  @Schema(name = "capacitySlots", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("capacitySlots")
  public @Nullable Integer getCapacitySlots() {
    return capacitySlots;
  }

  public void setCapacitySlots(@Nullable Integer capacitySlots) {
    this.capacitySlots = capacitySlots;
  }

  public Container capacityWeight(@Nullable BigDecimal capacityWeight) {
    this.capacityWeight = capacityWeight;
    return this;
  }

  /**
   * Get capacityWeight
   * @return capacityWeight
   */
  @Valid 
  @Schema(name = "capacityWeight", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("capacityWeight")
  public @Nullable BigDecimal getCapacityWeight() {
    return capacityWeight;
  }

  public void setCapacityWeight(@Nullable BigDecimal capacityWeight) {
    this.capacityWeight = capacityWeight;
  }

  public Container filters(List<String> filters) {
    this.filters = filters;
    return this;
  }

  public Container addFiltersItem(String filtersItem) {
    if (this.filters == null) {
      this.filters = new ArrayList<>();
    }
    this.filters.add(filtersItem);
    return this;
  }

  /**
   * Get filters
   * @return filters
   */
  
  @Schema(name = "filters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("filters")
  public List<String> getFilters() {
    return filters;
  }

  public void setFilters(List<String> filters) {
    this.filters = filters;
  }

  public Container slots(List<@Valid Slot> slots) {
    this.slots = slots;
    return this;
  }

  public Container addSlotsItem(Slot slotsItem) {
    if (this.slots == null) {
      this.slots = new ArrayList<>();
    }
    this.slots.add(slotsItem);
    return this;
  }

  /**
   * Get slots
   * @return slots
   */
  @Valid 
  @Schema(name = "slots", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slots")
  public List<@Valid Slot> getSlots() {
    return slots;
  }

  public void setSlots(List<@Valid Slot> slots) {
    this.slots = slots;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Container container = (Container) o;
    return Objects.equals(this.containerId, container.containerId) &&
        Objects.equals(this.type, container.type) &&
        Objects.equals(this.name, container.name) &&
        Objects.equals(this.capacitySlots, container.capacitySlots) &&
        Objects.equals(this.capacityWeight, container.capacityWeight) &&
        Objects.equals(this.filters, container.filters) &&
        Objects.equals(this.slots, container.slots);
  }

  @Override
  public int hashCode() {
    return Objects.hash(containerId, type, name, capacitySlots, capacityWeight, filters, slots);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Container {\n");
    sb.append("    containerId: ").append(toIndentedString(containerId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    capacitySlots: ").append(toIndentedString(capacitySlots)).append("\n");
    sb.append("    capacityWeight: ").append(toIndentedString(capacityWeight)).append("\n");
    sb.append("    filters: ").append(toIndentedString(filters)).append("\n");
    sb.append("    slots: ").append(toIndentedString(slots)).append("\n");
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

