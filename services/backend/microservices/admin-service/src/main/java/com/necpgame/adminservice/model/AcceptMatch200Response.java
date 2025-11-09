package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AcceptMatch200Response
 */

@JsonTypeName("acceptMatch_200_response")

public class AcceptMatch200Response {

  private @Nullable Boolean success;

  private @Nullable String matchId;

  private @Nullable Integer acceptedCount;

  private @Nullable Integer requiredCount;

  public AcceptMatch200Response success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public AcceptMatch200Response matchId(@Nullable String matchId) {
    this.matchId = matchId;
    return this;
  }

  /**
   * Get matchId
   * @return matchId
   */
  
  @Schema(name = "match_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("match_id")
  public @Nullable String getMatchId() {
    return matchId;
  }

  public void setMatchId(@Nullable String matchId) {
    this.matchId = matchId;
  }

  public AcceptMatch200Response acceptedCount(@Nullable Integer acceptedCount) {
    this.acceptedCount = acceptedCount;
    return this;
  }

  /**
   * Get acceptedCount
   * @return acceptedCount
   */
  
  @Schema(name = "accepted_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("accepted_count")
  public @Nullable Integer getAcceptedCount() {
    return acceptedCount;
  }

  public void setAcceptedCount(@Nullable Integer acceptedCount) {
    this.acceptedCount = acceptedCount;
  }

  public AcceptMatch200Response requiredCount(@Nullable Integer requiredCount) {
    this.requiredCount = requiredCount;
    return this;
  }

  /**
   * Get requiredCount
   * @return requiredCount
   */
  
  @Schema(name = "required_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_count")
  public @Nullable Integer getRequiredCount() {
    return requiredCount;
  }

  public void setRequiredCount(@Nullable Integer requiredCount) {
    this.requiredCount = requiredCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AcceptMatch200Response acceptMatch200Response = (AcceptMatch200Response) o;
    return Objects.equals(this.success, acceptMatch200Response.success) &&
        Objects.equals(this.matchId, acceptMatch200Response.matchId) &&
        Objects.equals(this.acceptedCount, acceptMatch200Response.acceptedCount) &&
        Objects.equals(this.requiredCount, acceptMatch200Response.requiredCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, matchId, acceptedCount, requiredCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AcceptMatch200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    matchId: ").append(toIndentedString(matchId)).append("\n");
    sb.append("    acceptedCount: ").append(toIndentedString(acceptedCount)).append("\n");
    sb.append("    requiredCount: ").append(toIndentedString(requiredCount)).append("\n");
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

