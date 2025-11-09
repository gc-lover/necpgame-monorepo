package com.necpgame.backjava.model;

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
 * ChallengeRerollRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ChallengeRerollRequest {

  private String playerId;

  private @Nullable String currency;

  private @Nullable Integer cost;

  public ChallengeRerollRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChallengeRerollRequest(String playerId) {
    this.playerId = playerId;
  }

  public ChallengeRerollRequest playerId(String playerId) {
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

  public ChallengeRerollRequest currency(@Nullable String currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Get currency
   * @return currency
   */
  
  @Schema(name = "currency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency")
  public @Nullable String getCurrency() {
    return currency;
  }

  public void setCurrency(@Nullable String currency) {
    this.currency = currency;
  }

  public ChallengeRerollRequest cost(@Nullable Integer cost) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChallengeRerollRequest challengeRerollRequest = (ChallengeRerollRequest) o;
    return Objects.equals(this.playerId, challengeRerollRequest.playerId) &&
        Objects.equals(this.currency, challengeRerollRequest.currency) &&
        Objects.equals(this.cost, challengeRerollRequest.cost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, currency, cost);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChallengeRerollRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
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

