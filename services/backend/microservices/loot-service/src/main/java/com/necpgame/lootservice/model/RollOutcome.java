package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * RollOutcome
 */


public class RollOutcome {

  private @Nullable UUID winnerId;

  private @Nullable String reason;

  private @Nullable Boolean rerollSuggested;

  public RollOutcome winnerId(@Nullable UUID winnerId) {
    this.winnerId = winnerId;
    return this;
  }

  /**
   * Get winnerId
   * @return winnerId
   */
  @Valid 
  @Schema(name = "winnerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("winnerId")
  public @Nullable UUID getWinnerId() {
    return winnerId;
  }

  public void setWinnerId(@Nullable UUID winnerId) {
    this.winnerId = winnerId;
  }

  public RollOutcome reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  public RollOutcome rerollSuggested(@Nullable Boolean rerollSuggested) {
    this.rerollSuggested = rerollSuggested;
    return this;
  }

  /**
   * Get rerollSuggested
   * @return rerollSuggested
   */
  
  @Schema(name = "rerollSuggested", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rerollSuggested")
  public @Nullable Boolean getRerollSuggested() {
    return rerollSuggested;
  }

  public void setRerollSuggested(@Nullable Boolean rerollSuggested) {
    this.rerollSuggested = rerollSuggested;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RollOutcome rollOutcome = (RollOutcome) o;
    return Objects.equals(this.winnerId, rollOutcome.winnerId) &&
        Objects.equals(this.reason, rollOutcome.reason) &&
        Objects.equals(this.rerollSuggested, rollOutcome.rerollSuggested);
  }

  @Override
  public int hashCode() {
    return Objects.hash(winnerId, reason, rerollSuggested);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RollOutcome {\n");
    sb.append("    winnerId: ").append(toIndentedString(winnerId)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    rerollSuggested: ").append(toIndentedString(rerollSuggested)).append("\n");
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

