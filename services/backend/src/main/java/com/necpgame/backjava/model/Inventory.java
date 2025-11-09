package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.Container;
import com.necpgame.backjava.model.WeightInfo;
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
 * Inventory
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class Inventory {

  private @Nullable String playerId;

  @Valid
  private List<@Valid Container> containers = new ArrayList<>();

  private @Nullable WeightInfo weight;

  @Valid
  private List<String> capacityModifiers = new ArrayList<>();

  private @Nullable Boolean overloaded;

  public Inventory playerId(@Nullable String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerId")
  public @Nullable String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable String playerId) {
    this.playerId = playerId;
  }

  public Inventory containers(List<@Valid Container> containers) {
    this.containers = containers;
    return this;
  }

  public Inventory addContainersItem(Container containersItem) {
    if (this.containers == null) {
      this.containers = new ArrayList<>();
    }
    this.containers.add(containersItem);
    return this;
  }

  /**
   * Get containers
   * @return containers
   */
  @Valid 
  @Schema(name = "containers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("containers")
  public List<@Valid Container> getContainers() {
    return containers;
  }

  public void setContainers(List<@Valid Container> containers) {
    this.containers = containers;
  }

  public Inventory weight(@Nullable WeightInfo weight) {
    this.weight = weight;
    return this;
  }

  /**
   * Get weight
   * @return weight
   */
  @Valid 
  @Schema(name = "weight", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weight")
  public @Nullable WeightInfo getWeight() {
    return weight;
  }

  public void setWeight(@Nullable WeightInfo weight) {
    this.weight = weight;
  }

  public Inventory capacityModifiers(List<String> capacityModifiers) {
    this.capacityModifiers = capacityModifiers;
    return this;
  }

  public Inventory addCapacityModifiersItem(String capacityModifiersItem) {
    if (this.capacityModifiers == null) {
      this.capacityModifiers = new ArrayList<>();
    }
    this.capacityModifiers.add(capacityModifiersItem);
    return this;
  }

  /**
   * Get capacityModifiers
   * @return capacityModifiers
   */
  
  @Schema(name = "capacityModifiers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("capacityModifiers")
  public List<String> getCapacityModifiers() {
    return capacityModifiers;
  }

  public void setCapacityModifiers(List<String> capacityModifiers) {
    this.capacityModifiers = capacityModifiers;
  }

  public Inventory overloaded(@Nullable Boolean overloaded) {
    this.overloaded = overloaded;
    return this;
  }

  /**
   * Get overloaded
   * @return overloaded
   */
  
  @Schema(name = "overloaded", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("overloaded")
  public @Nullable Boolean getOverloaded() {
    return overloaded;
  }

  public void setOverloaded(@Nullable Boolean overloaded) {
    this.overloaded = overloaded;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Inventory inventory = (Inventory) o;
    return Objects.equals(this.playerId, inventory.playerId) &&
        Objects.equals(this.containers, inventory.containers) &&
        Objects.equals(this.weight, inventory.weight) &&
        Objects.equals(this.capacityModifiers, inventory.capacityModifiers) &&
        Objects.equals(this.overloaded, inventory.overloaded);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, containers, weight, capacityModifiers, overloaded);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Inventory {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    containers: ").append(toIndentedString(containers)).append("\n");
    sb.append("    weight: ").append(toIndentedString(weight)).append("\n");
    sb.append("    capacityModifiers: ").append(toIndentedString(capacityModifiers)).append("\n");
    sb.append("    overloaded: ").append(toIndentedString(overloaded)).append("\n");
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

