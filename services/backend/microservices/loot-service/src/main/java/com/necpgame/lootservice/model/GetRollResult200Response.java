package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * GetRollResult200Response
 */

@JsonTypeName("getRollResult_200_response")

public class GetRollResult200Response {

  private @Nullable String rollId;

  private @Nullable String winnerId;

  private @Nullable String itemId;

  @Valid
  private List<Object> participants = new ArrayList<>();

  public GetRollResult200Response rollId(@Nullable String rollId) {
    this.rollId = rollId;
    return this;
  }

  /**
   * Get rollId
   * @return rollId
   */
  
  @Schema(name = "roll_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roll_id")
  public @Nullable String getRollId() {
    return rollId;
  }

  public void setRollId(@Nullable String rollId) {
    this.rollId = rollId;
  }

  public GetRollResult200Response winnerId(@Nullable String winnerId) {
    this.winnerId = winnerId;
    return this;
  }

  /**
   * Get winnerId
   * @return winnerId
   */
  
  @Schema(name = "winner_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("winner_id")
  public @Nullable String getWinnerId() {
    return winnerId;
  }

  public void setWinnerId(@Nullable String winnerId) {
    this.winnerId = winnerId;
  }

  public GetRollResult200Response itemId(@Nullable String itemId) {
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

  public GetRollResult200Response participants(List<Object> participants) {
    this.participants = participants;
    return this;
  }

  public GetRollResult200Response addParticipantsItem(Object participantsItem) {
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
  public List<Object> getParticipants() {
    return participants;
  }

  public void setParticipants(List<Object> participants) {
    this.participants = participants;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetRollResult200Response getRollResult200Response = (GetRollResult200Response) o;
    return Objects.equals(this.rollId, getRollResult200Response.rollId) &&
        Objects.equals(this.winnerId, getRollResult200Response.winnerId) &&
        Objects.equals(this.itemId, getRollResult200Response.itemId) &&
        Objects.equals(this.participants, getRollResult200Response.participants);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rollId, winnerId, itemId, participants);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetRollResult200Response {\n");
    sb.append("    rollId: ").append(toIndentedString(rollId)).append("\n");
    sb.append("    winnerId: ").append(toIndentedString(winnerId)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    participants: ").append(toIndentedString(participants)).append("\n");
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

