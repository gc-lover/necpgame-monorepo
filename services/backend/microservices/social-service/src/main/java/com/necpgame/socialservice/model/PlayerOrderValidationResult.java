package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.PlayerOrderValidationSummary;
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
 * PlayerOrderValidationResult
 */


public class PlayerOrderValidationResult {

  private UUID orderId;

  private PlayerOrderValidationSummary validation;

  private @Nullable String telemetryId;

  public PlayerOrderValidationResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderValidationResult(UUID orderId, PlayerOrderValidationSummary validation) {
    this.orderId = orderId;
    this.validation = validation;
  }

  public PlayerOrderValidationResult orderId(UUID orderId) {
    this.orderId = orderId;
    return this;
  }

  /**
   * Get orderId
   * @return orderId
   */
  @NotNull @Valid 
  @Schema(name = "orderId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("orderId")
  public UUID getOrderId() {
    return orderId;
  }

  public void setOrderId(UUID orderId) {
    this.orderId = orderId;
  }

  public PlayerOrderValidationResult validation(PlayerOrderValidationSummary validation) {
    this.validation = validation;
    return this;
  }

  /**
   * Get validation
   * @return validation
   */
  @NotNull @Valid 
  @Schema(name = "validation", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("validation")
  public PlayerOrderValidationSummary getValidation() {
    return validation;
  }

  public void setValidation(PlayerOrderValidationSummary validation) {
    this.validation = validation;
  }

  public PlayerOrderValidationResult telemetryId(@Nullable String telemetryId) {
    this.telemetryId = telemetryId;
    return this;
  }

  /**
   * Идентификатор записи в telemetry-service.
   * @return telemetryId
   */
  
  @Schema(name = "telemetryId", description = "Идентификатор записи в telemetry-service.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("telemetryId")
  public @Nullable String getTelemetryId() {
    return telemetryId;
  }

  public void setTelemetryId(@Nullable String telemetryId) {
    this.telemetryId = telemetryId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderValidationResult playerOrderValidationResult = (PlayerOrderValidationResult) o;
    return Objects.equals(this.orderId, playerOrderValidationResult.orderId) &&
        Objects.equals(this.validation, playerOrderValidationResult.validation) &&
        Objects.equals(this.telemetryId, playerOrderValidationResult.telemetryId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, validation, telemetryId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderValidationResult {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    validation: ").append(toIndentedString(validation)).append("\n");
    sb.append("    telemetryId: ").append(toIndentedString(telemetryId)).append("\n");
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

