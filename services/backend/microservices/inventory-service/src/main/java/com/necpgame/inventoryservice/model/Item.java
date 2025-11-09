package com.necpgame.inventoryservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.inventoryservice.model.ItemDurability;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Item
 */


public class Item {

  private String itemInstanceId;

  private String itemId;

  private @Nullable String name;

  private @Nullable String rarity;

  private @Nullable String type;

  private Integer quantity;

  private @Nullable Integer stackSize;

  private @Nullable BigDecimal weight;

  /**
   * Gets or Sets boundType
   */
  public enum BoundTypeEnum {
    NONE("NONE"),
    
    ACCOUNT("ACCOUNT"),
    
    CHARACTER("CHARACTER");

    private final String value;

    BoundTypeEnum(String value) {
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
    public static BoundTypeEnum fromValue(String value) {
      for (BoundTypeEnum b : BoundTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable BoundTypeEnum boundType;

  private @Nullable ItemDurability durability;

  @Valid
  private Map<String, Object> stats = new HashMap<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  @Valid
  private Map<String, Object> metadata = new HashMap<>();

  public Item() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Item(String itemInstanceId, String itemId, Integer quantity) {
    this.itemInstanceId = itemInstanceId;
    this.itemId = itemId;
    this.quantity = quantity;
  }

  public Item itemInstanceId(String itemInstanceId) {
    this.itemInstanceId = itemInstanceId;
    return this;
  }

  /**
   * Get itemInstanceId
   * @return itemInstanceId
   */
  @NotNull 
  @Schema(name = "itemInstanceId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("itemInstanceId")
  public String getItemInstanceId() {
    return itemInstanceId;
  }

  public void setItemInstanceId(String itemInstanceId) {
    this.itemInstanceId = itemInstanceId;
  }

  public Item itemId(String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  @NotNull 
  @Schema(name = "itemId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("itemId")
  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public Item name(@Nullable String name) {
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

  public Item rarity(@Nullable String rarity) {
    this.rarity = rarity;
    return this;
  }

  /**
   * Get rarity
   * @return rarity
   */
  
  @Schema(name = "rarity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rarity")
  public @Nullable String getRarity() {
    return rarity;
  }

  public void setRarity(@Nullable String rarity) {
    this.rarity = rarity;
  }

  public Item type(@Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable String getType() {
    return type;
  }

  public void setType(@Nullable String type) {
    this.type = type;
  }

  public Item quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
   */
  @NotNull 
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("quantity")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public Item stackSize(@Nullable Integer stackSize) {
    this.stackSize = stackSize;
    return this;
  }

  /**
   * Get stackSize
   * @return stackSize
   */
  
  @Schema(name = "stackSize", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stackSize")
  public @Nullable Integer getStackSize() {
    return stackSize;
  }

  public void setStackSize(@Nullable Integer stackSize) {
    this.stackSize = stackSize;
  }

  public Item weight(@Nullable BigDecimal weight) {
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
  public @Nullable BigDecimal getWeight() {
    return weight;
  }

  public void setWeight(@Nullable BigDecimal weight) {
    this.weight = weight;
  }

  public Item boundType(@Nullable BoundTypeEnum boundType) {
    this.boundType = boundType;
    return this;
  }

  /**
   * Get boundType
   * @return boundType
   */
  
  @Schema(name = "boundType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("boundType")
  public @Nullable BoundTypeEnum getBoundType() {
    return boundType;
  }

  public void setBoundType(@Nullable BoundTypeEnum boundType) {
    this.boundType = boundType;
  }

  public Item durability(@Nullable ItemDurability durability) {
    this.durability = durability;
    return this;
  }

  /**
   * Get durability
   * @return durability
   */
  @Valid 
  @Schema(name = "durability", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("durability")
  public @Nullable ItemDurability getDurability() {
    return durability;
  }

  public void setDurability(@Nullable ItemDurability durability) {
    this.durability = durability;
  }

  public Item stats(Map<String, Object> stats) {
    this.stats = stats;
    return this;
  }

  public Item putStatsItem(String key, Object statsItem) {
    if (this.stats == null) {
      this.stats = new HashMap<>();
    }
    this.stats.put(key, statsItem);
    return this;
  }

  /**
   * Get stats
   * @return stats
   */
  
  @Schema(name = "stats", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stats")
  public Map<String, Object> getStats() {
    return stats;
  }

  public void setStats(Map<String, Object> stats) {
    this.stats = stats;
  }

  public Item expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expiresAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expiresAt")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public Item metadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
  }

  public Item putMetadataItem(String key, Object metadataItem) {
    if (this.metadata == null) {
      this.metadata = new HashMap<>();
    }
    this.metadata.put(key, metadataItem);
    return this;
  }

  /**
   * Get metadata
   * @return metadata
   */
  
  @Schema(name = "metadata", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metadata")
  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Item item = (Item) o;
    return Objects.equals(this.itemInstanceId, item.itemInstanceId) &&
        Objects.equals(this.itemId, item.itemId) &&
        Objects.equals(this.name, item.name) &&
        Objects.equals(this.rarity, item.rarity) &&
        Objects.equals(this.type, item.type) &&
        Objects.equals(this.quantity, item.quantity) &&
        Objects.equals(this.stackSize, item.stackSize) &&
        Objects.equals(this.weight, item.weight) &&
        Objects.equals(this.boundType, item.boundType) &&
        Objects.equals(this.durability, item.durability) &&
        Objects.equals(this.stats, item.stats) &&
        Objects.equals(this.expiresAt, item.expiresAt) &&
        Objects.equals(this.metadata, item.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemInstanceId, itemId, name, rarity, type, quantity, stackSize, weight, boundType, durability, stats, expiresAt, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Item {\n");
    sb.append("    itemInstanceId: ").append(toIndentedString(itemInstanceId)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    rarity: ").append(toIndentedString(rarity)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    stackSize: ").append(toIndentedString(stackSize)).append("\n");
    sb.append("    weight: ").append(toIndentedString(weight)).append("\n");
    sb.append("    boundType: ").append(toIndentedString(boundType)).append("\n");
    sb.append("    durability: ").append(toIndentedString(durability)).append("\n");
    sb.append("    stats: ").append(toIndentedString(stats)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

