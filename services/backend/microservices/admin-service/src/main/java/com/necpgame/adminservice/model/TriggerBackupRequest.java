package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TriggerBackupRequest
 */

@JsonTypeName("triggerBackup_request")

public class TriggerBackupRequest {

  /**
   * Gets or Sets backupType
   */
  public enum BackupTypeEnum {
    FULL("full"),
    
    INCREMENTAL("incremental");

    private final String value;

    BackupTypeEnum(String value) {
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
    public static BackupTypeEnum fromValue(String value) {
      for (BackupTypeEnum b : BackupTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private BackupTypeEnum backupType = BackupTypeEnum.INCREMENTAL;

  public TriggerBackupRequest backupType(BackupTypeEnum backupType) {
    this.backupType = backupType;
    return this;
  }

  /**
   * Get backupType
   * @return backupType
   */
  
  @Schema(name = "backup_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("backup_type")
  public BackupTypeEnum getBackupType() {
    return backupType;
  }

  public void setBackupType(BackupTypeEnum backupType) {
    this.backupType = backupType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TriggerBackupRequest triggerBackupRequest = (TriggerBackupRequest) o;
    return Objects.equals(this.backupType, triggerBackupRequest.backupType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(backupType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TriggerBackupRequest {\n");
    sb.append("    backupType: ").append(toIndentedString(backupType)).append("\n");
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

