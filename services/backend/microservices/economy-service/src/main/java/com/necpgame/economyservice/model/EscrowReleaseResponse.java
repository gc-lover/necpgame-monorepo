package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
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
 * EscrowReleaseResponse
 */


public class EscrowReleaseResponse {

  private UUID orderId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    RELEASED("released"),
    
    PENDING("pending");

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
  private @Nullable OffsetDateTime releasedAt;

  public EscrowReleaseResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EscrowReleaseResponse(UUID orderId, StatusEnum status) {
    this.orderId = orderId;
    this.status = status;
  }

  public EscrowReleaseResponse orderId(UUID orderId) {
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

  public EscrowReleaseResponse status(StatusEnum status) {
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

  public EscrowReleaseResponse releasedAt(@Nullable OffsetDateTime releasedAt) {
    this.releasedAt = releasedAt;
    return this;
  }

  /**
   * Get releasedAt
   * @return releasedAt
   */
  @Valid 
  @Schema(name = "releasedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("releasedAt")
  public @Nullable OffsetDateTime getReleasedAt() {
    return releasedAt;
  }

  public void setReleasedAt(@Nullable OffsetDateTime releasedAt) {
    this.releasedAt = releasedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EscrowReleaseResponse escrowReleaseResponse = (EscrowReleaseResponse) o;
    return Objects.equals(this.orderId, escrowReleaseResponse.orderId) &&
        Objects.equals(this.status, escrowReleaseResponse.status) &&
        Objects.equals(this.releasedAt, escrowReleaseResponse.releasedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, status, releasedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EscrowReleaseResponse {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    releasedAt: ").append(toIndentedString(releasedAt)).append("\n");
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

