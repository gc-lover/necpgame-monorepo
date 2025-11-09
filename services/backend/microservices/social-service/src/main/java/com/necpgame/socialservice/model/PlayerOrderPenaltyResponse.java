package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.PlayerOrderPenalty;
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
 * PlayerOrderPenaltyResponse
 */


public class PlayerOrderPenaltyResponse {

  private UUID penaltyId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    APPLIED("applied"),
    
    PENDING_REVIEW("pending_review"),
    
    REJECTED("rejected");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StatusEnum status;

  private @Nullable PlayerOrderPenalty penalty;

  public PlayerOrderPenaltyResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderPenaltyResponse(UUID penaltyId, StatusEnum status) {
    this.penaltyId = penaltyId;
    this.status = status;
  }

  public PlayerOrderPenaltyResponse penaltyId(UUID penaltyId) {
    this.penaltyId = penaltyId;
    return this;
  }

  /**
   * Get penaltyId
   * @return penaltyId
   */
  @NotNull @Valid 
  @Schema(name = "penaltyId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("penaltyId")
  public UUID getPenaltyId() {
    return penaltyId;
  }

  public void setPenaltyId(UUID penaltyId) {
    this.penaltyId = penaltyId;
  }

  public PlayerOrderPenaltyResponse status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public PlayerOrderPenaltyResponse penalty(@Nullable PlayerOrderPenalty penalty) {
    this.penalty = penalty;
    return this;
  }

  /**
   * Get penalty
   * @return penalty
   */
  @Valid 
  @Schema(name = "penalty", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("penalty")
  public @Nullable PlayerOrderPenalty getPenalty() {
    return penalty;
  }

  public void setPenalty(@Nullable PlayerOrderPenalty penalty) {
    this.penalty = penalty;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderPenaltyResponse playerOrderPenaltyResponse = (PlayerOrderPenaltyResponse) o;
    return Objects.equals(this.penaltyId, playerOrderPenaltyResponse.penaltyId) &&
        Objects.equals(this.status, playerOrderPenaltyResponse.status) &&
        Objects.equals(this.penalty, playerOrderPenaltyResponse.penalty);
  }

  @Override
  public int hashCode() {
    return Objects.hash(penaltyId, status, penalty);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderPenaltyResponse {\n");
    sb.append("    penaltyId: ").append(toIndentedString(penaltyId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    penalty: ").append(toIndentedString(penalty)).append("\n");
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

