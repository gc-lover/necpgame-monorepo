package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.BudgetWarning;
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
 * EscrowLockResponse
 */


public class EscrowLockResponse {

  private UUID orderId;

  private Float escrowAmount;

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

  private @Nullable InsuranceTierEnum insuranceTier;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime lockExpiresAt;

  @Valid
  private List<@Valid BudgetWarning> warnings = new ArrayList<>();

  private @Nullable UUID kafkaEventId;

  public EscrowLockResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EscrowLockResponse(UUID orderId, Float escrowAmount, StatusEnum status, OffsetDateTime lockExpiresAt) {
    this.orderId = orderId;
    this.escrowAmount = escrowAmount;
    this.status = status;
    this.lockExpiresAt = lockExpiresAt;
  }

  public EscrowLockResponse orderId(UUID orderId) {
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

  public EscrowLockResponse escrowAmount(Float escrowAmount) {
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

  public EscrowLockResponse status(StatusEnum status) {
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

  public EscrowLockResponse insuranceTier(@Nullable InsuranceTierEnum insuranceTier) {
    this.insuranceTier = insuranceTier;
    return this;
  }

  /**
   * Get insuranceTier
   * @return insuranceTier
   */
  
  @Schema(name = "insuranceTier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("insuranceTier")
  public @Nullable InsuranceTierEnum getInsuranceTier() {
    return insuranceTier;
  }

  public void setInsuranceTier(@Nullable InsuranceTierEnum insuranceTier) {
    this.insuranceTier = insuranceTier;
  }

  public EscrowLockResponse lockExpiresAt(OffsetDateTime lockExpiresAt) {
    this.lockExpiresAt = lockExpiresAt;
    return this;
  }

  /**
   * Get lockExpiresAt
   * @return lockExpiresAt
   */
  @NotNull @Valid 
  @Schema(name = "lockExpiresAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("lockExpiresAt")
  public OffsetDateTime getLockExpiresAt() {
    return lockExpiresAt;
  }

  public void setLockExpiresAt(OffsetDateTime lockExpiresAt) {
    this.lockExpiresAt = lockExpiresAt;
  }

  public EscrowLockResponse warnings(List<@Valid BudgetWarning> warnings) {
    this.warnings = warnings;
    return this;
  }

  public EscrowLockResponse addWarningsItem(BudgetWarning warningsItem) {
    if (this.warnings == null) {
      this.warnings = new ArrayList<>();
    }
    this.warnings.add(warningsItem);
    return this;
  }

  /**
   * Get warnings
   * @return warnings
   */
  @Valid 
  @Schema(name = "warnings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("warnings")
  public List<@Valid BudgetWarning> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<@Valid BudgetWarning> warnings) {
    this.warnings = warnings;
  }

  public EscrowLockResponse kafkaEventId(@Nullable UUID kafkaEventId) {
    this.kafkaEventId = kafkaEventId;
    return this;
  }

  /**
   * Get kafkaEventId
   * @return kafkaEventId
   */
  @Valid 
  @Schema(name = "kafkaEventId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("kafkaEventId")
  public @Nullable UUID getKafkaEventId() {
    return kafkaEventId;
  }

  public void setKafkaEventId(@Nullable UUID kafkaEventId) {
    this.kafkaEventId = kafkaEventId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EscrowLockResponse escrowLockResponse = (EscrowLockResponse) o;
    return Objects.equals(this.orderId, escrowLockResponse.orderId) &&
        Objects.equals(this.escrowAmount, escrowLockResponse.escrowAmount) &&
        Objects.equals(this.status, escrowLockResponse.status) &&
        Objects.equals(this.insuranceTier, escrowLockResponse.insuranceTier) &&
        Objects.equals(this.lockExpiresAt, escrowLockResponse.lockExpiresAt) &&
        Objects.equals(this.warnings, escrowLockResponse.warnings) &&
        Objects.equals(this.kafkaEventId, escrowLockResponse.kafkaEventId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, escrowAmount, status, insuranceTier, lockExpiresAt, warnings, kafkaEventId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EscrowLockResponse {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    escrowAmount: ").append(toIndentedString(escrowAmount)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    insuranceTier: ").append(toIndentedString(insuranceTier)).append("\n");
    sb.append("    lockExpiresAt: ").append(toIndentedString(lockExpiresAt)).append("\n");
    sb.append("    warnings: ").append(toIndentedString(warnings)).append("\n");
    sb.append("    kafkaEventId: ").append(toIndentedString(kafkaEventId)).append("\n");
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

