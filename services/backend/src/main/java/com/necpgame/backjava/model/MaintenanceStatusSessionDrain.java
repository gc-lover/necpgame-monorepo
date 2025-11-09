package com.necpgame.backjava.model;

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
 * MaintenanceStatusSessionDrain
 */

@JsonTypeName("MaintenanceStatus_sessionDrain")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class MaintenanceStatusSessionDrain {

  private @Nullable Integer activeSessions;

  private @Nullable Integer drainedSessions;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime estimatedCompletion;

  public MaintenanceStatusSessionDrain activeSessions(@Nullable Integer activeSessions) {
    this.activeSessions = activeSessions;
    return this;
  }

  /**
   * Get activeSessions
   * @return activeSessions
   */
  
  @Schema(name = "activeSessions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activeSessions")
  public @Nullable Integer getActiveSessions() {
    return activeSessions;
  }

  public void setActiveSessions(@Nullable Integer activeSessions) {
    this.activeSessions = activeSessions;
  }

  public MaintenanceStatusSessionDrain drainedSessions(@Nullable Integer drainedSessions) {
    this.drainedSessions = drainedSessions;
    return this;
  }

  /**
   * Get drainedSessions
   * @return drainedSessions
   */
  
  @Schema(name = "drainedSessions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("drainedSessions")
  public @Nullable Integer getDrainedSessions() {
    return drainedSessions;
  }

  public void setDrainedSessions(@Nullable Integer drainedSessions) {
    this.drainedSessions = drainedSessions;
  }

  public MaintenanceStatusSessionDrain estimatedCompletion(@Nullable OffsetDateTime estimatedCompletion) {
    this.estimatedCompletion = estimatedCompletion;
    return this;
  }

  /**
   * Get estimatedCompletion
   * @return estimatedCompletion
   */
  @Valid 
  @Schema(name = "estimatedCompletion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimatedCompletion")
  public @Nullable OffsetDateTime getEstimatedCompletion() {
    return estimatedCompletion;
  }

  public void setEstimatedCompletion(@Nullable OffsetDateTime estimatedCompletion) {
    this.estimatedCompletion = estimatedCompletion;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaintenanceStatusSessionDrain maintenanceStatusSessionDrain = (MaintenanceStatusSessionDrain) o;
    return Objects.equals(this.activeSessions, maintenanceStatusSessionDrain.activeSessions) &&
        Objects.equals(this.drainedSessions, maintenanceStatusSessionDrain.drainedSessions) &&
        Objects.equals(this.estimatedCompletion, maintenanceStatusSessionDrain.estimatedCompletion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(activeSessions, drainedSessions, estimatedCompletion);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaintenanceStatusSessionDrain {\n");
    sb.append("    activeSessions: ").append(toIndentedString(activeSessions)).append("\n");
    sb.append("    drainedSessions: ").append(toIndentedString(drainedSessions)).append("\n");
    sb.append("    estimatedCompletion: ").append(toIndentedString(estimatedCompletion)).append("\n");
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

