package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.lootservice.model.LootItem;
import com.necpgame.lootservice.model.RollHistoryEntry;
import com.necpgame.lootservice.model.RollParticipant;
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
 * LootRoll
 */


public class LootRoll {

  private @Nullable String rollId;

  private @Nullable String dropId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PENDING("PENDING"),
    
    COMPLETED("COMPLETED"),
    
    CANCELLED("CANCELLED");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  @Valid
  private List<@Valid LootItem> items = new ArrayList<>();

  @Valid
  private List<@Valid RollParticipant> participants = new ArrayList<>();

  private @Nullable String winner;

  @Valid
  private List<@Valid RollHistoryEntry> history = new ArrayList<>();

  public LootRoll rollId(@Nullable String rollId) {
    this.rollId = rollId;
    return this;
  }

  /**
   * Get rollId
   * @return rollId
   */
  
  @Schema(name = "rollId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rollId")
  public @Nullable String getRollId() {
    return rollId;
  }

  public void setRollId(@Nullable String rollId) {
    this.rollId = rollId;
  }

  public LootRoll dropId(@Nullable String dropId) {
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

  public LootRoll status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public LootRoll expiresAt(@Nullable OffsetDateTime expiresAt) {
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

  public LootRoll items(List<@Valid LootItem> items) {
    this.items = items;
    return this;
  }

  public LootRoll addItemsItem(LootItem itemsItem) {
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

  public LootRoll participants(List<@Valid RollParticipant> participants) {
    this.participants = participants;
    return this;
  }

  public LootRoll addParticipantsItem(RollParticipant participantsItem) {
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
  @Valid 
  @Schema(name = "participants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("participants")
  public List<@Valid RollParticipant> getParticipants() {
    return participants;
  }

  public void setParticipants(List<@Valid RollParticipant> participants) {
    this.participants = participants;
  }

  public LootRoll winner(@Nullable String winner) {
    this.winner = winner;
    return this;
  }

  /**
   * Get winner
   * @return winner
   */
  
  @Schema(name = "winner", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("winner")
  public @Nullable String getWinner() {
    return winner;
  }

  public void setWinner(@Nullable String winner) {
    this.winner = winner;
  }

  public LootRoll history(List<@Valid RollHistoryEntry> history) {
    this.history = history;
    return this;
  }

  public LootRoll addHistoryItem(RollHistoryEntry historyItem) {
    if (this.history == null) {
      this.history = new ArrayList<>();
    }
    this.history.add(historyItem);
    return this;
  }

  /**
   * Get history
   * @return history
   */
  @Valid 
  @Schema(name = "history", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("history")
  public List<@Valid RollHistoryEntry> getHistory() {
    return history;
  }

  public void setHistory(List<@Valid RollHistoryEntry> history) {
    this.history = history;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootRoll lootRoll = (LootRoll) o;
    return Objects.equals(this.rollId, lootRoll.rollId) &&
        Objects.equals(this.dropId, lootRoll.dropId) &&
        Objects.equals(this.status, lootRoll.status) &&
        Objects.equals(this.expiresAt, lootRoll.expiresAt) &&
        Objects.equals(this.items, lootRoll.items) &&
        Objects.equals(this.participants, lootRoll.participants) &&
        Objects.equals(this.winner, lootRoll.winner) &&
        Objects.equals(this.history, lootRoll.history);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rollId, dropId, status, expiresAt, items, participants, winner, history);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootRoll {\n");
    sb.append("    rollId: ").append(toIndentedString(rollId)).append("\n");
    sb.append("    dropId: ").append(toIndentedString(dropId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    participants: ").append(toIndentedString(participants)).append("\n");
    sb.append("    winner: ").append(toIndentedString(winner)).append("\n");
    sb.append("    history: ").append(toIndentedString(history)).append("\n");
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

