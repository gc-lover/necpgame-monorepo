package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * StateSnapshotRef
 */


public class StateSnapshotRef {

  private @Nullable UUID snapshotId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime takenAt;

  private @Nullable String reason;

  public StateSnapshotRef snapshotId(@Nullable UUID snapshotId) {
    this.snapshotId = snapshotId;
    return this;
  }

  /**
   * Get snapshotId
   * @return snapshotId
   */
  @Valid 
  @Schema(name = "snapshotId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("snapshotId")
  public @Nullable UUID getSnapshotId() {
    return snapshotId;
  }

  public void setSnapshotId(@Nullable UUID snapshotId) {
    this.snapshotId = snapshotId;
  }

  public StateSnapshotRef takenAt(@Nullable OffsetDateTime takenAt) {
    this.takenAt = takenAt;
    return this;
  }

  /**
   * Get takenAt
   * @return takenAt
   */
  @Valid 
  @Schema(name = "takenAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("takenAt")
  public @Nullable OffsetDateTime getTakenAt() {
    return takenAt;
  }

  public void setTakenAt(@Nullable OffsetDateTime takenAt) {
    this.takenAt = takenAt;
  }

  public StateSnapshotRef reason(@Nullable String reason) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StateSnapshotRef stateSnapshotRef = (StateSnapshotRef) o;
    return Objects.equals(this.snapshotId, stateSnapshotRef.snapshotId) &&
        Objects.equals(this.takenAt, stateSnapshotRef.takenAt) &&
        Objects.equals(this.reason, stateSnapshotRef.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(snapshotId, takenAt, reason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StateSnapshotRef {\n");
    sb.append("    snapshotId: ").append(toIndentedString(snapshotId)).append("\n");
    sb.append("    takenAt: ").append(toIndentedString(takenAt)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
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

