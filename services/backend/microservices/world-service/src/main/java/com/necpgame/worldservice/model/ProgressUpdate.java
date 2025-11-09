package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.ProgressUpdateTelemetry;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * ProgressUpdate
 */


public class ProgressUpdate {

  private UUID playerId;

  private String stageId;

  private @Nullable String branchId;

  private @Nullable String objectiveId;

  private @Nullable BigDecimal progressValue;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    STARTED("STARTED"),
    
    IN_PROGRESS("IN_PROGRESS"),
    
    COMPLETED("COMPLETED"),
    
    FAILED("FAILED");

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

  private @Nullable StatusEnum status;

  @Valid
  private List<String> worldFlags = new ArrayList<>();

  private @Nullable ProgressUpdateTelemetry telemetry;

  public ProgressUpdate() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ProgressUpdate(UUID playerId, String stageId) {
    this.playerId = playerId;
    this.stageId = stageId;
  }

  public ProgressUpdate playerId(UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull @Valid 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(UUID playerId) {
    this.playerId = playerId;
  }

  public ProgressUpdate stageId(String stageId) {
    this.stageId = stageId;
    return this;
  }

  /**
   * Get stageId
   * @return stageId
   */
  @NotNull 
  @Schema(name = "stageId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("stageId")
  public String getStageId() {
    return stageId;
  }

  public void setStageId(String stageId) {
    this.stageId = stageId;
  }

  public ProgressUpdate branchId(@Nullable String branchId) {
    this.branchId = branchId;
    return this;
  }

  /**
   * Get branchId
   * @return branchId
   */
  
  @Schema(name = "branchId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("branchId")
  public @Nullable String getBranchId() {
    return branchId;
  }

  public void setBranchId(@Nullable String branchId) {
    this.branchId = branchId;
  }

  public ProgressUpdate objectiveId(@Nullable String objectiveId) {
    this.objectiveId = objectiveId;
    return this;
  }

  /**
   * Get objectiveId
   * @return objectiveId
   */
  
  @Schema(name = "objectiveId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("objectiveId")
  public @Nullable String getObjectiveId() {
    return objectiveId;
  }

  public void setObjectiveId(@Nullable String objectiveId) {
    this.objectiveId = objectiveId;
  }

  public ProgressUpdate progressValue(@Nullable BigDecimal progressValue) {
    this.progressValue = progressValue;
    return this;
  }

  /**
   * Get progressValue
   * @return progressValue
   */
  @Valid 
  @Schema(name = "progressValue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progressValue")
  public @Nullable BigDecimal getProgressValue() {
    return progressValue;
  }

  public void setProgressValue(@Nullable BigDecimal progressValue) {
    this.progressValue = progressValue;
  }

  public ProgressUpdate status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public ProgressUpdate worldFlags(List<String> worldFlags) {
    this.worldFlags = worldFlags;
    return this;
  }

  public ProgressUpdate addWorldFlagsItem(String worldFlagsItem) {
    if (this.worldFlags == null) {
      this.worldFlags = new ArrayList<>();
    }
    this.worldFlags.add(worldFlagsItem);
    return this;
  }

  /**
   * Get worldFlags
   * @return worldFlags
   */
  
  @Schema(name = "worldFlags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("worldFlags")
  public List<String> getWorldFlags() {
    return worldFlags;
  }

  public void setWorldFlags(List<String> worldFlags) {
    this.worldFlags = worldFlags;
  }

  public ProgressUpdate telemetry(@Nullable ProgressUpdateTelemetry telemetry) {
    this.telemetry = telemetry;
    return this;
  }

  /**
   * Get telemetry
   * @return telemetry
   */
  @Valid 
  @Schema(name = "telemetry", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("telemetry")
  public @Nullable ProgressUpdateTelemetry getTelemetry() {
    return telemetry;
  }

  public void setTelemetry(@Nullable ProgressUpdateTelemetry telemetry) {
    this.telemetry = telemetry;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProgressUpdate progressUpdate = (ProgressUpdate) o;
    return Objects.equals(this.playerId, progressUpdate.playerId) &&
        Objects.equals(this.stageId, progressUpdate.stageId) &&
        Objects.equals(this.branchId, progressUpdate.branchId) &&
        Objects.equals(this.objectiveId, progressUpdate.objectiveId) &&
        Objects.equals(this.progressValue, progressUpdate.progressValue) &&
        Objects.equals(this.status, progressUpdate.status) &&
        Objects.equals(this.worldFlags, progressUpdate.worldFlags) &&
        Objects.equals(this.telemetry, progressUpdate.telemetry);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, stageId, branchId, objectiveId, progressValue, status, worldFlags, telemetry);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProgressUpdate {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    stageId: ").append(toIndentedString(stageId)).append("\n");
    sb.append("    branchId: ").append(toIndentedString(branchId)).append("\n");
    sb.append("    objectiveId: ").append(toIndentedString(objectiveId)).append("\n");
    sb.append("    progressValue: ").append(toIndentedString(progressValue)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    worldFlags: ").append(toIndentedString(worldFlags)).append("\n");
    sb.append("    telemetry: ").append(toIndentedString(telemetry)).append("\n");
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

