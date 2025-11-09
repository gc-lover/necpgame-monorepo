package com.necpgame.partyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.partyservice.model.ReadyCheckResponse;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * ReadyCheck
 */


public class ReadyCheck {

  private @Nullable String readyCheckId;

  private @Nullable String initiatorId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  @Valid
  private List<@Valid ReadyCheckResponse> responses = new ArrayList<>();

  public ReadyCheck readyCheckId(@Nullable String readyCheckId) {
    this.readyCheckId = readyCheckId;
    return this;
  }

  /**
   * Get readyCheckId
   * @return readyCheckId
   */
  
  @Schema(name = "readyCheckId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("readyCheckId")
  public @Nullable String getReadyCheckId() {
    return readyCheckId;
  }

  public void setReadyCheckId(@Nullable String readyCheckId) {
    this.readyCheckId = readyCheckId;
  }

  public ReadyCheck initiatorId(@Nullable String initiatorId) {
    this.initiatorId = initiatorId;
    return this;
  }

  /**
   * Get initiatorId
   * @return initiatorId
   */
  
  @Schema(name = "initiatorId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("initiatorId")
  public @Nullable String getInitiatorId() {
    return initiatorId;
  }

  public void setInitiatorId(@Nullable String initiatorId) {
    this.initiatorId = initiatorId;
  }

  public ReadyCheck expiresAt(@Nullable OffsetDateTime expiresAt) {
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

  public ReadyCheck responses(List<@Valid ReadyCheckResponse> responses) {
    this.responses = responses;
    return this;
  }

  public ReadyCheck addResponsesItem(ReadyCheckResponse responsesItem) {
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
    ReadyCheck readyCheck = (ReadyCheck) o;
    return Objects.equals(this.readyCheckId, readyCheck.readyCheckId) &&
        Objects.equals(this.initiatorId, readyCheck.initiatorId) &&
        Objects.equals(this.expiresAt, readyCheck.expiresAt) &&
        Objects.equals(this.responses, readyCheck.responses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(readyCheckId, initiatorId, expiresAt, responses);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReadyCheck {\n");
    sb.append("    readyCheckId: ").append(toIndentedString(readyCheckId)).append("\n");
    sb.append("    initiatorId: ").append(toIndentedString(initiatorId)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
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

