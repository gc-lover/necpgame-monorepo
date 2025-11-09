package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import com.necpgame.backjava.model.CombatParticipant;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CombatState
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T21:22:00.452540100+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class CombatState {

  private UUID id;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("active"),
    
    ENDED("ended"),
    
    FLED("fled");

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

  private StatusEnum status;

  @Valid
  private List<@Valid CombatParticipant> participants = new ArrayList<>();

  private String currentTurn;

  private @Nullable Integer round;

  @Valid
  private List<String> log = new ArrayList<>();

  public CombatState() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CombatState(UUID id, StatusEnum status, List<@Valid CombatParticipant> participants, String currentTurn) {
    this.id = id;
    this.status = status;
    this.participants = participants;
    this.currentTurn = currentTurn;
  }

  public CombatState id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @NotNull @Valid 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public CombatState status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public CombatState participants(List<@Valid CombatParticipant> participants) {
    this.participants = participants;
    return this;
  }

  public CombatState addParticipantsItem(CombatParticipant participantsItem) {
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
  @NotNull @Valid 
  @Schema(name = "participants", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("participants")
  public List<@Valid CombatParticipant> getParticipants() {
    return participants;
  }

  public void setParticipants(List<@Valid CombatParticipant> participants) {
    this.participants = participants;
  }

  public CombatState currentTurn(String currentTurn) {
    this.currentTurn = currentTurn;
    return this;
  }

  /**
   * Get currentTurn
   * @return currentTurn
   */
  @NotNull 
  @Schema(name = "currentTurn", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("currentTurn")
  public String getCurrentTurn() {
    return currentTurn;
  }

  public void setCurrentTurn(String currentTurn) {
    this.currentTurn = currentTurn;
  }

  public CombatState round(@Nullable Integer round) {
    this.round = round;
    return this;
  }

  /**
   * Get round
   * @return round
   */
  
  @Schema(name = "round", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("round")
  public @Nullable Integer getRound() {
    return round;
  }

  public void setRound(@Nullable Integer round) {
    this.round = round;
  }

  public CombatState log(List<String> log) {
    this.log = log;
    return this;
  }

  public CombatState addLogItem(String logItem) {
    if (this.log == null) {
      this.log = new ArrayList<>();
    }
    this.log.add(logItem);
    return this;
  }

  /**
   * Get log
   * @return log
   */
  
  @Schema(name = "log", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("log")
  public List<String> getLog() {
    return log;
  }

  public void setLog(List<String> log) {
    this.log = log;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CombatState combatState = (CombatState) o;
    return Objects.equals(this.id, combatState.id) &&
        Objects.equals(this.status, combatState.status) &&
        Objects.equals(this.participants, combatState.participants) &&
        Objects.equals(this.currentTurn, combatState.currentTurn) &&
        Objects.equals(this.round, combatState.round) &&
        Objects.equals(this.log, combatState.log);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, status, participants, currentTurn, round, log);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CombatState {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    participants: ").append(toIndentedString(participants)).append("\n");
    sb.append("    currentTurn: ").append(toIndentedString(currentTurn)).append("\n");
    sb.append("    round: ").append(toIndentedString(round)).append("\n");
    sb.append("    log: ").append(toIndentedString(log)).append("\n");
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

