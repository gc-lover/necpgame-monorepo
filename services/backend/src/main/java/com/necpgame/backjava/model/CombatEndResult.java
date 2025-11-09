package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.CombatRewards;
import com.necpgame.backjava.model.ParticipantResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
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
 * CombatEndResult
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class CombatEndResult {

  private @Nullable UUID sessionId;

  /**
   * Gets or Sets outcome
   */
  public enum OutcomeEnum {
    VICTORY("VICTORY"),
    
    DEFEAT("DEFEAT"),
    
    DRAW("DRAW"),
    
    TIMEOUT("TIMEOUT");

    private final String value;

    OutcomeEnum(String value) {
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
    public static OutcomeEnum fromValue(String value) {
      for (OutcomeEnum b : OutcomeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable OutcomeEnum outcome;

  private JsonNullable<String> winnerTeam = JsonNullable.<String>undefined();

  private @Nullable Integer durationSeconds;

  @Valid
  private List<@Valid ParticipantResult> participantResults = new ArrayList<>();

  private @Nullable CombatRewards rewards;

  public CombatEndResult sessionId(@Nullable UUID sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  /**
   * Get sessionId
   * @return sessionId
   */
  @Valid 
  @Schema(name = "session_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("session_id")
  public @Nullable UUID getSessionId() {
    return sessionId;
  }

  public void setSessionId(@Nullable UUID sessionId) {
    this.sessionId = sessionId;
  }

  public CombatEndResult outcome(@Nullable OutcomeEnum outcome) {
    this.outcome = outcome;
    return this;
  }

  /**
   * Get outcome
   * @return outcome
   */
  
  @Schema(name = "outcome", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("outcome")
  public @Nullable OutcomeEnum getOutcome() {
    return outcome;
  }

  public void setOutcome(@Nullable OutcomeEnum outcome) {
    this.outcome = outcome;
  }

  public CombatEndResult winnerTeam(String winnerTeam) {
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

  public CombatEndResult durationSeconds(@Nullable Integer durationSeconds) {
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

  public CombatEndResult participantResults(List<@Valid ParticipantResult> participantResults) {
    this.participantResults = participantResults;
    return this;
  }

  public CombatEndResult addParticipantResultsItem(ParticipantResult participantResultsItem) {
    if (this.participantResults == null) {
      this.participantResults = new ArrayList<>();
    }
    this.participantResults.add(participantResultsItem);
    return this;
  }

  /**
   * Get participantResults
   * @return participantResults
   */
  @Valid 
  @Schema(name = "participant_results", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("participant_results")
  public List<@Valid ParticipantResult> getParticipantResults() {
    return participantResults;
  }

  public void setParticipantResults(List<@Valid ParticipantResult> participantResults) {
    this.participantResults = participantResults;
  }

  public CombatEndResult rewards(@Nullable CombatRewards rewards) {
    this.rewards = rewards;
    return this;
  }

  /**
   * Get rewards
   * @return rewards
   */
  @Valid 
  @Schema(name = "rewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards")
  public @Nullable CombatRewards getRewards() {
    return rewards;
  }

  public void setRewards(@Nullable CombatRewards rewards) {
    this.rewards = rewards;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CombatEndResult combatEndResult = (CombatEndResult) o;
    return Objects.equals(this.sessionId, combatEndResult.sessionId) &&
        Objects.equals(this.outcome, combatEndResult.outcome) &&
        equalsNullable(this.winnerTeam, combatEndResult.winnerTeam) &&
        Objects.equals(this.durationSeconds, combatEndResult.durationSeconds) &&
        Objects.equals(this.participantResults, combatEndResult.participantResults) &&
        Objects.equals(this.rewards, combatEndResult.rewards);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionId, outcome, hashCodeNullable(winnerTeam), durationSeconds, participantResults, rewards);
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
    sb.append("class CombatEndResult {\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    outcome: ").append(toIndentedString(outcome)).append("\n");
    sb.append("    winnerTeam: ").append(toIndentedString(winnerTeam)).append("\n");
    sb.append("    durationSeconds: ").append(toIndentedString(durationSeconds)).append("\n");
    sb.append("    participantResults: ").append(toIndentedString(participantResults)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
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

