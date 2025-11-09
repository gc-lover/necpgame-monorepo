package com.necpgame.adminservice.model;

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
 * ResetHistoryEntry
 */


public class ResetHistoryEntry {

  private @Nullable UUID resetId;

  /**
   * Gets or Sets resetType
   */
  public enum ResetTypeEnum {
    DAILY("DAILY"),
    
    WEEKLY("WEEKLY"),
    
    MONTHLY("MONTHLY");

    private final String value;

    ResetTypeEnum(String value) {
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
    public static ResetTypeEnum fromValue(String value) {
      for (ResetTypeEnum b : ResetTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ResetTypeEnum resetType;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime executionTime;

  /**
   * Gets or Sets triggeredBy
   */
  public enum TriggeredByEnum {
    SCHEDULED("SCHEDULED"),
    
    MANUAL_ADMIN("MANUAL_ADMIN");

    private final String value;

    TriggeredByEnum(String value) {
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
    public static TriggeredByEnum fromValue(String value) {
      for (TriggeredByEnum b : TriggeredByEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TriggeredByEnum triggeredBy;

  private @Nullable Integer affectedPlayers;

  private @Nullable Boolean success;

  private @Nullable Integer executionDurationMs;

  public ResetHistoryEntry resetId(@Nullable UUID resetId) {
    this.resetId = resetId;
    return this;
  }

  /**
   * Get resetId
   * @return resetId
   */
  @Valid 
  @Schema(name = "reset_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reset_id")
  public @Nullable UUID getResetId() {
    return resetId;
  }

  public void setResetId(@Nullable UUID resetId) {
    this.resetId = resetId;
  }

  public ResetHistoryEntry resetType(@Nullable ResetTypeEnum resetType) {
    this.resetType = resetType;
    return this;
  }

  /**
   * Get resetType
   * @return resetType
   */
  
  @Schema(name = "reset_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reset_type")
  public @Nullable ResetTypeEnum getResetType() {
    return resetType;
  }

  public void setResetType(@Nullable ResetTypeEnum resetType) {
    this.resetType = resetType;
  }

  public ResetHistoryEntry executionTime(@Nullable OffsetDateTime executionTime) {
    this.executionTime = executionTime;
    return this;
  }

  /**
   * Get executionTime
   * @return executionTime
   */
  @Valid 
  @Schema(name = "execution_time", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("execution_time")
  public @Nullable OffsetDateTime getExecutionTime() {
    return executionTime;
  }

  public void setExecutionTime(@Nullable OffsetDateTime executionTime) {
    this.executionTime = executionTime;
  }

  public ResetHistoryEntry triggeredBy(@Nullable TriggeredByEnum triggeredBy) {
    this.triggeredBy = triggeredBy;
    return this;
  }

  /**
   * Get triggeredBy
   * @return triggeredBy
   */
  
  @Schema(name = "triggered_by", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("triggered_by")
  public @Nullable TriggeredByEnum getTriggeredBy() {
    return triggeredBy;
  }

  public void setTriggeredBy(@Nullable TriggeredByEnum triggeredBy) {
    this.triggeredBy = triggeredBy;
  }

  public ResetHistoryEntry affectedPlayers(@Nullable Integer affectedPlayers) {
    this.affectedPlayers = affectedPlayers;
    return this;
  }

  /**
   * Get affectedPlayers
   * @return affectedPlayers
   */
  
  @Schema(name = "affected_players", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affected_players")
  public @Nullable Integer getAffectedPlayers() {
    return affectedPlayers;
  }

  public void setAffectedPlayers(@Nullable Integer affectedPlayers) {
    this.affectedPlayers = affectedPlayers;
  }

  public ResetHistoryEntry success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public ResetHistoryEntry executionDurationMs(@Nullable Integer executionDurationMs) {
    this.executionDurationMs = executionDurationMs;
    return this;
  }

  /**
   * Get executionDurationMs
   * @return executionDurationMs
   */
  
  @Schema(name = "execution_duration_ms", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("execution_duration_ms")
  public @Nullable Integer getExecutionDurationMs() {
    return executionDurationMs;
  }

  public void setExecutionDurationMs(@Nullable Integer executionDurationMs) {
    this.executionDurationMs = executionDurationMs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResetHistoryEntry resetHistoryEntry = (ResetHistoryEntry) o;
    return Objects.equals(this.resetId, resetHistoryEntry.resetId) &&
        Objects.equals(this.resetType, resetHistoryEntry.resetType) &&
        Objects.equals(this.executionTime, resetHistoryEntry.executionTime) &&
        Objects.equals(this.triggeredBy, resetHistoryEntry.triggeredBy) &&
        Objects.equals(this.affectedPlayers, resetHistoryEntry.affectedPlayers) &&
        Objects.equals(this.success, resetHistoryEntry.success) &&
        Objects.equals(this.executionDurationMs, resetHistoryEntry.executionDurationMs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resetId, resetType, executionTime, triggeredBy, affectedPlayers, success, executionDurationMs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResetHistoryEntry {\n");
    sb.append("    resetId: ").append(toIndentedString(resetId)).append("\n");
    sb.append("    resetType: ").append(toIndentedString(resetType)).append("\n");
    sb.append("    executionTime: ").append(toIndentedString(executionTime)).append("\n");
    sb.append("    triggeredBy: ").append(toIndentedString(triggeredBy)).append("\n");
    sb.append("    affectedPlayers: ").append(toIndentedString(affectedPlayers)).append("\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    executionDurationMs: ").append(toIndentedString(executionDurationMs)).append("\n");
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

