package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.LootItem;
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
 * LootHistoryEntry
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class LootHistoryEntry {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  private @Nullable LootItem item;

  private @Nullable String source;

  private @Nullable String rollType;

  private @Nullable Integer rollValue;

  @Valid
  private List<String> participants = new ArrayList<>();

  private @Nullable String result;

  public LootHistoryEntry timestamp(@Nullable OffsetDateTime timestamp) {
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

  public LootHistoryEntry item(@Nullable LootItem item) {
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
  public @Nullable LootItem getItem() {
    return item;
  }

  public void setItem(@Nullable LootItem item) {
    this.item = item;
  }

  public LootHistoryEntry source(@Nullable String source) {
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

  public LootHistoryEntry rollType(@Nullable String rollType) {
    this.rollType = rollType;
    return this;
  }

  /**
   * Get rollType
   * @return rollType
   */
  
  @Schema(name = "rollType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rollType")
  public @Nullable String getRollType() {
    return rollType;
  }

  public void setRollType(@Nullable String rollType) {
    this.rollType = rollType;
  }

  public LootHistoryEntry rollValue(@Nullable Integer rollValue) {
    this.rollValue = rollValue;
    return this;
  }

  /**
   * Get rollValue
   * @return rollValue
   */
  
  @Schema(name = "rollValue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rollValue")
  public @Nullable Integer getRollValue() {
    return rollValue;
  }

  public void setRollValue(@Nullable Integer rollValue) {
    this.rollValue = rollValue;
  }

  public LootHistoryEntry participants(List<String> participants) {
    this.participants = participants;
    return this;
  }

  public LootHistoryEntry addParticipantsItem(String participantsItem) {
    if (this.participants == null) {
      this.participants = new ArrayList<>();
    }
    this.participants.add(participantsItem);
    return this;
  }

  /**
   * Get participants
   * @return participants
   */
  
  @Schema(name = "participants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("participants")
  public List<String> getParticipants() {
    return participants;
  }

  public void setParticipants(List<String> participants) {
    this.participants = participants;
  }

  public LootHistoryEntry result(@Nullable String result) {
    this.result = result;
    return this;
  }

  /**
   * Get result
   * @return result
   */
  
  @Schema(name = "result", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("result")
  public @Nullable String getResult() {
    return result;
  }

  public void setResult(@Nullable String result) {
    this.result = result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootHistoryEntry lootHistoryEntry = (LootHistoryEntry) o;
    return Objects.equals(this.timestamp, lootHistoryEntry.timestamp) &&
        Objects.equals(this.item, lootHistoryEntry.item) &&
        Objects.equals(this.source, lootHistoryEntry.source) &&
        Objects.equals(this.rollType, lootHistoryEntry.rollType) &&
        Objects.equals(this.rollValue, lootHistoryEntry.rollValue) &&
        Objects.equals(this.participants, lootHistoryEntry.participants) &&
        Objects.equals(this.result, lootHistoryEntry.result);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, item, source, rollType, rollValue, participants, result);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootHistoryEntry {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    item: ").append(toIndentedString(item)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    rollType: ").append(toIndentedString(rollType)).append("\n");
    sb.append("    rollValue: ").append(toIndentedString(rollValue)).append("\n");
    sb.append("    participants: ").append(toIndentedString(participants)).append("\n");
    sb.append("    result: ").append(toIndentedString(result)).append("\n");
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

