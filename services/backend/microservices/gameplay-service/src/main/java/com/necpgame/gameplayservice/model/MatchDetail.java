package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.LatencyProfile;
import com.necpgame.gameplayservice.model.MatchQualityReport;
import com.necpgame.gameplayservice.model.MatchStatus;
import com.necpgame.gameplayservice.model.MatchTeam;
import com.necpgame.gameplayservice.model.ReadyCheckState;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * MatchDetail
 */


public class MatchDetail {

  private UUID matchId;

  /**
   * Gets or Sets mode
   */
  public enum ModeEnum {
    PVP_RANKED("PVP_RANKED"),
    
    PVP_CASUAL("PVP_CASUAL"),
    
    PVE_DUNGEON("PVE_DUNGEON"),
    
    RAID("RAID"),
    
    ARENA_EVENT("ARENA_EVENT");

    private final String value;

    ModeEnum(String value) {
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
    public static ModeEnum fromValue(String value) {
      for (ModeEnum b : ModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ModeEnum mode;

  private MatchStatus status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  @Valid
  private List<UUID> queueIds = new ArrayList<>();

  @Valid
  private List<@Valid MatchTeam> teams = new ArrayList<>();

  private @Nullable MatchQualityReport quality;

  private @Nullable LatencyProfile latencyProfile;

  private @Nullable ReadyCheckState readyCheck;

  private @Nullable String voiceLobbyId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lockingDeadline;

  public MatchDetail() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MatchDetail(UUID matchId, ModeEnum mode, MatchStatus status, OffsetDateTime createdAt, List<@Valid MatchTeam> teams) {
    this.matchId = matchId;
    this.mode = mode;
    this.status = status;
    this.createdAt = createdAt;
    this.teams = teams;
  }

  public MatchDetail matchId(UUID matchId) {
    this.matchId = matchId;
    return this;
  }

  /**
   * Get matchId
   * @return matchId
   */
  @NotNull @Valid 
  @Schema(name = "matchId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("matchId")
  public UUID getMatchId() {
    return matchId;
  }

  public void setMatchId(UUID matchId) {
    this.matchId = matchId;
  }

  public MatchDetail mode(ModeEnum mode) {
    this.mode = mode;
    return this;
  }

  /**
   * Get mode
   * @return mode
   */
  @NotNull 
  @Schema(name = "mode", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("mode")
  public ModeEnum getMode() {
    return mode;
  }

  public void setMode(ModeEnum mode) {
    this.mode = mode;
  }

  public MatchDetail status(MatchStatus status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull @Valid 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public MatchStatus getStatus() {
    return status;
  }

  public void setStatus(MatchStatus status) {
    this.status = status;
  }

  public MatchDetail createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @NotNull @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("createdAt")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public MatchDetail updatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @Valid 
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updatedAt")
  public @Nullable OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public MatchDetail queueIds(List<UUID> queueIds) {
    this.queueIds = queueIds;
    return this;
  }

  public MatchDetail addQueueIdsItem(UUID queueIdsItem) {
    if (this.queueIds == null) {
      this.queueIds = new ArrayList<>();
    }
    this.queueIds.add(queueIdsItem);
    return this;
  }

  /**
   * Get queueIds
   * @return queueIds
   */
  @Valid 
  @Schema(name = "queueIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("queueIds")
  public List<UUID> getQueueIds() {
    return queueIds;
  }

  public void setQueueIds(List<UUID> queueIds) {
    this.queueIds = queueIds;
  }

  public MatchDetail teams(List<@Valid MatchTeam> teams) {
    this.teams = teams;
    return this;
  }

  public MatchDetail addTeamsItem(MatchTeam teamsItem) {
    if (this.teams == null) {
      this.teams = new ArrayList<>();
    }
    this.teams.add(teamsItem);
    return this;
  }

  /**
   * Get teams
   * @return teams
   */
  @NotNull @Valid @Size(min = 1) 
  @Schema(name = "teams", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("teams")
  public List<@Valid MatchTeam> getTeams() {
    return teams;
  }

  public void setTeams(List<@Valid MatchTeam> teams) {
    this.teams = teams;
  }

  public MatchDetail quality(@Nullable MatchQualityReport quality) {
    this.quality = quality;
    return this;
  }

  /**
   * Get quality
   * @return quality
   */
  @Valid 
  @Schema(name = "quality", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quality")
  public @Nullable MatchQualityReport getQuality() {
    return quality;
  }

  public void setQuality(@Nullable MatchQualityReport quality) {
    this.quality = quality;
  }

  public MatchDetail latencyProfile(@Nullable LatencyProfile latencyProfile) {
    this.latencyProfile = latencyProfile;
    return this;
  }

  /**
   * Get latencyProfile
   * @return latencyProfile
   */
  @Valid 
  @Schema(name = "latencyProfile", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("latencyProfile")
  public @Nullable LatencyProfile getLatencyProfile() {
    return latencyProfile;
  }

  public void setLatencyProfile(@Nullable LatencyProfile latencyProfile) {
    this.latencyProfile = latencyProfile;
  }

  public MatchDetail readyCheck(@Nullable ReadyCheckState readyCheck) {
    this.readyCheck = readyCheck;
    return this;
  }

  /**
   * Get readyCheck
   * @return readyCheck
   */
  @Valid 
  @Schema(name = "readyCheck", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("readyCheck")
  public @Nullable ReadyCheckState getReadyCheck() {
    return readyCheck;
  }

  public void setReadyCheck(@Nullable ReadyCheckState readyCheck) {
    this.readyCheck = readyCheck;
  }

  public MatchDetail voiceLobbyId(@Nullable String voiceLobbyId) {
    this.voiceLobbyId = voiceLobbyId;
    return this;
  }

  /**
   * Get voiceLobbyId
   * @return voiceLobbyId
   */
  
  @Schema(name = "voiceLobbyId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("voiceLobbyId")
  public @Nullable String getVoiceLobbyId() {
    return voiceLobbyId;
  }

  public void setVoiceLobbyId(@Nullable String voiceLobbyId) {
    this.voiceLobbyId = voiceLobbyId;
  }

  public MatchDetail lockingDeadline(@Nullable OffsetDateTime lockingDeadline) {
    this.lockingDeadline = lockingDeadline;
    return this;
  }

  /**
   * Get lockingDeadline
   * @return lockingDeadline
   */
  @Valid 
  @Schema(name = "lockingDeadline", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lockingDeadline")
  public @Nullable OffsetDateTime getLockingDeadline() {
    return lockingDeadline;
  }

  public void setLockingDeadline(@Nullable OffsetDateTime lockingDeadline) {
    this.lockingDeadline = lockingDeadline;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MatchDetail matchDetail = (MatchDetail) o;
    return Objects.equals(this.matchId, matchDetail.matchId) &&
        Objects.equals(this.mode, matchDetail.mode) &&
        Objects.equals(this.status, matchDetail.status) &&
        Objects.equals(this.createdAt, matchDetail.createdAt) &&
        Objects.equals(this.updatedAt, matchDetail.updatedAt) &&
        Objects.equals(this.queueIds, matchDetail.queueIds) &&
        Objects.equals(this.teams, matchDetail.teams) &&
        Objects.equals(this.quality, matchDetail.quality) &&
        Objects.equals(this.latencyProfile, matchDetail.latencyProfile) &&
        Objects.equals(this.readyCheck, matchDetail.readyCheck) &&
        Objects.equals(this.voiceLobbyId, matchDetail.voiceLobbyId) &&
        Objects.equals(this.lockingDeadline, matchDetail.lockingDeadline);
  }

  @Override
  public int hashCode() {
    return Objects.hash(matchId, mode, status, createdAt, updatedAt, queueIds, teams, quality, latencyProfile, readyCheck, voiceLobbyId, lockingDeadline);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MatchDetail {\n");
    sb.append("    matchId: ").append(toIndentedString(matchId)).append("\n");
    sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("    queueIds: ").append(toIndentedString(queueIds)).append("\n");
    sb.append("    teams: ").append(toIndentedString(teams)).append("\n");
    sb.append("    quality: ").append(toIndentedString(quality)).append("\n");
    sb.append("    latencyProfile: ").append(toIndentedString(latencyProfile)).append("\n");
    sb.append("    readyCheck: ").append(toIndentedString(readyCheck)).append("\n");
    sb.append("    voiceLobbyId: ").append(toIndentedString(voiceLobbyId)).append("\n");
    sb.append("    lockingDeadline: ").append(toIndentedString(lockingDeadline)).append("\n");
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

