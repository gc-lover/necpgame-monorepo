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
 * BudgetAnomalyEvent
 */


public class BudgetAnomalyEvent {

  private UUID orderId;

  /**
   * Gets or Sets anomalyType
   */
  public enum AnomalyTypeEnum {
    MARKET_SPIKE("market_spike"),
    
    MARKET_DROP("market_drop"),
    
    ESCROW_TIMEOUT("escrow_timeout"),
    
    VALIDATION_LOOP("validation_loop"),
    
    INSURANCE_REJECT("insurance_reject");

    private final String value;

    AnomalyTypeEnum(String value) {
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
    public static AnomalyTypeEnum fromValue(String value) {
      for (AnomalyTypeEnum b : AnomalyTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private AnomalyTypeEnum anomalyType;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime detectedAt;

  private @Nullable String details;

  public BudgetAnomalyEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BudgetAnomalyEvent(UUID orderId, AnomalyTypeEnum anomalyType, OffsetDateTime detectedAt) {
    this.orderId = orderId;
    this.anomalyType = anomalyType;
    this.detectedAt = detectedAt;
  }

  public BudgetAnomalyEvent orderId(UUID orderId) {
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

  public BudgetAnomalyEvent anomalyType(AnomalyTypeEnum anomalyType) {
    this.anomalyType = anomalyType;
    return this;
  }

  /**
   * Get anomalyType
   * @return anomalyType
   */
  @NotNull 
  @Schema(name = "anomalyType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("anomalyType")
  public AnomalyTypeEnum getAnomalyType() {
    return anomalyType;
  }

  public void setAnomalyType(AnomalyTypeEnum anomalyType) {
    this.anomalyType = anomalyType;
  }

  public BudgetAnomalyEvent detectedAt(OffsetDateTime detectedAt) {
    this.detectedAt = detectedAt;
    return this;
  }

  /**
   * Get detectedAt
   * @return detectedAt
   */
  @NotNull @Valid 
  @Schema(name = "detectedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("detectedAt")
  public OffsetDateTime getDetectedAt() {
    return detectedAt;
  }

  public void setDetectedAt(OffsetDateTime detectedAt) {
    this.detectedAt = detectedAt;
  }

  public BudgetAnomalyEvent details(@Nullable String details) {
    this.details = details;
    return this;
  }

  /**
   * Get details
   * @return details
   */
  @Size(max = 2048) 
  @Schema(name = "details", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("details")
  public @Nullable String getDetails() {
    return details;
  }

  public void setDetails(@Nullable String details) {
    this.details = details;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BudgetAnomalyEvent budgetAnomalyEvent = (BudgetAnomalyEvent) o;
    return Objects.equals(this.orderId, budgetAnomalyEvent.orderId) &&
        Objects.equals(this.anomalyType, budgetAnomalyEvent.anomalyType) &&
        Objects.equals(this.detectedAt, budgetAnomalyEvent.detectedAt) &&
        Objects.equals(this.details, budgetAnomalyEvent.details);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, anomalyType, detectedAt, details);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BudgetAnomalyEvent {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    anomalyType: ").append(toIndentedString(anomalyType)).append("\n");
    sb.append("    detectedAt: ").append(toIndentedString(detectedAt)).append("\n");
    sb.append("    details: ").append(toIndentedString(details)).append("\n");
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

