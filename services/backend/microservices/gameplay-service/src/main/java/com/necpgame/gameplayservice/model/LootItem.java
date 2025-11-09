package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LootItem
 */


public class LootItem {

  private @Nullable String itemId;

  /**
   * Gets or Sets itemType
   */
  public enum ItemTypeEnum {
    WEAPON("weapon"),
    
    EQUIPMENT("equipment"),
    
    RESOURCE("resource"),
    
    INFORMATION("information"),
    
    IMPLANT("implant"),
    
    CYBERDECK("cyberdeck"),
    
    MOD("mod"),
    
    ACCESS_CARD("access_card"),
    
    VIRUS("virus"),
    
    DATA_CHIP("data_chip"),
    
    BLUEPRINT("blueprint");

    private final String value;

    ItemTypeEnum(String value) {
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
    public static ItemTypeEnum fromValue(String value) {
      for (ItemTypeEnum b : ItemTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ItemTypeEnum itemType;

  /**
   * Gets or Sets rarity
   */
  public enum RarityEnum {
    COMMON("common"),
    
    UNCOMMON("uncommon"),
    
    RARE("rare"),
    
    EPIC("epic"),
    
    LEGENDARY("legendary"),
    
    ARTIFACT("artifact"),
    
    ICONIC("iconic");

    private final String value;

    RarityEnum(String value) {
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
    public static RarityEnum fromValue(String value) {
      for (RarityEnum b : RarityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RarityEnum rarity;

  private @Nullable BigDecimal value;

  public LootItem itemId(@Nullable String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  
  @Schema(name = "item_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_id")
  public @Nullable String getItemId() {
    return itemId;
  }

  public void setItemId(@Nullable String itemId) {
    this.itemId = itemId;
  }

  public LootItem itemType(@Nullable ItemTypeEnum itemType) {
    this.itemType = itemType;
    return this;
  }

  /**
   * Get itemType
   * @return itemType
   */
  
  @Schema(name = "item_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_type")
  public @Nullable ItemTypeEnum getItemType() {
    return itemType;
  }

  public void setItemType(@Nullable ItemTypeEnum itemType) {
    this.itemType = itemType;
  }

  public LootItem rarity(@Nullable RarityEnum rarity) {
    this.rarity = rarity;
    return this;
  }

  /**
   * Get rarity
   * @return rarity
   */
  
  @Schema(name = "rarity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rarity")
  public @Nullable RarityEnum getRarity() {
    return rarity;
  }

  public void setRarity(@Nullable RarityEnum rarity) {
    this.rarity = rarity;
  }

  public LootItem value(@Nullable BigDecimal value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * @return value
   */
  @Valid 
  @Schema(name = "value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("value")
  public @Nullable BigDecimal getValue() {
    return value;
  }

  public void setValue(@Nullable BigDecimal value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootItem lootItem = (LootItem) o;
    return Objects.equals(this.itemId, lootItem.itemId) &&
        Objects.equals(this.itemType, lootItem.itemType) &&
        Objects.equals(this.rarity, lootItem.rarity) &&
        Objects.equals(this.value, lootItem.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, itemType, rarity, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootItem {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    itemType: ").append(toIndentedString(itemType)).append("\n");
    sb.append("    rarity: ").append(toIndentedString(rarity)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

