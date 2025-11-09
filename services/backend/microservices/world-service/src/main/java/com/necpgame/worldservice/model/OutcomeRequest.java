package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.OutcomeRequestAnalytics;
import com.necpgame.worldservice.model.ReputationChange;
import com.necpgame.worldservice.model.RewardPayload;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * OutcomeRequest
 */


public class OutcomeRequest {

  private UUID playerId;

  /**
   * Gets or Sets outcome
   */
  public enum OutcomeEnum {
    SUCCESS("SUCCESS"),
    
    FAILURE("FAILURE"),
    
    ABORTED("ABORTED");

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

  private OutcomeEnum outcome;

  private @Nullable RewardPayload rewardsOverride;

  @Valid
  private List<@Valid ReputationChange> reputationChanges = new ArrayList<>();

  private @Nullable OutcomeRequestAnalytics analytics;

  public OutcomeRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OutcomeRequest(UUID playerId, OutcomeEnum outcome) {
    this.playerId = playerId;
    this.outcome = outcome;
  }

  public OutcomeRequest playerId(UUID playerId) {
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

  public OutcomeRequest outcome(OutcomeEnum outcome) {
    this.outcome = outcome;
    return this;
  }

  /**
   * Get outcome
   * @return outcome
   */
  @NotNull 
  @Schema(name = "outcome", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("outcome")
  public OutcomeEnum getOutcome() {
    return outcome;
  }

  public void setOutcome(OutcomeEnum outcome) {
    this.outcome = outcome;
  }

  public OutcomeRequest rewardsOverride(@Nullable RewardPayload rewardsOverride) {
    this.rewardsOverride = rewardsOverride;
    return this;
  }

  /**
   * Get rewardsOverride
   * @return rewardsOverride
   */
  @Valid 
  @Schema(name = "rewardsOverride", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewardsOverride")
  public @Nullable RewardPayload getRewardsOverride() {
    return rewardsOverride;
  }

  public void setRewardsOverride(@Nullable RewardPayload rewardsOverride) {
    this.rewardsOverride = rewardsOverride;
  }

  public OutcomeRequest reputationChanges(List<@Valid ReputationChange> reputationChanges) {
    this.reputationChanges = reputationChanges;
    return this;
  }

  public OutcomeRequest addReputationChangesItem(ReputationChange reputationChangesItem) {
    if (this.reputationChanges == null) {
      this.reputationChanges = new ArrayList<>();
    }
    this.reputationChanges.add(reputationChangesItem);
    return this;
  }

  /**
   * Get reputationChanges
   * @return reputationChanges
   */
  @Valid 
  @Schema(name = "reputationChanges", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputationChanges")
  public List<@Valid ReputationChange> getReputationChanges() {
    return reputationChanges;
  }

  public void setReputationChanges(List<@Valid ReputationChange> reputationChanges) {
    this.reputationChanges = reputationChanges;
  }

  public OutcomeRequest analytics(@Nullable OutcomeRequestAnalytics analytics) {
    this.analytics = analytics;
    return this;
  }

  /**
   * Get analytics
   * @return analytics
   */
  @Valid 
  @Schema(name = "analytics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("analytics")
  public @Nullable OutcomeRequestAnalytics getAnalytics() {
    return analytics;
  }

  public void setAnalytics(@Nullable OutcomeRequestAnalytics analytics) {
    this.analytics = analytics;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OutcomeRequest outcomeRequest = (OutcomeRequest) o;
    return Objects.equals(this.playerId, outcomeRequest.playerId) &&
        Objects.equals(this.outcome, outcomeRequest.outcome) &&
        Objects.equals(this.rewardsOverride, outcomeRequest.rewardsOverride) &&
        Objects.equals(this.reputationChanges, outcomeRequest.reputationChanges) &&
        Objects.equals(this.analytics, outcomeRequest.analytics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, outcome, rewardsOverride, reputationChanges, analytics);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OutcomeRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    outcome: ").append(toIndentedString(outcome)).append("\n");
    sb.append("    rewardsOverride: ").append(toIndentedString(rewardsOverride)).append("\n");
    sb.append("    reputationChanges: ").append(toIndentedString(reputationChanges)).append("\n");
    sb.append("    analytics: ").append(toIndentedString(analytics)).append("\n");
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

