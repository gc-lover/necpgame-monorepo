package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.MatchResult;
import com.necpgame.gameplayservice.model.RatingBonus;
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
 * RatingDeltaRequest
 */


public class RatingDeltaRequest {

  private UUID matchId;

  private UUID playerId;

  private @Nullable Integer opponentRating;

  private MatchResult result;

  @Valid
  private List<@Valid RatingBonus> bonusAdjustments = new ArrayList<>();

  private Boolean placementFlag = false;

  private @Nullable UUID queueTicketId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime processedAt;

  public RatingDeltaRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RatingDeltaRequest(UUID matchId, UUID playerId, MatchResult result) {
    this.matchId = matchId;
    this.playerId = playerId;
    this.result = result;
  }

  public RatingDeltaRequest matchId(UUID matchId) {
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

  public RatingDeltaRequest playerId(UUID playerId) {
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

  public RatingDeltaRequest opponentRating(@Nullable Integer opponentRating) {
    this.opponentRating = opponentRating;
    return this;
  }

  /**
   * Get opponentRating
   * @return opponentRating
   */
  
  @Schema(name = "opponentRating", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("opponentRating")
  public @Nullable Integer getOpponentRating() {
    return opponentRating;
  }

  public void setOpponentRating(@Nullable Integer opponentRating) {
    this.opponentRating = opponentRating;
  }

  public RatingDeltaRequest result(MatchResult result) {
    this.result = result;
    return this;
  }

  /**
   * Get result
   * @return result
   */
  @NotNull @Valid 
  @Schema(name = "result", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("result")
  public MatchResult getResult() {
    return result;
  }

  public void setResult(MatchResult result) {
    this.result = result;
  }

  public RatingDeltaRequest bonusAdjustments(List<@Valid RatingBonus> bonusAdjustments) {
    this.bonusAdjustments = bonusAdjustments;
    return this;
  }

  public RatingDeltaRequest addBonusAdjustmentsItem(RatingBonus bonusAdjustmentsItem) {
    if (this.bonusAdjustments == null) {
      this.bonusAdjustments = new ArrayList<>();
    }
    this.bonusAdjustments.add(bonusAdjustmentsItem);
    return this;
  }

  /**
   * Get bonusAdjustments
   * @return bonusAdjustments
   */
  @Valid 
  @Schema(name = "bonusAdjustments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonusAdjustments")
  public List<@Valid RatingBonus> getBonusAdjustments() {
    return bonusAdjustments;
  }

  public void setBonusAdjustments(List<@Valid RatingBonus> bonusAdjustments) {
    this.bonusAdjustments = bonusAdjustments;
  }

  public RatingDeltaRequest placementFlag(Boolean placementFlag) {
    this.placementFlag = placementFlag;
    return this;
  }

  /**
   * Get placementFlag
   * @return placementFlag
   */
  
  @Schema(name = "placementFlag", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("placementFlag")
  public Boolean getPlacementFlag() {
    return placementFlag;
  }

  public void setPlacementFlag(Boolean placementFlag) {
    this.placementFlag = placementFlag;
  }

  public RatingDeltaRequest queueTicketId(@Nullable UUID queueTicketId) {
    this.queueTicketId = queueTicketId;
    return this;
  }

  /**
   * Get queueTicketId
   * @return queueTicketId
   */
  @Valid 
  @Schema(name = "queueTicketId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("queueTicketId")
  public @Nullable UUID getQueueTicketId() {
    return queueTicketId;
  }

  public void setQueueTicketId(@Nullable UUID queueTicketId) {
    this.queueTicketId = queueTicketId;
  }

  public RatingDeltaRequest processedAt(@Nullable OffsetDateTime processedAt) {
    this.processedAt = processedAt;
    return this;
  }

  /**
   * Get processedAt
   * @return processedAt
   */
  @Valid 
  @Schema(name = "processedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("processedAt")
  public @Nullable OffsetDateTime getProcessedAt() {
    return processedAt;
  }

  public void setProcessedAt(@Nullable OffsetDateTime processedAt) {
    this.processedAt = processedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RatingDeltaRequest ratingDeltaRequest = (RatingDeltaRequest) o;
    return Objects.equals(this.matchId, ratingDeltaRequest.matchId) &&
        Objects.equals(this.playerId, ratingDeltaRequest.playerId) &&
        Objects.equals(this.opponentRating, ratingDeltaRequest.opponentRating) &&
        Objects.equals(this.result, ratingDeltaRequest.result) &&
        Objects.equals(this.bonusAdjustments, ratingDeltaRequest.bonusAdjustments) &&
        Objects.equals(this.placementFlag, ratingDeltaRequest.placementFlag) &&
        Objects.equals(this.queueTicketId, ratingDeltaRequest.queueTicketId) &&
        Objects.equals(this.processedAt, ratingDeltaRequest.processedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(matchId, playerId, opponentRating, result, bonusAdjustments, placementFlag, queueTicketId, processedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RatingDeltaRequest {\n");
    sb.append("    matchId: ").append(toIndentedString(matchId)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    opponentRating: ").append(toIndentedString(opponentRating)).append("\n");
    sb.append("    result: ").append(toIndentedString(result)).append("\n");
    sb.append("    bonusAdjustments: ").append(toIndentedString(bonusAdjustments)).append("\n");
    sb.append("    placementFlag: ").append(toIndentedString(placementFlag)).append("\n");
    sb.append("    queueTicketId: ").append(toIndentedString(queueTicketId)).append("\n");
    sb.append("    processedAt: ").append(toIndentedString(processedAt)).append("\n");
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

