package com.necpgame.partyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.partyservice.model.PartyMemberStats;
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
 * PartyMember
 */


public class PartyMember {

  private @Nullable String memberId;

  private @Nullable String playerId;

  private @Nullable String displayName;

  /**
   * Gets or Sets role
   */
  public enum RoleEnum {
    LEADER("LEADER"),
    
    TANK("TANK"),
    
    HEALER("HEALER"),
    
    DPS("DPS"),
    
    SUPPORT("SUPPORT");

    private final String value;

    RoleEnum(String value) {
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
    public static RoleEnum fromValue(String value) {
      for (RoleEnum b : RoleEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RoleEnum role;

  private @Nullable Boolean isLeader;

  /**
   * Gets or Sets readyState
   */
  public enum ReadyStateEnum {
    UNKNOWN("UNKNOWN"),
    
    READY("READY"),
    
    NOT_READY("NOT_READY");

    private final String value;

    ReadyStateEnum(String value) {
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
    public static ReadyStateEnum fromValue(String value) {
      for (ReadyStateEnum b : ReadyStateEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ReadyStateEnum readyState;

  private @Nullable String voiceChannel;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime joinTime;

  private @Nullable PartyMemberStats stats;

  public PartyMember memberId(@Nullable String memberId) {
    this.memberId = memberId;
    return this;
  }

  /**
   * Get memberId
   * @return memberId
   */
  
  @Schema(name = "memberId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("memberId")
  public @Nullable String getMemberId() {
    return memberId;
  }

  public void setMemberId(@Nullable String memberId) {
    this.memberId = memberId;
  }

  public PartyMember playerId(@Nullable String playerId) {
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

  public PartyMember displayName(@Nullable String displayName) {
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

  public PartyMember role(@Nullable RoleEnum role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  
  @Schema(name = "role", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public @Nullable RoleEnum getRole() {
    return role;
  }

  public void setRole(@Nullable RoleEnum role) {
    this.role = role;
  }

  public PartyMember isLeader(@Nullable Boolean isLeader) {
    this.isLeader = isLeader;
    return this;
  }

  /**
   * Get isLeader
   * @return isLeader
   */
  
  @Schema(name = "isLeader", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isLeader")
  public @Nullable Boolean getIsLeader() {
    return isLeader;
  }

  public void setIsLeader(@Nullable Boolean isLeader) {
    this.isLeader = isLeader;
  }

  public PartyMember readyState(@Nullable ReadyStateEnum readyState) {
    this.readyState = readyState;
    return this;
  }

  /**
   * Get readyState
   * @return readyState
   */
  
  @Schema(name = "readyState", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("readyState")
  public @Nullable ReadyStateEnum getReadyState() {
    return readyState;
  }

  public void setReadyState(@Nullable ReadyStateEnum readyState) {
    this.readyState = readyState;
  }

  public PartyMember voiceChannel(@Nullable String voiceChannel) {
    this.voiceChannel = voiceChannel;
    return this;
  }

  /**
   * Get voiceChannel
   * @return voiceChannel
   */
  
  @Schema(name = "voiceChannel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("voiceChannel")
  public @Nullable String getVoiceChannel() {
    return voiceChannel;
  }

  public void setVoiceChannel(@Nullable String voiceChannel) {
    this.voiceChannel = voiceChannel;
  }

  public PartyMember joinTime(@Nullable OffsetDateTime joinTime) {
    this.joinTime = joinTime;
    return this;
  }

  /**
   * Get joinTime
   * @return joinTime
   */
  @Valid 
  @Schema(name = "joinTime", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("joinTime")
  public @Nullable OffsetDateTime getJoinTime() {
    return joinTime;
  }

  public void setJoinTime(@Nullable OffsetDateTime joinTime) {
    this.joinTime = joinTime;
  }

  public PartyMember stats(@Nullable PartyMemberStats stats) {
    this.stats = stats;
    return this;
  }

  /**
   * Get stats
   * @return stats
   */
  @Valid 
  @Schema(name = "stats", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stats")
  public @Nullable PartyMemberStats getStats() {
    return stats;
  }

  public void setStats(@Nullable PartyMemberStats stats) {
    this.stats = stats;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PartyMember partyMember = (PartyMember) o;
    return Objects.equals(this.memberId, partyMember.memberId) &&
        Objects.equals(this.playerId, partyMember.playerId) &&
        Objects.equals(this.displayName, partyMember.displayName) &&
        Objects.equals(this.role, partyMember.role) &&
        Objects.equals(this.isLeader, partyMember.isLeader) &&
        Objects.equals(this.readyState, partyMember.readyState) &&
        Objects.equals(this.voiceChannel, partyMember.voiceChannel) &&
        Objects.equals(this.joinTime, partyMember.joinTime) &&
        Objects.equals(this.stats, partyMember.stats);
  }

  @Override
  public int hashCode() {
    return Objects.hash(memberId, playerId, displayName, role, isLeader, readyState, voiceChannel, joinTime, stats);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PartyMember {\n");
    sb.append("    memberId: ").append(toIndentedString(memberId)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    isLeader: ").append(toIndentedString(isLeader)).append("\n");
    sb.append("    readyState: ").append(toIndentedString(readyState)).append("\n");
    sb.append("    voiceChannel: ").append(toIndentedString(voiceChannel)).append("\n");
    sb.append("    joinTime: ").append(toIndentedString(joinTime)).append("\n");
    sb.append("    stats: ").append(toIndentedString(stats)).append("\n");
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

