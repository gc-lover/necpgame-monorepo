package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
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
 * CalculationBreakdownAuditTrailInner
 */

@JsonTypeName("CalculationBreakdown_auditTrail_inner")

public class CalculationBreakdownAuditTrailInner {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  private String action;

  private @Nullable Float valueBefore;

  private @Nullable Float valueAfter;

  public CalculationBreakdownAuditTrailInner() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CalculationBreakdownAuditTrailInner(OffsetDateTime timestamp, String action) {
    this.timestamp = timestamp;
    this.action = action;
  }

  public CalculationBreakdownAuditTrailInner timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @NotNull @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timestamp")
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public CalculationBreakdownAuditTrailInner action(String action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
   */
  @NotNull 
  @Schema(name = "action", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("action")
  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public CalculationBreakdownAuditTrailInner valueBefore(@Nullable Float valueBefore) {
    this.valueBefore = valueBefore;
    return this;
  }

  /**
   * Get valueBefore
   * @return valueBefore
   */
  
  @Schema(name = "valueBefore", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("valueBefore")
  public @Nullable Float getValueBefore() {
    return valueBefore;
  }

  public void setValueBefore(@Nullable Float valueBefore) {
    this.valueBefore = valueBefore;
  }

  public CalculationBreakdownAuditTrailInner valueAfter(@Nullable Float valueAfter) {
    this.valueAfter = valueAfter;
    return this;
  }

  /**
   * Get valueAfter
   * @return valueAfter
   */
  
  @Schema(name = "valueAfter", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("valueAfter")
  public @Nullable Float getValueAfter() {
    return valueAfter;
  }

  public void setValueAfter(@Nullable Float valueAfter) {
    this.valueAfter = valueAfter;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CalculationBreakdownAuditTrailInner calculationBreakdownAuditTrailInner = (CalculationBreakdownAuditTrailInner) o;
    return Objects.equals(this.timestamp, calculationBreakdownAuditTrailInner.timestamp) &&
        Objects.equals(this.action, calculationBreakdownAuditTrailInner.action) &&
        Objects.equals(this.valueBefore, calculationBreakdownAuditTrailInner.valueBefore) &&
        Objects.equals(this.valueAfter, calculationBreakdownAuditTrailInner.valueAfter);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, action, valueBefore, valueAfter);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CalculationBreakdownAuditTrailInner {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    valueBefore: ").append(toIndentedString(valueBefore)).append("\n");
    sb.append("    valueAfter: ").append(toIndentedString(valueAfter)).append("\n");
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

