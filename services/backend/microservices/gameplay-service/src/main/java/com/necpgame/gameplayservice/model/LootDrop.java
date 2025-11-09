package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.LootItem;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
 * LootDrop
 */


public class LootDrop {

  private @Nullable String dropId;

  /**
   * Gets or Sets source
   */
  public enum SourceEnum {
    BOSS("BOSS"),
    
    CHEST("CHEST"),
    
    WORLD("WORLD"),
    
    QUEST("QUEST");

    private final String value;

    SourceEnum(String value) {
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
    public static SourceEnum fromValue(String value) {
      for (SourceEnum b : SourceEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable SourceEnum source;

  @Valid
  private Map<String, Object> location = new HashMap<>();

  private @Nullable String partyId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  @Valid
  private List<@Valid LootItem> items = new ArrayList<>();

  public LootDrop dropId(@Nullable String dropId) {
    this.dropId = dropId;
    return this;
  }

  /**
   * Get dropId
   * @return dropId
   */
  
  @Schema(name = "dropId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dropId")
  public @Nullable String getDropId() {
    return dropId;
  }

  public void setDropId(@Nullable String dropId) {
    this.dropId = dropId;
  }

  public LootDrop source(@Nullable SourceEnum source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
   */
  
  @Schema(name = "source", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("source")
  public @Nullable SourceEnum getSource() {
    return source;
  }

  public void setSource(@Nullable SourceEnum source) {
    this.source = source;
  }

  public LootDrop location(Map<String, Object> location) {
    this.location = location;
    return this;
  }

  public LootDrop putLocationItem(String key, Object locationItem) {
    if (this.location == null) {
      this.location = new HashMap<>();
    }
    this.location.put(key, locationItem);
    return this;
  }

  /**
   * Get location
   * @return location
   */
  
  @Schema(name = "location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public Map<String, Object> getLocation() {
    return location;
  }

  public void setLocation(Map<String, Object> location) {
    this.location = location;
  }

  public LootDrop partyId(@Nullable String partyId) {
    this.partyId = partyId;
    return this;
  }

  /**
   * Get partyId
   * @return partyId
   */
  
  @Schema(name = "partyId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("partyId")
  public @Nullable String getPartyId() {
    return partyId;
  }

  public void setPartyId(@Nullable String partyId) {
    this.partyId = partyId;
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
  @Schema(name = "expiresAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expiresAt")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public LootDrop items(List<@Valid LootItem> items) {
    this.items = items;
    return this;
  }

  public LootDrop addItemsItem(LootItem itemsItem) {
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
  @Valid 
  @Schema(name = "items", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items")
  public List<@Valid LootItem> getItems() {
    return items;
  }

  public void setItems(List<@Valid LootItem> items) {
    this.items = items;
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
        Objects.equals(this.source, lootDrop.source) &&
        Objects.equals(this.location, lootDrop.location) &&
        Objects.equals(this.partyId, lootDrop.partyId) &&
        Objects.equals(this.expiresAt, lootDrop.expiresAt) &&
        Objects.equals(this.items, lootDrop.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dropId, source, location, partyId, expiresAt, items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootDrop {\n");
    sb.append("    dropId: ").append(toIndentedString(dropId)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
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

