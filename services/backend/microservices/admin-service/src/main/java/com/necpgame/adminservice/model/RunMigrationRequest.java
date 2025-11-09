package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RunMigrationRequest
 */

@JsonTypeName("runMigration_request")

public class RunMigrationRequest {

  private String migrationId;

  private Boolean dryRun = true;

  public RunMigrationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RunMigrationRequest(String migrationId) {
    this.migrationId = migrationId;
  }

  public RunMigrationRequest migrationId(String migrationId) {
    this.migrationId = migrationId;
    return this;
  }

  /**
   * Get migrationId
   * @return migrationId
   */
  @NotNull 
  @Schema(name = "migration_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("migration_id")
  public String getMigrationId() {
    return migrationId;
  }

  public void setMigrationId(String migrationId) {
    this.migrationId = migrationId;
  }

  public RunMigrationRequest dryRun(Boolean dryRun) {
    this.dryRun = dryRun;
    return this;
  }

  /**
   * Get dryRun
   * @return dryRun
   */
  
  @Schema(name = "dry_run", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dry_run")
  public Boolean getDryRun() {
    return dryRun;
  }

  public void setDryRun(Boolean dryRun) {
    this.dryRun = dryRun;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RunMigrationRequest runMigrationRequest = (RunMigrationRequest) o;
    return Objects.equals(this.migrationId, runMigrationRequest.migrationId) &&
        Objects.equals(this.dryRun, runMigrationRequest.dryRun);
  }

  @Override
  public int hashCode() {
    return Objects.hash(migrationId, dryRun);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RunMigrationRequest {\n");
    sb.append("    migrationId: ").append(toIndentedString(migrationId)).append("\n");
    sb.append("    dryRun: ").append(toIndentedString(dryRun)).append("\n");
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

