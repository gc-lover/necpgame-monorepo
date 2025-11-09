package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * ValidatePlayerOrderRequest
 */

@JsonTypeName("validatePlayerOrder_request")

public class ValidatePlayerOrderRequest {

  private UUID orderId;

  private @Nullable Boolean force;

  public ValidatePlayerOrderRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ValidatePlayerOrderRequest(UUID orderId) {
    this.orderId = orderId;
  }

  public ValidatePlayerOrderRequest orderId(UUID orderId) {
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

  public ValidatePlayerOrderRequest force(@Nullable Boolean force) {
    this.force = force;
    return this;
  }

  /**
   * Форсировать валидацию, игнорируя кэшированные результаты.
   * @return force
   */
  
  @Schema(name = "force", description = "Форсировать валидацию, игнорируя кэшированные результаты.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("force")
  public @Nullable Boolean getForce() {
    return force;
  }

  public void setForce(@Nullable Boolean force) {
    this.force = force;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValidatePlayerOrderRequest validatePlayerOrderRequest = (ValidatePlayerOrderRequest) o;
    return Objects.equals(this.orderId, validatePlayerOrderRequest.orderId) &&
        Objects.equals(this.force, validatePlayerOrderRequest.force);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, force);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ValidatePlayerOrderRequest {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    force: ").append(toIndentedString(force)).append("\n");
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

