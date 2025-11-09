package com.necpgame.adminservice.model;

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
 * RestoreFromBackupRequest
 */

@JsonTypeName("restoreFromBackup_request")

public class RestoreFromBackupRequest {

  private String backupId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime restorePoint;

  public RestoreFromBackupRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RestoreFromBackupRequest(String backupId) {
    this.backupId = backupId;
  }

  public RestoreFromBackupRequest backupId(String backupId) {
    this.backupId = backupId;
    return this;
  }

  /**
   * Get backupId
   * @return backupId
   */
  @NotNull 
  @Schema(name = "backup_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("backup_id")
  public String getBackupId() {
    return backupId;
  }

  public void setBackupId(String backupId) {
    this.backupId = backupId;
  }

  public RestoreFromBackupRequest restorePoint(@Nullable OffsetDateTime restorePoint) {
    this.restorePoint = restorePoint;
    return this;
  }

  /**
   * Get restorePoint
   * @return restorePoint
   */
  @Valid 
  @Schema(name = "restore_point", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("restore_point")
  public @Nullable OffsetDateTime getRestorePoint() {
    return restorePoint;
  }

  public void setRestorePoint(@Nullable OffsetDateTime restorePoint) {
    this.restorePoint = restorePoint;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RestoreFromBackupRequest restoreFromBackupRequest = (RestoreFromBackupRequest) o;
    return Objects.equals(this.backupId, restoreFromBackupRequest.backupId) &&
        Objects.equals(this.restorePoint, restoreFromBackupRequest.restorePoint);
  }

  @Override
  public int hashCode() {
    return Objects.hash(backupId, restorePoint);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RestoreFromBackupRequest {\n");
    sb.append("    backupId: ").append(toIndentedString(backupId)).append("\n");
    sb.append("    restorePoint: ").append(toIndentedString(restorePoint)).append("\n");
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

