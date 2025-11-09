package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * LootDrop
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class LootDrop {

  private @Nullable String dropId;

  private @Nullable Object position;

  @Valid
  private List<Object> items = new ArrayList<>();

  /**
   * Gets or Sets lootMode
   */
  public enum LootModeEnum {
    PERSONAL("personal"),
    
    SHARED("shared"),
    
    NEED_GREED("need_greed"),
    
    MASTER_LOOTER("master_looter");

    private final String value;

    LootModeEnum(String value) {
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
    public static LootModeEnum fromValue(String value) {
      for (LootModeEnum b : LootModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable LootModeEnum lootMode;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  public LootDrop dropId(@Nullable String dropId) {
    this.dropId = dropId;
    return this;
  }

  /**
   * Get dropId
   * @return dropId
   */
  
  @Schema(name = "drop_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("drop_id")
  public @Nullable String getDropId() {
    return dropId;
  }

  public void setDropId(@Nullable String dropId) {
    this.dropId = dropId;
  }

  public LootDrop position(@Nullable Object position) {
    this.position = position;
    return this;
  }

  /**
   * Get position
   * @return position
   */
  
  @Schema(name = "position", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("position")
  public @Nullable Object getPosition() {
    return position;
  }

  public void setPosition(@Nullable Object position) {
    this.position = position;
  }

  public LootDrop items(List<Object> items) {
    this.items = items;
    return this;
  }

  public LootDrop addItemsItem(Object itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Get items
   * @return items
   */
  
  @Schema(name = "items", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items")
  public List<Object> getItems() {
    return items;
  }

  public void setItems(List<Object> items) {
    this.items = items;
  }

  public LootDrop lootMode(@Nullable LootModeEnum lootMode) {
    this.lootMode = lootMode;
    return this;
  }

  /**
   * Get lootMode
   * @return lootMode
   */
  
  @Schema(name = "loot_mode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loot_mode")
  public @Nullable LootModeEnum getLootMode() {
    return lootMode;
  }

  public void setLootMode(@Nullable LootModeEnum lootMode) {
    this.lootMode = lootMode;
  }

  public LootDrop expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expires_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expires_at")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootDrop lootDrop = (LootDrop) o;
    return Objects.equals(this.dropId, lootDrop.dropId) &&
        Objects.equals(this.position, lootDrop.position) &&
        Objects.equals(this.items, lootDrop.items) &&
        Objects.equals(this.lootMode, lootDrop.lootMode) &&
        Objects.equals(this.expiresAt, lootDrop.expiresAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dropId, position, items, lootMode, expiresAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootDrop {\n");
    sb.append("    dropId: ").append(toIndentedString(dropId)).append("\n");
    sb.append("    position: ").append(toIndentedString(position)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    lootMode: ").append(toIndentedString(lootMode)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
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

