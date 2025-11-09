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
 * ChallengeCompleteRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ChallengeCompleteRequest {

  private String playerId;

  private @Nullable Integer awardedXp;

  public ChallengeCompleteRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChallengeCompleteRequest(String playerId) {
    this.playerId = playerId;
  }

  public ChallengeCompleteRequest playerId(String playerId) {
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

  public ChallengeCompleteRequest awardedXp(@Nullable Integer awardedXp) {
    this.awardedXp = awardedXp;
    return this;
  }

  /**
   * Get awardedXp
   * @return awardedXp
   */
  
  @Schema(name = "awardedXp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("awardedXp")
  public @Nullable Integer getAwardedXp() {
    return awardedXp;
  }

  public void setAwardedXp(@Nullable Integer awardedXp) {
    this.awardedXp = awardedXp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChallengeCompleteRequest challengeCompleteRequest = (ChallengeCompleteRequest) o;
    return Objects.equals(this.playerId, challengeCompleteRequest.playerId) &&
        Objects.equals(this.awardedXp, challengeCompleteRequest.awardedXp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, awardedXp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChallengeCompleteRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    awardedXp: ").append(toIndentedString(awardedXp)).append("\n");
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

