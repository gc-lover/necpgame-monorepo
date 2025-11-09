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
 * ChannelMember
 */


public class ChannelMember {

  private UUID playerId;

  private @Nullable String nickname;

  private MembershipRole role;

  private @Nullable Boolean online;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime joinedAt;

  public ChannelMember() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChannelMember(UUID playerId, MembershipRole role) {
    this.playerId = playerId;
    this.role = role;
  }

  public ChannelMember playerId(UUID playerId) {
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

  public ChannelMember nickname(@Nullable String nickname) {
    this.nickname = nickname;
    return this;
  }

  /**
   * Get nickname
   * @return nickname
   */
  
  @Schema(name = "nickname", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nickname")
  public @Nullable String getNickname() {
    return nickname;
  }

  public void setNickname(@Nullable String nickname) {
    this.nickname = nickname;
  }

  public ChannelMember role(MembershipRole role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  @NotNull @Valid 
  @Schema(name = "role", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("role")
  public MembershipRole getRole() {
    return role;
  }

  public void setRole(MembershipRole role) {
    this.role = role;
  }

  public ChannelMember online(@Nullable Boolean online) {
    this.online = online;
    return this;
  }

  /**
   * Get online
   * @return online
   */
  
  @Schema(name = "online", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("online")
  public @Nullable Boolean getOnline() {
    return online;
  }

  public void setOnline(@Nullable Boolean online) {
    this.online = online;
  }

  public ChannelMember joinedAt(@Nullable OffsetDateTime joinedAt) {
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
    ChannelMember channelMember = (ChannelMember) o;
    return Objects.equals(this.playerId, channelMember.playerId) &&
        Objects.equals(this.nickname, channelMember.nickname) &&
        Objects.equals(this.role, channelMember.role) &&
        Objects.equals(this.online, channelMember.online) &&
        Objects.equals(this.joinedAt, channelMember.joinedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, nickname, role, online, joinedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChannelMember {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    nickname: ").append(toIndentedString(nickname)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    online: ").append(toIndentedString(online)).append("\n");
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

