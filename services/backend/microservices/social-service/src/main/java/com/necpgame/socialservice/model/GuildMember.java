package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * GuildMember
 */


public class GuildMember {

  private @Nullable String memberId;

  private @Nullable String playerId;

  private @Nullable String displayName;

  private @Nullable String rankId;

  private @Nullable String role;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime joinDate;

  private @Nullable Integer contribution;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("active"),
    
    VACATION("vacation"),
    
    SUSPENDED("suspended");

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
  private @Nullable OffsetDateTime lastOnline;

  public GuildMember memberId(@Nullable String memberId) {
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

  public GuildMember playerId(@Nullable String playerId) {
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

  public GuildMember displayName(@Nullable String displayName) {
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

  public GuildMember rankId(@Nullable String rankId) {
    this.rankId = rankId;
    return this;
  }

  /**
   * Get rankId
   * @return rankId
   */
  
  @Schema(name = "rankId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rankId")
  public @Nullable String getRankId() {
    return rankId;
  }

  public void setRankId(@Nullable String rankId) {
    this.rankId = rankId;
  }

  public GuildMember role(@Nullable String role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  
  @Schema(name = "role", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public @Nullable String getRole() {
    return role;
  }

  public void setRole(@Nullable String role) {
    this.role = role;
  }

  public GuildMember joinDate(@Nullable OffsetDateTime joinDate) {
    this.joinDate = joinDate;
    return this;
  }

  /**
   * Get joinDate
   * @return joinDate
   */
  @Valid 
  @Schema(name = "joinDate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("joinDate")
  public @Nullable OffsetDateTime getJoinDate() {
    return joinDate;
  }

  public void setJoinDate(@Nullable OffsetDateTime joinDate) {
    this.joinDate = joinDate;
  }

  public GuildMember contribution(@Nullable Integer contribution) {
    this.contribution = contribution;
    return this;
  }

  /**
   * Get contribution
   * @return contribution
   */
  
  @Schema(name = "contribution", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("contribution")
  public @Nullable Integer getContribution() {
    return contribution;
  }

  public void setContribution(@Nullable Integer contribution) {
    this.contribution = contribution;
  }

  public GuildMember status(@Nullable StatusEnum status) {
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

  public GuildMember lastOnline(@Nullable OffsetDateTime lastOnline) {
    this.lastOnline = lastOnline;
    return this;
  }

  /**
   * Get lastOnline
   * @return lastOnline
   */
  @Valid 
  @Schema(name = "lastOnline", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastOnline")
  public @Nullable OffsetDateTime getLastOnline() {
    return lastOnline;
  }

  public void setLastOnline(@Nullable OffsetDateTime lastOnline) {
    this.lastOnline = lastOnline;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildMember guildMember = (GuildMember) o;
    return Objects.equals(this.memberId, guildMember.memberId) &&
        Objects.equals(this.playerId, guildMember.playerId) &&
        Objects.equals(this.displayName, guildMember.displayName) &&
        Objects.equals(this.rankId, guildMember.rankId) &&
        Objects.equals(this.role, guildMember.role) &&
        Objects.equals(this.joinDate, guildMember.joinDate) &&
        Objects.equals(this.contribution, guildMember.contribution) &&
        Objects.equals(this.status, guildMember.status) &&
        Objects.equals(this.lastOnline, guildMember.lastOnline);
  }

  @Override
  public int hashCode() {
    return Objects.hash(memberId, playerId, displayName, rankId, role, joinDate, contribution, status, lastOnline);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildMember {\n");
    sb.append("    memberId: ").append(toIndentedString(memberId)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    rankId: ").append(toIndentedString(rankId)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    joinDate: ").append(toIndentedString(joinDate)).append("\n");
    sb.append("    contribution: ").append(toIndentedString(contribution)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    lastOnline: ").append(toIndentedString(lastOnline)).append("\n");
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

