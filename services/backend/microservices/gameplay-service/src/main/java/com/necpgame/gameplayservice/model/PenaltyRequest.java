package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.Penalty;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PenaltyRequest
 */


public class PenaltyRequest {

  private Penalty penalty;

  private @Nullable String reason;

  public PenaltyRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PenaltyRequest(Penalty penalty) {
    this.penalty = penalty;
  }

  public PenaltyRequest penalty(Penalty penalty) {
    this.penalty = penalty;
    return this;
  }

  /**
   * Get penalty
   * @return penalty
   */
  @NotNull @Valid 
  @Schema(name = "penalty", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("penalty")
  public Penalty getPenalty() {
    return penalty;
  }

  public void setPenalty(Penalty penalty) {
    this.penalty = penalty;
  }

  public PenaltyRequest reason(@Nullable String reason) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PenaltyRequest penaltyRequest = (PenaltyRequest) o;
    return Objects.equals(this.penalty, penaltyRequest.penalty) &&
        Objects.equals(this.reason, penaltyRequest.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(penalty, reason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PenaltyRequest {\n");
    sb.append("    penalty: ").append(toIndentedString(penalty)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
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

