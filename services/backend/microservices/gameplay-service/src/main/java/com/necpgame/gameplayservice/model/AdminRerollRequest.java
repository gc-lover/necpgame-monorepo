package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AdminRerollRequest
 */


public class AdminRerollRequest {

  private String dropId;

  private String reason;

  private @Nullable String executorId;

  private @Nullable Boolean force;

  public AdminRerollRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AdminRerollRequest(String dropId, String reason) {
    this.dropId = dropId;
    this.reason = reason;
  }

  public AdminRerollRequest dropId(String dropId) {
    this.dropId = dropId;
    return this;
  }

  /**
   * Get dropId
   * @return dropId
   */
  @NotNull 
  @Schema(name = "dropId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("dropId")
  public String getDropId() {
    return dropId;
  }

  public void setDropId(String dropId) {
    this.dropId = dropId;
  }

  public AdminRerollRequest reason(String reason) {
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
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public AdminRerollRequest executorId(@Nullable String executorId) {
    this.executorId = executorId;
    return this;
  }

  /**
   * Get executorId
   * @return executorId
   */
  
  @Schema(name = "executorId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("executorId")
  public @Nullable String getExecutorId() {
    return executorId;
  }

  public void setExecutorId(@Nullable String executorId) {
    this.executorId = executorId;
  }

  public AdminRerollRequest force(@Nullable Boolean force) {
    this.force = force;
    return this;
  }

  /**
   * Get force
   * @return force
   */
  
  @Schema(name = "force", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("force")
  public @Nullable Boolean getForce() {
    return force;
  }

  public void setForce(@Nullable Boolean force) {
    this.force = force;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AdminRerollRequest adminRerollRequest = (AdminRerollRequest) o;
    return Objects.equals(this.dropId, adminRerollRequest.dropId) &&
        Objects.equals(this.reason, adminRerollRequest.reason) &&
        Objects.equals(this.executorId, adminRerollRequest.executorId) &&
        Objects.equals(this.force, adminRerollRequest.force);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dropId, reason, executorId, force);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AdminRerollRequest {\n");
    sb.append("    dropId: ").append(toIndentedString(dropId)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    executorId: ").append(toIndentedString(executorId)).append("\n");
    sb.append("    force: ").append(toIndentedString(force)).append("\n");
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

