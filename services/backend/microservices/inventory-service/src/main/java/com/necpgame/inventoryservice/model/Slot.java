package com.necpgame.inventoryservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.inventoryservice.model.Item;
import java.util.HashMap;
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
 * Slot
 */


public class Slot {

  private @Nullable String slotId;

  private @Nullable Integer index;

  private @Nullable Boolean locked;

  private @Nullable Item item;

  @Valid
  private Map<String, Object> metadata = new HashMap<>();

  public Slot slotId(@Nullable String slotId) {
    this.slotId = slotId;
    return this;
  }

  /**
   * Get slotId
   * @return slotId
   */
  
  @Schema(name = "slotId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slotId")
  public @Nullable String getSlotId() {
    return slotId;
  }

  public void setSlotId(@Nullable String slotId) {
    this.slotId = slotId;
  }

  public Slot index(@Nullable Integer index) {
    this.index = index;
    return this;
  }

  /**
   * Get index
   * @return index
   */
  
  @Schema(name = "index", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("index")
  public @Nullable Integer getIndex() {
    return index;
  }

  public void setIndex(@Nullable Integer index) {
    this.index = index;
  }

  public Slot locked(@Nullable Boolean locked) {
    this.locked = locked;
    return this;
  }

  /**
   * Get locked
   * @return locked
   */
  
  @Schema(name = "locked", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("locked")
  public @Nullable Boolean getLocked() {
    return locked;
  }

  public void setLocked(@Nullable Boolean locked) {
    this.locked = locked;
  }

  public Slot item(@Nullable Item item) {
    this.item = item;
    return this;
  }

  /**
   * Get item
   * @return item
   */
  @Valid 
  @Schema(name = "item", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item")
  public @Nullable Item getItem() {
    return item;
  }

  public void setItem(@Nullable Item item) {
    this.item = item;
  }

  public Slot metadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
  }

  public Slot putMetadataItem(String key, Object metadataItem) {
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
    Slot slot = (Slot) o;
    return Objects.equals(this.slotId, slot.slotId) &&
        Objects.equals(this.index, slot.index) &&
        Objects.equals(this.locked, slot.locked) &&
        Objects.equals(this.item, slot.item) &&
        Objects.equals(this.metadata, slot.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(slotId, index, locked, item, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Slot {\n");
    sb.append("    slotId: ").append(toIndentedString(slotId)).append("\n");
    sb.append("    index: ").append(toIndentedString(index)).append("\n");
    sb.append("    locked: ").append(toIndentedString(locked)).append("\n");
    sb.append("    item: ").append(toIndentedString(item)).append("\n");
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

