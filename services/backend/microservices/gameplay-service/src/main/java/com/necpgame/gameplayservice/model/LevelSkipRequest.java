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
 * LevelSkipRequest
 */


public class LevelSkipRequest {

  private String playerId;

  private String seasonId;

  private Integer levelsToSkip;

  private @Nullable String paymentMethod;

  private @Nullable Integer cost;

  private @Nullable String auditId;

  public LevelSkipRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LevelSkipRequest(String playerId, String seasonId, Integer levelsToSkip) {
    this.playerId = playerId;
    this.seasonId = seasonId;
    this.levelsToSkip = levelsToSkip;
  }

  public LevelSkipRequest playerId(String playerId) {
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

  public LevelSkipRequest seasonId(String seasonId) {
    this.seasonId = seasonId;
    return this;
  }

  /**
   * Get seasonId
   * @return seasonId
   */
  @NotNull 
  @Schema(name = "seasonId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("seasonId")
  public String getSeasonId() {
    return seasonId;
  }

  public void setSeasonId(String seasonId) {
    this.seasonId = seasonId;
  }

  public LevelSkipRequest levelsToSkip(Integer levelsToSkip) {
    this.levelsToSkip = levelsToSkip;
    return this;
  }

  /**
   * Get levelsToSkip
   * minimum: 1
   * @return levelsToSkip
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "levelsToSkip", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("levelsToSkip")
  public Integer getLevelsToSkip() {
    return levelsToSkip;
  }

  public void setLevelsToSkip(Integer levelsToSkip) {
    this.levelsToSkip = levelsToSkip;
  }

  public LevelSkipRequest paymentMethod(@Nullable String paymentMethod) {
    this.paymentMethod = paymentMethod;
    return this;
  }

  /**
   * Get paymentMethod
   * @return paymentMethod
   */
  
  @Schema(name = "paymentMethod", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("paymentMethod")
  public @Nullable String getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(@Nullable String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public LevelSkipRequest cost(@Nullable Integer cost) {
    this.cost = cost;
    return this;
  }

  /**
   * Get cost
   * @return cost
   */
  
  @Schema(name = "cost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cost")
  public @Nullable Integer getCost() {
    return cost;
  }

  public void setCost(@Nullable Integer cost) {
    this.cost = cost;
  }

  public LevelSkipRequest auditId(@Nullable String auditId) {
    this.auditId = auditId;
    return this;
  }

  /**
   * Get auditId
   * @return auditId
   */
  
  @Schema(name = "auditId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("auditId")
  public @Nullable String getAuditId() {
    return auditId;
  }

  public void setAuditId(@Nullable String auditId) {
    this.auditId = auditId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LevelSkipRequest levelSkipRequest = (LevelSkipRequest) o;
    return Objects.equals(this.playerId, levelSkipRequest.playerId) &&
        Objects.equals(this.seasonId, levelSkipRequest.seasonId) &&
        Objects.equals(this.levelsToSkip, levelSkipRequest.levelsToSkip) &&
        Objects.equals(this.paymentMethod, levelSkipRequest.paymentMethod) &&
        Objects.equals(this.cost, levelSkipRequest.cost) &&
        Objects.equals(this.auditId, levelSkipRequest.auditId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, seasonId, levelsToSkip, paymentMethod, cost, auditId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LevelSkipRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    seasonId: ").append(toIndentedString(seasonId)).append("\n");
    sb.append("    levelsToSkip: ").append(toIndentedString(levelsToSkip)).append("\n");
    sb.append("    paymentMethod: ").append(toIndentedString(paymentMethod)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
    sb.append("    auditId: ").append(toIndentedString(auditId)).append("\n");
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

