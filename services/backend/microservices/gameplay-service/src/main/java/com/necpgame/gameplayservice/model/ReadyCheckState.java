package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.ReadyCheckResponse;
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
 * ReadyCheckState
 */


public class ReadyCheckState {

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    INITIATED("INITIATED"),
    
    IN_PROGRESS("IN_PROGRESS"),
    
    SUCCEEDED("SUCCEEDED"),
    
    FAILED("FAILED"),
    
    EXPIRED("EXPIRED");

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

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  private @Nullable UUID initiatedBy;

  @Valid
  private List<@Valid ReadyCheckResponse> responses = new ArrayList<>();

  public ReadyCheckState() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReadyCheckState(StatusEnum status) {
    this.status = status;
  }

  public ReadyCheckState status(StatusEnum status) {
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

  public ReadyCheckState expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expiresAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expiresAt")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public ReadyCheckState initiatedBy(@Nullable UUID initiatedBy) {
    this.initiatedBy = initiatedBy;
    return this;
  }

  /**
   * Get initiatedBy
   * @return initiatedBy
   */
  @Valid 
  @Schema(name = "initiatedBy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("initiatedBy")
  public @Nullable UUID getInitiatedBy() {
    return initiatedBy;
  }

  public void setInitiatedBy(@Nullable UUID initiatedBy) {
    this.initiatedBy = initiatedBy;
  }

  public ReadyCheckState responses(List<@Valid ReadyCheckResponse> responses) {
    this.responses = responses;
    return this;
  }

  public ReadyCheckState addResponsesItem(ReadyCheckResponse responsesItem) {
    if (this.responses == null) {
      this.responses = new ArrayList<>();
    }
    this.responses.add(responsesItem);
    return this;
  }

  /**
   * Get responses
   * @return responses
   */
  @Valid 
  @Schema(name = "responses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("responses")
  public List<@Valid ReadyCheckResponse> getResponses() {
    return responses;
  }

  public void setResponses(List<@Valid ReadyCheckResponse> responses) {
    this.responses = responses;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReadyCheckState readyCheckState = (ReadyCheckState) o;
    return Objects.equals(this.status, readyCheckState.status) &&
        Objects.equals(this.expiresAt, readyCheckState.expiresAt) &&
        Objects.equals(this.initiatedBy, readyCheckState.initiatedBy) &&
        Objects.equals(this.responses, readyCheckState.responses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, expiresAt, initiatedBy, responses);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReadyCheckState {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    initiatedBy: ").append(toIndentedString(initiatedBy)).append("\n");
    sb.append("    responses: ").append(toIndentedString(responses)).append("\n");
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

