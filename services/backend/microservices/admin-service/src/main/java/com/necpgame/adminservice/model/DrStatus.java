package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * DrStatus
 */


public class DrStatus {

  private @Nullable Boolean ready;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastBackup;

  private @Nullable String backupFrequency;

  private @Nullable Boolean failoverReady;

  private @Nullable Integer rpoMinutes;

  private @Nullable Integer rtoMinutes;

  public DrStatus ready(@Nullable Boolean ready) {
    this.ready = ready;
    return this;
  }

  /**
   * Get ready
   * @return ready
   */
  
  @Schema(name = "ready", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ready")
  public @Nullable Boolean getReady() {
    return ready;
  }

  public void setReady(@Nullable Boolean ready) {
    this.ready = ready;
  }

  public DrStatus lastBackup(@Nullable OffsetDateTime lastBackup) {
    this.lastBackup = lastBackup;
    return this;
  }

  /**
   * Get lastBackup
   * @return lastBackup
   */
  @Valid 
  @Schema(name = "last_backup", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("last_backup")
  public @Nullable OffsetDateTime getLastBackup() {
    return lastBackup;
  }

  public void setLastBackup(@Nullable OffsetDateTime lastBackup) {
    this.lastBackup = lastBackup;
  }

  public DrStatus backupFrequency(@Nullable String backupFrequency) {
    this.backupFrequency = backupFrequency;
    return this;
  }

  /**
   * Get backupFrequency
   * @return backupFrequency
   */
  
  @Schema(name = "backup_frequency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("backup_frequency")
  public @Nullable String getBackupFrequency() {
    return backupFrequency;
  }

  public void setBackupFrequency(@Nullable String backupFrequency) {
    this.backupFrequency = backupFrequency;
  }

  public DrStatus failoverReady(@Nullable Boolean failoverReady) {
    this.failoverReady = failoverReady;
    return this;
  }

  /**
   * Get failoverReady
   * @return failoverReady
   */
  
  @Schema(name = "failover_ready", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("failover_ready")
  public @Nullable Boolean getFailoverReady() {
    return failoverReady;
  }

  public void setFailoverReady(@Nullable Boolean failoverReady) {
    this.failoverReady = failoverReady;
  }

  public DrStatus rpoMinutes(@Nullable Integer rpoMinutes) {
    this.rpoMinutes = rpoMinutes;
    return this;
  }

  /**
   * Recovery Point Objective
   * @return rpoMinutes
   */
  
  @Schema(name = "rpo_minutes", description = "Recovery Point Objective", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rpo_minutes")
  public @Nullable Integer getRpoMinutes() {
    return rpoMinutes;
  }

  public void setRpoMinutes(@Nullable Integer rpoMinutes) {
    this.rpoMinutes = rpoMinutes;
  }

  public DrStatus rtoMinutes(@Nullable Integer rtoMinutes) {
    this.rtoMinutes = rtoMinutes;
    return this;
  }

  /**
   * Recovery Time Objective
   * @return rtoMinutes
   */
  
  @Schema(name = "rto_minutes", description = "Recovery Time Objective", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rto_minutes")
  public @Nullable Integer getRtoMinutes() {
    return rtoMinutes;
  }

  public void setRtoMinutes(@Nullable Integer rtoMinutes) {
    this.rtoMinutes = rtoMinutes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DrStatus drStatus = (DrStatus) o;
    return Objects.equals(this.ready, drStatus.ready) &&
        Objects.equals(this.lastBackup, drStatus.lastBackup) &&
        Objects.equals(this.backupFrequency, drStatus.backupFrequency) &&
        Objects.equals(this.failoverReady, drStatus.failoverReady) &&
        Objects.equals(this.rpoMinutes, drStatus.rpoMinutes) &&
        Objects.equals(this.rtoMinutes, drStatus.rtoMinutes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ready, lastBackup, backupFrequency, failoverReady, rpoMinutes, rtoMinutes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DrStatus {\n");
    sb.append("    ready: ").append(toIndentedString(ready)).append("\n");
    sb.append("    lastBackup: ").append(toIndentedString(lastBackup)).append("\n");
    sb.append("    backupFrequency: ").append(toIndentedString(backupFrequency)).append("\n");
    sb.append("    failoverReady: ").append(toIndentedString(failoverReady)).append("\n");
    sb.append("    rpoMinutes: ").append(toIndentedString(rpoMinutes)).append("\n");
    sb.append("    rtoMinutes: ").append(toIndentedString(rtoMinutes)).append("\n");
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

