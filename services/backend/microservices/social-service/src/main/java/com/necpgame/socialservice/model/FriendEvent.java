package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.Friend;
import java.time.OffsetDateTime;
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
 * FriendEvent
 */


public class FriendEvent {

  private @Nullable String playerId;

  private @Nullable Friend friend;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime occurredAt;

  public FriendEvent playerId(@Nullable String playerId) {
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

  public FriendEvent friend(@Nullable Friend friend) {
    this.friend = friend;
    return this;
  }

  /**
   * Get friend
   * @return friend
   */
  @Valid 
  @Schema(name = "friend", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("friend")
  public @Nullable Friend getFriend() {
    return friend;
  }

  public void setFriend(@Nullable Friend friend) {
    this.friend = friend;
  }

  public FriendEvent occurredAt(@Nullable OffsetDateTime occurredAt) {
    this.occurredAt = occurredAt;
    return this;
  }

  /**
   * Get occurredAt
   * @return occurredAt
   */
  @Valid 
  @Schema(name = "occurredAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("occurredAt")
  public @Nullable OffsetDateTime getOccurredAt() {
    return occurredAt;
  }

  public void setOccurredAt(@Nullable OffsetDateTime occurredAt) {
    this.occurredAt = occurredAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FriendEvent friendEvent = (FriendEvent) o;
    return Objects.equals(this.playerId, friendEvent.playerId) &&
        Objects.equals(this.friend, friendEvent.friend) &&
        Objects.equals(this.occurredAt, friendEvent.occurredAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, friend, occurredAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FriendEvent {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    friend: ").append(toIndentedString(friend)).append("\n");
    sb.append("    occurredAt: ").append(toIndentedString(occurredAt)).append("\n");
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

