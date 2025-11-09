package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.Participant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CombatSession
 */


public class CombatSession {

  private @Nullable UUID id;

  /**
   * Gets or Sets combatType
   */
  public enum CombatTypeEnum {
    PVE("PVE"),
    
    PVP_DUEL("PVP_DUEL"),
    
    PVP_ARENA("PVP_ARENA"),
    
    RAID_BOSS("RAID_BOSS"),
    
    EXTRACTION("EXTRACTION");

    private final String value;

    CombatTypeEnum(String value) {
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
    public static CombatTypeEnum fromValue(String value) {
      for (CombatTypeEnum b : CombatTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable CombatTypeEnum combatType;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    STARTING("STARTING"),
    
    ACTIVE("ACTIVE"),
    
    PAUSED("PAUSED"),
    
    ENDED("ENDED");

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

  @Valid
  private List<@Valid Participant> participants = new ArrayList<>();

  private JsonNullable<Integer> currentTurn = JsonNullable.<Integer>undefined();

  private JsonNullable<String> activeParticipantId = JsonNullable.<String>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> endedAt = JsonNullable.<OffsetDateTime>undefined();

  private @Nullable Integer durationSeconds;

  private JsonNullable<String> winnerTeam = JsonNullable.<String>undefined();

  public CombatSession id(@Nullable UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @Valid 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable UUID getId() {
    return id;
  }

  public void setId(@Nullable UUID id) {
    this.id = id;
  }

  public CombatSession combatType(@Nullable CombatTypeEnum combatType) {
    this.combatType = combatType;
    return this;
  }

  /**
   * Get combatType
   * @return combatType
   */
  
  @Schema(name = "combat_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("combat_type")
  public @Nullable CombatTypeEnum getCombatType() {
    return combatType;
  }

  public void setCombatType(@Nullable CombatTypeEnum combatType) {
    this.combatType = combatType;
  }

  public CombatSession status(@Nullable StatusEnum status) {
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

  public CombatSession participants(List<@Valid Participant> participants) {
    this.participants = participants;
    return this;
  }

  public CombatSession addParticipantsItem(Participant participantsItem) {
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
  public List<@Valid Participant> getParticipants() {
    return participants;
  }

  public void setParticipants(List<@Valid Participant> participants) {
    this.participants = participants;
  }

  public CombatSession currentTurn(Integer currentTurn) {
    this.currentTurn = JsonNullable.of(currentTurn);
    return this;
  }

  /**
   * Get currentTurn
   * @return currentTurn
   */
  
  @Schema(name = "current_turn", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_turn")
  public JsonNullable<Integer> getCurrentTurn() {
    return currentTurn;
  }

  public void setCurrentTurn(JsonNullable<Integer> currentTurn) {
    this.currentTurn = currentTurn;
  }

  public CombatSession activeParticipantId(String activeParticipantId) {
    this.activeParticipantId = JsonNullable.of(activeParticipantId);
    return this;
  }

  /**
   * Get activeParticipantId
   * @return activeParticipantId
   */
  
  @Schema(name = "active_participant_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_participant_id")
  public JsonNullable<String> getActiveParticipantId() {
    return activeParticipantId;
  }

  public void setActiveParticipantId(JsonNullable<String> activeParticipantId) {
    this.activeParticipantId = activeParticipantId;
  }

  public CombatSession startedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
    return this;
  }

  /**
   * Get startedAt
   * @return startedAt
   */
  @Valid 
  @Schema(name = "started_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("started_at")
  public @Nullable OffsetDateTime getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
  }

  public CombatSession endedAt(OffsetDateTime endedAt) {
    this.endedAt = JsonNullable.of(endedAt);
    return this;
  }

  /**
   * Get endedAt
   * @return endedAt
   */
  @Valid 
  @Schema(name = "ended_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ended_at")
  public JsonNullable<OffsetDateTime> getEndedAt() {
    return endedAt;
  }

  public void setEndedAt(JsonNullable<OffsetDateTime> endedAt) {
    this.endedAt = endedAt;
  }

  public CombatSession durationSeconds(@Nullable Integer durationSeconds) {
    this.durationSeconds = durationSeconds;
    return this;
  }

  /**
   * Get durationSeconds
   * @return durationSeconds
   */
  
  @Schema(name = "duration_seconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration_seconds")
  public @Nullable Integer getDurationSeconds() {
    return durationSeconds;
  }

  public void setDurationSeconds(@Nullable Integer durationSeconds) {
    this.durationSeconds = durationSeconds;
  }

  public CombatSession winnerTeam(String winnerTeam) {
    this.winnerTeam = JsonNullable.of(winnerTeam);
    return this;
  }

  /**
   * Get winnerTeam
   * @return winnerTeam
   */
  
  @Schema(name = "winner_team", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("winner_team")
  public JsonNullable<String> getWinnerTeam() {
    return winnerTeam;
  }

  public void setWinnerTeam(JsonNullable<String> winnerTeam) {
    this.winnerTeam = winnerTeam;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CombatSession combatSession = (CombatSession) o;
    return Objects.equals(this.id, combatSession.id) &&
        Objects.equals(this.combatType, combatSession.combatType) &&
        Objects.equals(this.status, combatSession.status) &&
        Objects.equals(this.participants, combatSession.participants) &&
        equalsNullable(this.currentTurn, combatSession.currentTurn) &&
        equalsNullable(this.activeParticipantId, combatSession.activeParticipantId) &&
        Objects.equals(this.startedAt, combatSession.startedAt) &&
        equalsNullable(this.endedAt, combatSession.endedAt) &&
        Objects.equals(this.durationSeconds, combatSession.durationSeconds) &&
        equalsNullable(this.winnerTeam, combatSession.winnerTeam);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, combatType, status, participants, hashCodeNullable(currentTurn), hashCodeNullable(activeParticipantId), startedAt, hashCodeNullable(endedAt), durationSeconds, hashCodeNullable(winnerTeam));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CombatSession {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    combatType: ").append(toIndentedString(combatType)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    participants: ").append(toIndentedString(participants)).append("\n");
    sb.append("    currentTurn: ").append(toIndentedString(currentTurn)).append("\n");
    sb.append("    activeParticipantId: ").append(toIndentedString(activeParticipantId)).append("\n");
    sb.append("    startedAt: ").append(toIndentedString(startedAt)).append("\n");
    sb.append("    endedAt: ").append(toIndentedString(endedAt)).append("\n");
    sb.append("    durationSeconds: ").append(toIndentedString(durationSeconds)).append("\n");
    sb.append("    winnerTeam: ").append(toIndentedString(winnerTeam)).append("\n");
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

