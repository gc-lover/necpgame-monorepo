package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * RangeExpansion
 */


public class RangeExpansion {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  private Integer newRange;

  /**
   * Gets or Sets reason
   */
  public enum ReasonEnum {
    TIMEOUT("TIMEOUT"),
    
    EVENT("EVENT"),
    
    ADMIN("ADMIN"),
    
    AUTO("AUTO");

    private final String value;

    ReasonEnum(String value) {
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
    public static ReasonEnum fromValue(String value) {
      for (ReasonEnum b : ReasonEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ReasonEnum reason;

  private @Nullable Integer latencyCapMs;

  public RangeExpansion() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RangeExpansion(OffsetDateTime timestamp, Integer newRange, ReasonEnum reason) {
    this.timestamp = timestamp;
    this.newRange = newRange;
    this.reason = reason;
  }

  public RangeExpansion timestamp(OffsetDateTime timestamp) {
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

  public RangeExpansion newRange(Integer newRange) {
    this.newRange = newRange;
    return this;
  }

  /**
   * Get newRange
   * minimum: 0
   * @return newRange
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "newRange", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("newRange")
  public Integer getNewRange() {
    return newRange;
  }

  public void setNewRange(Integer newRange) {
    this.newRange = newRange;
  }

  public RangeExpansion reason(ReasonEnum reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  @NotNull 
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reason")
  public ReasonEnum getReason() {
    return reason;
  }

  public void setReason(ReasonEnum reason) {
    this.reason = reason;
  }

  public RangeExpansion latencyCapMs(@Nullable Integer latencyCapMs) {
    this.latencyCapMs = latencyCapMs;
    return this;
  }

  /**
   * Get latencyCapMs
   * minimum: 0
   * @return latencyCapMs
   */
  @Min(value = 0) 
  @Schema(name = "latencyCapMs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("latencyCapMs")
  public @Nullable Integer getLatencyCapMs() {
    return latencyCapMs;
  }

  public void setLatencyCapMs(@Nullable Integer latencyCapMs) {
    this.latencyCapMs = latencyCapMs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RangeExpansion rangeExpansion = (RangeExpansion) o;
    return Objects.equals(this.timestamp, rangeExpansion.timestamp) &&
        Objects.equals(this.newRange, rangeExpansion.newRange) &&
        Objects.equals(this.reason, rangeExpansion.reason) &&
        Objects.equals(this.latencyCapMs, rangeExpansion.latencyCapMs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, newRange, reason, latencyCapMs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RangeExpansion {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    newRange: ").append(toIndentedString(newRange)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    latencyCapMs: ").append(toIndentedString(latencyCapMs)).append("\n");
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

