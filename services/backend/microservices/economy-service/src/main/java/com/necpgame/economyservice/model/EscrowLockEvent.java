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
 * EscrowLockEvent
 */


public class EscrowLockEvent {

  private UUID orderId;

  private Float escrowAmount;

  /**
   * Gets or Sets insuranceTier
   */
  public enum InsuranceTierEnum {
    BASIC("basic"),
    
    EXTENDED("extended"),
    
    PREMIUM("premium");

    private final String value;

    InsuranceTierEnum(String value) {
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
    public static InsuranceTierEnum fromValue(String value) {
      for (InsuranceTierEnum b : InsuranceTierEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private InsuranceTierEnum insuranceTier;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    LOCKED("locked"),
    
    PENDING("pending"),
    
    RELEASED("released");

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
  private OffsetDateTime emittedAt;

  private @Nullable UUID traceId;

  public EscrowLockEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EscrowLockEvent(UUID orderId, Float escrowAmount, InsuranceTierEnum insuranceTier, StatusEnum status, OffsetDateTime emittedAt) {
    this.orderId = orderId;
    this.escrowAmount = escrowAmount;
    this.insuranceTier = insuranceTier;
    this.status = status;
    this.emittedAt = emittedAt;
  }

  public EscrowLockEvent orderId(UUID orderId) {
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

  public EscrowLockEvent escrowAmount(Float escrowAmount) {
    this.escrowAmount = escrowAmount;
    return this;
  }

  /**
   * Get escrowAmount
   * @return escrowAmount
   */
  @NotNull 
  @Schema(name = "escrowAmount", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("escrowAmount")
  public Float getEscrowAmount() {
    return escrowAmount;
  }

  public void setEscrowAmount(Float escrowAmount) {
    this.escrowAmount = escrowAmount;
  }

  public EscrowLockEvent insuranceTier(InsuranceTierEnum insuranceTier) {
    this.insuranceTier = insuranceTier;
    return this;
  }

  /**
   * Get insuranceTier
   * @return insuranceTier
   */
  @NotNull 
  @Schema(name = "insuranceTier", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("insuranceTier")
  public InsuranceTierEnum getInsuranceTier() {
    return insuranceTier;
  }

  public void setInsuranceTier(InsuranceTierEnum insuranceTier) {
    this.insuranceTier = insuranceTier;
  }

  public EscrowLockEvent status(StatusEnum status) {
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

  public EscrowLockEvent emittedAt(OffsetDateTime emittedAt) {
    this.emittedAt = emittedAt;
    return this;
  }

  /**
   * Get emittedAt
   * @return emittedAt
   */
  @NotNull @Valid 
  @Schema(name = "emittedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("emittedAt")
  public OffsetDateTime getEmittedAt() {
    return emittedAt;
  }

  public void setEmittedAt(OffsetDateTime emittedAt) {
    this.emittedAt = emittedAt;
  }

  public EscrowLockEvent traceId(@Nullable UUID traceId) {
    this.traceId = traceId;
    return this;
  }

  /**
   * Get traceId
   * @return traceId
   */
  @Valid 
  @Schema(name = "traceId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("traceId")
  public @Nullable UUID getTraceId() {
    return traceId;
  }

  public void setTraceId(@Nullable UUID traceId) {
    this.traceId = traceId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EscrowLockEvent escrowLockEvent = (EscrowLockEvent) o;
    return Objects.equals(this.orderId, escrowLockEvent.orderId) &&
        Objects.equals(this.escrowAmount, escrowLockEvent.escrowAmount) &&
        Objects.equals(this.insuranceTier, escrowLockEvent.insuranceTier) &&
        Objects.equals(this.status, escrowLockEvent.status) &&
        Objects.equals(this.emittedAt, escrowLockEvent.emittedAt) &&
        Objects.equals(this.traceId, escrowLockEvent.traceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, escrowAmount, insuranceTier, status, emittedAt, traceId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EscrowLockEvent {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    escrowAmount: ").append(toIndentedString(escrowAmount)).append("\n");
    sb.append("    insuranceTier: ").append(toIndentedString(insuranceTier)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    emittedAt: ").append(toIndentedString(emittedAt)).append("\n");
    sb.append("    traceId: ").append(toIndentedString(traceId)).append("\n");
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

