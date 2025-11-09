package com.necpgame.systemservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * MaintenanceActionResponse
 */


public class MaintenanceActionResponse {

  private UUID windowId;

  private String status;

  private @Nullable String message;

  private @Nullable UUID auditEntryId;

  public MaintenanceActionResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MaintenanceActionResponse(UUID windowId, String status) {
    this.windowId = windowId;
    this.status = status;
  }

  public MaintenanceActionResponse windowId(UUID windowId) {
    this.windowId = windowId;
    return this;
  }

  /**
   * Get windowId
   * @return windowId
   */
  @NotNull @Valid 
  @Schema(name = "windowId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("windowId")
  public UUID getWindowId() {
    return windowId;
  }

  public void setWindowId(UUID windowId) {
    this.windowId = windowId;
  }

  public MaintenanceActionResponse status(String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public MaintenanceActionResponse message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  
  @Schema(name = "message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  public MaintenanceActionResponse auditEntryId(@Nullable UUID auditEntryId) {
    this.auditEntryId = auditEntryId;
    return this;
  }

  /**
   * Get auditEntryId
   * @return auditEntryId
   */
  @Valid 
  @Schema(name = "auditEntryId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("auditEntryId")
  public @Nullable UUID getAuditEntryId() {
    return auditEntryId;
  }

  public void setAuditEntryId(@Nullable UUID auditEntryId) {
    this.auditEntryId = auditEntryId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaintenanceActionResponse maintenanceActionResponse = (MaintenanceActionResponse) o;
    return Objects.equals(this.windowId, maintenanceActionResponse.windowId) &&
        Objects.equals(this.status, maintenanceActionResponse.status) &&
        Objects.equals(this.message, maintenanceActionResponse.message) &&
        Objects.equals(this.auditEntryId, maintenanceActionResponse.auditEntryId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(windowId, status, message, auditEntryId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaintenanceActionResponse {\n");
    sb.append("    windowId: ").append(toIndentedString(windowId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    auditEntryId: ").append(toIndentedString(auditEntryId)).append("\n");
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

