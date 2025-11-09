package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ChallengeProgressUpdateRequest
 */


public class ChallengeProgressUpdateRequest {

  private String playerId;

  private String metric;

  private Integer amount;

  private @Nullable String sourceEventId;

  private @Nullable Boolean allowOverflow;

  public ChallengeProgressUpdateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChallengeProgressUpdateRequest(String playerId, String metric, Integer amount) {
    this.playerId = playerId;
    this.metric = metric;
    this.amount = amount;
  }

  public ChallengeProgressUpdateRequest playerId(String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public ChallengeProgressUpdateRequest metric(String metric) {
    this.metric = metric;
    return this;
  }

  /**
   * Get metric
   * @return metric
   */
  @NotNull 
  @Schema(name = "metric", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("metric")
  public String getMetric() {
    return metric;
  }

  public void setMetric(String metric) {
    this.metric = metric;
  }

  public ChallengeProgressUpdateRequest amount(Integer amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
   */
  @NotNull 
  @Schema(name = "amount", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("amount")
  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  public ChallengeProgressUpdateRequest sourceEventId(@Nullable String sourceEventId) {
    this.sourceEventId = sourceEventId;
    return this;
  }

  /**
   * Get sourceEventId
   * @return sourceEventId
   */
  
  @Schema(name = "sourceEventId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sourceEventId")
  public @Nullable String getSourceEventId() {
    return sourceEventId;
  }

  public void setSourceEventId(@Nullable String sourceEventId) {
    this.sourceEventId = sourceEventId;
  }

  public ChallengeProgressUpdateRequest allowOverflow(@Nullable Boolean allowOverflow) {
    this.allowOverflow = allowOverflow;
    return this;
  }

  /**
   * Get allowOverflow
   * @return allowOverflow
   */
  
  @Schema(name = "allowOverflow", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allowOverflow")
  public @Nullable Boolean getAllowOverflow() {
    return allowOverflow;
  }

  public void setAllowOverflow(@Nullable Boolean allowOverflow) {
    this.allowOverflow = allowOverflow;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChallengeProgressUpdateRequest challengeProgressUpdateRequest = (ChallengeProgressUpdateRequest) o;
    return Objects.equals(this.playerId, challengeProgressUpdateRequest.playerId) &&
        Objects.equals(this.metric, challengeProgressUpdateRequest.metric) &&
        Objects.equals(this.amount, challengeProgressUpdateRequest.amount) &&
        Objects.equals(this.sourceEventId, challengeProgressUpdateRequest.sourceEventId) &&
        Objects.equals(this.allowOverflow, challengeProgressUpdateRequest.allowOverflow);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, metric, amount, sourceEventId, allowOverflow);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChallengeProgressUpdateRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    metric: ").append(toIndentedString(metric)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    sourceEventId: ").append(toIndentedString(sourceEventId)).append("\n");
    sb.append("    allowOverflow: ").append(toIndentedString(allowOverflow)).append("\n");
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

