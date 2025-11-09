package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * EscrowLockRequest
 */


public class EscrowLockRequest {

  private UUID ownerId;

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

  private @Nullable Integer holdDurationMinutes;

  private @Nullable String currency;

  private @Nullable UUID auditTraceId;

  public EscrowLockRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EscrowLockRequest(UUID ownerId, Float escrowAmount, InsuranceTierEnum insuranceTier) {
    this.ownerId = ownerId;
    this.escrowAmount = escrowAmount;
    this.insuranceTier = insuranceTier;
  }

  public EscrowLockRequest ownerId(UUID ownerId) {
    this.ownerId = ownerId;
    return this;
  }

  /**
   * Get ownerId
   * @return ownerId
   */
  @NotNull @Valid 
  @Schema(name = "ownerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ownerId")
  public UUID getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(UUID ownerId) {
    this.ownerId = ownerId;
  }

  public EscrowLockRequest escrowAmount(Float escrowAmount) {
    this.escrowAmount = escrowAmount;
    return this;
  }

  /**
   * Get escrowAmount
   * minimum: 0
   * @return escrowAmount
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "escrowAmount", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("escrowAmount")
  public Float getEscrowAmount() {
    return escrowAmount;
  }

  public void setEscrowAmount(Float escrowAmount) {
    this.escrowAmount = escrowAmount;
  }

  public EscrowLockRequest insuranceTier(InsuranceTierEnum insuranceTier) {
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

  public EscrowLockRequest holdDurationMinutes(@Nullable Integer holdDurationMinutes) {
    this.holdDurationMinutes = holdDurationMinutes;
    return this;
  }

  /**
   * Get holdDurationMinutes
   * minimum: 5
   * maximum: 1440
   * @return holdDurationMinutes
   */
  @Min(value = 5) @Max(value = 1440) 
  @Schema(name = "holdDurationMinutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("holdDurationMinutes")
  public @Nullable Integer getHoldDurationMinutes() {
    return holdDurationMinutes;
  }

  public void setHoldDurationMinutes(@Nullable Integer holdDurationMinutes) {
    this.holdDurationMinutes = holdDurationMinutes;
  }

  public EscrowLockRequest currency(@Nullable String currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Get currency
   * @return currency
   */
  @Pattern(regexp = "^[A-Z]{3}$") 
  @Schema(name = "currency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency")
  public @Nullable String getCurrency() {
    return currency;
  }

  public void setCurrency(@Nullable String currency) {
    this.currency = currency;
  }

  public EscrowLockRequest auditTraceId(@Nullable UUID auditTraceId) {
    this.auditTraceId = auditTraceId;
    return this;
  }

  /**
   * Get auditTraceId
   * @return auditTraceId
   */
  @Valid 
  @Schema(name = "auditTraceId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("auditTraceId")
  public @Nullable UUID getAuditTraceId() {
    return auditTraceId;
  }

  public void setAuditTraceId(@Nullable UUID auditTraceId) {
    this.auditTraceId = auditTraceId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EscrowLockRequest escrowLockRequest = (EscrowLockRequest) o;
    return Objects.equals(this.ownerId, escrowLockRequest.ownerId) &&
        Objects.equals(this.escrowAmount, escrowLockRequest.escrowAmount) &&
        Objects.equals(this.insuranceTier, escrowLockRequest.insuranceTier) &&
        Objects.equals(this.holdDurationMinutes, escrowLockRequest.holdDurationMinutes) &&
        Objects.equals(this.currency, escrowLockRequest.currency) &&
        Objects.equals(this.auditTraceId, escrowLockRequest.auditTraceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ownerId, escrowAmount, insuranceTier, holdDurationMinutes, currency, auditTraceId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EscrowLockRequest {\n");
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
    sb.append("    escrowAmount: ").append(toIndentedString(escrowAmount)).append("\n");
    sb.append("    insuranceTier: ").append(toIndentedString(insuranceTier)).append("\n");
    sb.append("    holdDurationMinutes: ").append(toIndentedString(holdDurationMinutes)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    auditTraceId: ").append(toIndentedString(auditTraceId)).append("\n");
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

