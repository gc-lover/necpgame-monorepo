package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.StorageUpgrade;
import com.necpgame.backjava.model.StoredItem;
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
 * StorageStatus
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class StorageStatus {

  private @Nullable Integer capacity;

  private @Nullable Integer usedSlots;

  @Valid
  private List<@Valid StorageUpgrade> upgrades = new ArrayList<>();

  @Valid
  private List<@Valid StoredItem> storedItems = new ArrayList<>();

  public StorageStatus capacity(@Nullable Integer capacity) {
    this.capacity = capacity;
    return this;
  }

  /**
   * Get capacity
   * @return capacity
   */
  
  @Schema(name = "capacity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("capacity")
  public @Nullable Integer getCapacity() {
    return capacity;
  }

  public void setCapacity(@Nullable Integer capacity) {
    this.capacity = capacity;
  }

  public StorageStatus usedSlots(@Nullable Integer usedSlots) {
    this.usedSlots = usedSlots;
    return this;
  }

  /**
   * Get usedSlots
   * @return usedSlots
   */
  
  @Schema(name = "usedSlots", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("usedSlots")
  public @Nullable Integer getUsedSlots() {
    return usedSlots;
  }

  public void setUsedSlots(@Nullable Integer usedSlots) {
    this.usedSlots = usedSlots;
  }

  public StorageStatus upgrades(List<@Valid StorageUpgrade> upgrades) {
    this.upgrades = upgrades;
    return this;
  }

  public StorageStatus addUpgradesItem(StorageUpgrade upgradesItem) {
    if (this.upgrades == null) {
      this.upgrades = new ArrayList<>();
    }
    this.upgrades.add(upgradesItem);
    return this;
  }

  /**
   * Get upgrades
   * @return upgrades
   */
  @Valid 
  @Schema(name = "upgrades", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("upgrades")
  public List<@Valid StorageUpgrade> getUpgrades() {
    return upgrades;
  }

  public void setUpgrades(List<@Valid StorageUpgrade> upgrades) {
    this.upgrades = upgrades;
  }

  public StorageStatus storedItems(List<@Valid StoredItem> storedItems) {
    this.storedItems = storedItems;
    return this;
  }

  public StorageStatus addStoredItemsItem(StoredItem storedItemsItem) {
    if (this.storedItems == null) {
      this.storedItems = new ArrayList<>();
    }
    this.storedItems.add(storedItemsItem);
    return this;
  }

  /**
   * Get storedItems
   * @return storedItems
   */
  @Valid 
  @Schema(name = "storedItems", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("storedItems")
  public List<@Valid StoredItem> getStoredItems() {
    return storedItems;
  }

  public void setStoredItems(List<@Valid StoredItem> storedItems) {
    this.storedItems = storedItems;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StorageStatus storageStatus = (StorageStatus) o;
    return Objects.equals(this.capacity, storageStatus.capacity) &&
        Objects.equals(this.usedSlots, storageStatus.usedSlots) &&
        Objects.equals(this.upgrades, storageStatus.upgrades) &&
        Objects.equals(this.storedItems, storageStatus.storedItems);
  }

  @Override
  public int hashCode() {
    return Objects.hash(capacity, usedSlots, upgrades, storedItems);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StorageStatus {\n");
    sb.append("    capacity: ").append(toIndentedString(capacity)).append("\n");
    sb.append("    usedSlots: ").append(toIndentedString(usedSlots)).append("\n");
    sb.append("    upgrades: ").append(toIndentedString(upgrades)).append("\n");
    sb.append("    storedItems: ").append(toIndentedString(storedItems)).append("\n");
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

