package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.MembershipRole;
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
 * ChannelMembership
 */


public class ChannelMembership {

  private String channelId;

  private UUID playerId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime joinedAt;

  private @Nullable MembershipRole role;

  private Boolean muted = false;

  private Boolean notificationsMuted = false;

  public ChannelMembership() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChannelMembership(String channelId, UUID playerId, OffsetDateTime joinedAt) {
    this.channelId = channelId;
    this.playerId = playerId;
    this.joinedAt = joinedAt;
  }

  public ChannelMembership channelId(String channelId) {
    this.channelId = channelId;
    return this;
  }

  /**
   * Get channelId
   * @return channelId
   */
  @NotNull 
  @Schema(name = "channelId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("channelId")
  public String getChannelId() {
    return channelId;
  }

  public void setChannelId(String channelId) {
    this.channelId = channelId;
  }

  public ChannelMembership playerId(UUID playerId) {
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

  public ChannelMembership joinedAt(OffsetDateTime joinedAt) {
    this.joinedAt = joinedAt;
    return this;
  }

  /**
   * Get joinedAt
   * @return joinedAt
   */
  @NotNull @Valid 
  @Schema(name = "joinedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("joinedAt")
  public OffsetDateTime getJoinedAt() {
    return joinedAt;
  }

  public void setJoinedAt(OffsetDateTime joinedAt) {
    this.joinedAt = joinedAt;
  }

  public ChannelMembership role(@Nullable MembershipRole role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  @Valid 
  @Schema(name = "role", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public @Nullable MembershipRole getRole() {
    return role;
  }

  public void setRole(@Nullable MembershipRole role) {
    this.role = role;
  }

  public ChannelMembership muted(Boolean muted) {
    this.muted = muted;
    return this;
  }

  /**
   * Get muted
   * @return muted
   */
  
  @Schema(name = "muted", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("muted")
  public Boolean getMuted() {
    return muted;
  }

  public void setMuted(Boolean muted) {
    this.muted = muted;
  }

  public ChannelMembership notificationsMuted(Boolean notificationsMuted) {
    this.notificationsMuted = notificationsMuted;
    return this;
  }

  /**
   * Get notificationsMuted
   * @return notificationsMuted
   */
  
  @Schema(name = "notificationsMuted", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notificationsMuted")
  public Boolean getNotificationsMuted() {
    return notificationsMuted;
  }

  public void setNotificationsMuted(Boolean notificationsMuted) {
    this.notificationsMuted = notificationsMuted;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChannelMembership channelMembership = (ChannelMembership) o;
    return Objects.equals(this.channelId, channelMembership.channelId) &&
        Objects.equals(this.playerId, channelMembership.playerId) &&
        Objects.equals(this.joinedAt, channelMembership.joinedAt) &&
        Objects.equals(this.role, channelMembership.role) &&
        Objects.equals(this.muted, channelMembership.muted) &&
        Objects.equals(this.notificationsMuted, channelMembership.notificationsMuted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channelId, playerId, joinedAt, role, muted, notificationsMuted);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChannelMembership {\n");
    sb.append("    channelId: ").append(toIndentedString(channelId)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    joinedAt: ").append(toIndentedString(joinedAt)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    muted: ").append(toIndentedString(muted)).append("\n");
    sb.append("    notificationsMuted: ").append(toIndentedString(notificationsMuted)).append("\n");
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

