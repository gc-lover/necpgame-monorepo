package com.necpgame.economyservice.model;

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
 * CreatePriceAlert200Response
 */

@JsonTypeName("createPriceAlert_200_response")

public class CreatePriceAlert200Response {

  private @Nullable String alertId;

  private @Nullable String status;

  public CreatePriceAlert200Response alertId(@Nullable String alertId) {
    this.alertId = alertId;
    return this;
  }

  /**
   * Get alertId
   * @return alertId
   */
  
  @Schema(name = "alert_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alert_id")
  public @Nullable String getAlertId() {
    return alertId;
  }

  public void setAlertId(@Nullable String alertId) {
    this.alertId = alertId;
  }

  public CreatePriceAlert200Response status(@Nullable String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable String getStatus() {
    return status;
  }

  public void setStatus(@Nullable String status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreatePriceAlert200Response createPriceAlert200Response = (CreatePriceAlert200Response) o;
    return Objects.equals(this.alertId, createPriceAlert200Response.alertId) &&
        Objects.equals(this.status, createPriceAlert200Response.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(alertId, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreatePriceAlert200Response {\n");
    sb.append("    alertId: ").append(toIndentedString(alertId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

