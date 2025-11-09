package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.ItemTransfer;
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
 * InventoryHistoryEntry
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class InventoryHistoryEntry {

  private @Nullable String entryId;

  private @Nullable String event;

  private @Nullable String source;

  @Valid
  private List<@Valid ItemTransfer> delta = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  private @Nullable String relatedEntity;

  public InventoryHistoryEntry entryId(@Nullable String entryId) {
    this.entryId = entryId;
    return this;
  }

  /**
   * Get entryId
   * @return entryId
   */
  
  @Schema(name = "entryId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("entryId")
  public @Nullable String getEntryId() {
    return entryId;
  }

  public void setEntryId(@Nullable String entryId) {
    this.entryId = entryId;
  }

  public InventoryHistoryEntry event(@Nullable String event) {
    this.event = event;
    return this;
  }

  /**
   * Get event
   * @return event
   */
  
  @Schema(name = "event", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event")
  public @Nullable String getEvent() {
    return event;
  }

  public void setEvent(@Nullable String event) {
    this.event = event;
  }

  public InventoryHistoryEntry source(@Nullable String source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
   */
  
  @Schema(name = "source", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("source")
  public @Nullable String getSource() {
    return source;
  }

  public void setSource(@Nullable String source) {
    this.source = source;
  }

  public InventoryHistoryEntry delta(List<@Valid ItemTransfer> delta) {
    this.delta = delta;
    return this;
  }

  public InventoryHistoryEntry addDeltaItem(ItemTransfer deltaItem) {
    if (this.delta == null) {
      this.delta = new ArrayList<>();
    }
    this.delta.add(deltaItem);
    return this;
  }

  /**
   * Get delta
   * @return delta
   */
  @Valid 
  @Schema(name = "delta", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("delta")
  public List<@Valid ItemTransfer> getDelta() {
    return delta;
  }

  public void setDelta(List<@Valid ItemTransfer> delta) {
    this.delta = delta;
  }

  public InventoryHistoryEntry timestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public @Nullable OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public InventoryHistoryEntry relatedEntity(@Nullable String relatedEntity) {
    this.relatedEntity = relatedEntity;
    return this;
  }

  /**
   * Get relatedEntity
   * @return relatedEntity
   */
  
  @Schema(name = "relatedEntity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relatedEntity")
  public @Nullable String getRelatedEntity() {
    return relatedEntity;
  }

  public void setRelatedEntity(@Nullable String relatedEntity) {
    this.relatedEntity = relatedEntity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InventoryHistoryEntry inventoryHistoryEntry = (InventoryHistoryEntry) o;
    return Objects.equals(this.entryId, inventoryHistoryEntry.entryId) &&
        Objects.equals(this.event, inventoryHistoryEntry.event) &&
        Objects.equals(this.source, inventoryHistoryEntry.source) &&
        Objects.equals(this.delta, inventoryHistoryEntry.delta) &&
        Objects.equals(this.timestamp, inventoryHistoryEntry.timestamp) &&
        Objects.equals(this.relatedEntity, inventoryHistoryEntry.relatedEntity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entryId, event, source, delta, timestamp, relatedEntity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InventoryHistoryEntry {\n");
    sb.append("    entryId: ").append(toIndentedString(entryId)).append("\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    delta: ").append(toIndentedString(delta)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    relatedEntity: ").append(toIndentedString(relatedEntity)).append("\n");
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

