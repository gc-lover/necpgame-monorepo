package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * LeaveVoiceResponse
 */


public class LeaveVoiceResponse {

  private @Nullable String channelId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime removedAt;

  private @Nullable Integer remainingParticipants;

  public LeaveVoiceResponse channelId(@Nullable String channelId) {
    this.channelId = channelId;
    return this;
  }

  /**
   * Get channelId
   * @return channelId
   */
  
  @Schema(name = "channelId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("channelId")
  public @Nullable String getChannelId() {
    return channelId;
  }

  public void setChannelId(@Nullable String channelId) {
    this.channelId = channelId;
  }

  public LeaveVoiceResponse removedAt(@Nullable OffsetDateTime removedAt) {
    this.removedAt = removedAt;
    return this;
  }

  /**
   * Get removedAt
   * @return removedAt
   */
  @Valid 
  @Schema(name = "removedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("removedAt")
  public @Nullable OffsetDateTime getRemovedAt() {
    return removedAt;
  }

  public void setRemovedAt(@Nullable OffsetDateTime removedAt) {
    this.removedAt = removedAt;
  }

  public LeaveVoiceResponse remainingParticipants(@Nullable Integer remainingParticipants) {
    this.remainingParticipants = remainingParticipants;
    return this;
  }

  /**
   * Get remainingParticipants
   * @return remainingParticipants
   */
  
  @Schema(name = "remainingParticipants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("remainingParticipants")
  public @Nullable Integer getRemainingParticipants() {
    return remainingParticipants;
  }

  public void setRemainingParticipants(@Nullable Integer remainingParticipants) {
    this.remainingParticipants = remainingParticipants;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LeaveVoiceResponse leaveVoiceResponse = (LeaveVoiceResponse) o;
    return Objects.equals(this.channelId, leaveVoiceResponse.channelId) &&
        Objects.equals(this.removedAt, leaveVoiceResponse.removedAt) &&
        Objects.equals(this.remainingParticipants, leaveVoiceResponse.remainingParticipants);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channelId, removedAt, remainingParticipants);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LeaveVoiceResponse {\n");
    sb.append("    channelId: ").append(toIndentedString(channelId)).append("\n");
    sb.append("    removedAt: ").append(toIndentedString(removedAt)).append("\n");
    sb.append("    remainingParticipants: ").append(toIndentedString(remainingParticipants)).append("\n");
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

