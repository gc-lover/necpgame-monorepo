package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
import java.util.UUID;
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
 * VoiceParticipantsResponseParticipantsInner
 */

@JsonTypeName("VoiceParticipantsResponse_participants_inner")

public class VoiceParticipantsResponseParticipantsInner {

  private UUID playerId;

  private @Nullable String displayName;

  private @Nullable Boolean muted;

  private @Nullable Boolean speaking;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime joinedAt;

  public VoiceParticipantsResponseParticipantsInner() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public VoiceParticipantsResponseParticipantsInner(UUID playerId) {
    this.playerId = playerId;
  }

  public VoiceParticipantsResponseParticipantsInner playerId(UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull @Valid 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(UUID playerId) {
    this.playerId = playerId;
  }

  public VoiceParticipantsResponseParticipantsInner displayName(@Nullable String displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * Get displayName
   * @return displayName
   */
  
  @Schema(name = "displayName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("displayName")
  public @Nullable String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(@Nullable String displayName) {
    this.displayName = displayName;
  }

  public VoiceParticipantsResponseParticipantsInner muted(@Nullable Boolean muted) {
    this.muted = muted;
    return this;
  }

  /**
   * Get muted
   * @return muted
   */
  
  @Schema(name = "muted", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("muted")
  public @Nullable Boolean getMuted() {
    return muted;
  }

  public void setMuted(@Nullable Boolean muted) {
    this.muted = muted;
  }

  public VoiceParticipantsResponseParticipantsInner speaking(@Nullable Boolean speaking) {
    this.speaking = speaking;
    return this;
  }

  /**
   * Get speaking
   * @return speaking
   */
  
  @Schema(name = "speaking", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("speaking")
  public @Nullable Boolean getSpeaking() {
    return speaking;
  }

  public void setSpeaking(@Nullable Boolean speaking) {
    this.speaking = speaking;
  }

  public VoiceParticipantsResponseParticipantsInner joinedAt(@Nullable OffsetDateTime joinedAt) {
    this.joinedAt = joinedAt;
    return this;
  }

  /**
   * Get joinedAt
   * @return joinedAt
   */
  @Valid 
  @Schema(name = "joinedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("joinedAt")
  public @Nullable OffsetDateTime getJoinedAt() {
    return joinedAt;
  }

  public void setJoinedAt(@Nullable OffsetDateTime joinedAt) {
    this.joinedAt = joinedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VoiceParticipantsResponseParticipantsInner voiceParticipantsResponseParticipantsInner = (VoiceParticipantsResponseParticipantsInner) o;
    return Objects.equals(this.playerId, voiceParticipantsResponseParticipantsInner.playerId) &&
        Objects.equals(this.displayName, voiceParticipantsResponseParticipantsInner.displayName) &&
        Objects.equals(this.muted, voiceParticipantsResponseParticipantsInner.muted) &&
        Objects.equals(this.speaking, voiceParticipantsResponseParticipantsInner.speaking) &&
        Objects.equals(this.joinedAt, voiceParticipantsResponseParticipantsInner.joinedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, displayName, muted, speaking, joinedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VoiceParticipantsResponseParticipantsInner {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    muted: ").append(toIndentedString(muted)).append("\n");
    sb.append("    speaking: ").append(toIndentedString(speaking)).append("\n");
    sb.append("    joinedAt: ").append(toIndentedString(joinedAt)).append("\n");
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

