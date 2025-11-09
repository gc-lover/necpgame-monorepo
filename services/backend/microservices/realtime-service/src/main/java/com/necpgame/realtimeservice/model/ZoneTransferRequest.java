package com.necpgame.realtimeservice.model;

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
 * ZoneTransferRequest
 */


public class ZoneTransferRequest {

  private String targetInstanceId;

  /**
   * Gets or Sets drainStrategy
   */
  public enum DrainStrategyEnum {
    INSTANT("instant"),
    
    GRADUAL("gradual");

    private final String value;

    DrainStrategyEnum(String value) {
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
    public static DrainStrategyEnum fromValue(String value) {
      for (DrainStrategyEnum b : DrainStrategyEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private DrainStrategyEnum drainStrategy;

  /**
   * Gets or Sets priority
   */
  public enum PriorityEnum {
    LOW("low"),
    
    NORMAL("normal"),
    
    HIGH("high"),
    
    CRITICAL("critical");

    private final String value;

    PriorityEnum(String value) {
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
    public static PriorityEnum fromValue(String value) {
      for (PriorityEnum b : PriorityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable PriorityEnum priority;

  private @Nullable String reason;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime scheduledFor;

  public ZoneTransferRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ZoneTransferRequest(String targetInstanceId, DrainStrategyEnum drainStrategy) {
    this.targetInstanceId = targetInstanceId;
    this.drainStrategy = drainStrategy;
  }

  public ZoneTransferRequest targetInstanceId(String targetInstanceId) {
    this.targetInstanceId = targetInstanceId;
    return this;
  }

  /**
   * Get targetInstanceId
   * @return targetInstanceId
   */
  @NotNull 
  @Schema(name = "targetInstanceId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("targetInstanceId")
  public String getTargetInstanceId() {
    return targetInstanceId;
  }

  public void setTargetInstanceId(String targetInstanceId) {
    this.targetInstanceId = targetInstanceId;
  }

  public ZoneTransferRequest drainStrategy(DrainStrategyEnum drainStrategy) {
    this.drainStrategy = drainStrategy;
    return this;
  }

  /**
   * Get drainStrategy
   * @return drainStrategy
   */
  @NotNull 
  @Schema(name = "drainStrategy", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("drainStrategy")
  public DrainStrategyEnum getDrainStrategy() {
    return drainStrategy;
  }

  public void setDrainStrategy(DrainStrategyEnum drainStrategy) {
    this.drainStrategy = drainStrategy;
  }

  public ZoneTransferRequest priority(@Nullable PriorityEnum priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
   */
  
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priority")
  public @Nullable PriorityEnum getPriority() {
    return priority;
  }

  public void setPriority(@Nullable PriorityEnum priority) {
    this.priority = priority;
  }

  public ZoneTransferRequest reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  public ZoneTransferRequest scheduledFor(@Nullable OffsetDateTime scheduledFor) {
    this.scheduledFor = scheduledFor;
    return this;
  }

  /**
   * Get scheduledFor
   * @return scheduledFor
   */
  @Valid 
  @Schema(name = "scheduledFor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scheduledFor")
  public @Nullable OffsetDateTime getScheduledFor() {
    return scheduledFor;
  }

  public void setScheduledFor(@Nullable OffsetDateTime scheduledFor) {
    this.scheduledFor = scheduledFor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ZoneTransferRequest zoneTransferRequest = (ZoneTransferRequest) o;
    return Objects.equals(this.targetInstanceId, zoneTransferRequest.targetInstanceId) &&
        Objects.equals(this.drainStrategy, zoneTransferRequest.drainStrategy) &&
        Objects.equals(this.priority, zoneTransferRequest.priority) &&
        Objects.equals(this.reason, zoneTransferRequest.reason) &&
        Objects.equals(this.scheduledFor, zoneTransferRequest.scheduledFor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(targetInstanceId, drainStrategy, priority, reason, scheduledFor);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ZoneTransferRequest {\n");
    sb.append("    targetInstanceId: ").append(toIndentedString(targetInstanceId)).append("\n");
    sb.append("    drainStrategy: ").append(toIndentedString(drainStrategy)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    scheduledFor: ").append(toIndentedString(scheduledFor)).append("\n");
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

