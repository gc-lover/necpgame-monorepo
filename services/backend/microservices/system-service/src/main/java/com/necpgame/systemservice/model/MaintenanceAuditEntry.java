package com.necpgame.systemservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.systemservice.model.MaintenanceAuditEntryAttachmentsInner;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * MaintenanceAuditEntry
 */


public class MaintenanceAuditEntry {

  private UUID entryId;

  private UUID windowId;

  private @Nullable String actor;

  private @Nullable String role;

  private String action;

  private @Nullable String details;

  @Valid
  private List<@Valid MaintenanceAuditEntryAttachmentsInner> attachments = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  public MaintenanceAuditEntry() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MaintenanceAuditEntry(UUID entryId, UUID windowId, String action, OffsetDateTime timestamp) {
    this.entryId = entryId;
    this.windowId = windowId;
    this.action = action;
    this.timestamp = timestamp;
  }

  public MaintenanceAuditEntry entryId(UUID entryId) {
    this.entryId = entryId;
    return this;
  }

  /**
   * Get entryId
   * @return entryId
   */
  @NotNull @Valid 
  @Schema(name = "entryId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("entryId")
  public UUID getEntryId() {
    return entryId;
  }

  public void setEntryId(UUID entryId) {
    this.entryId = entryId;
  }

  public MaintenanceAuditEntry windowId(UUID windowId) {
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

  public MaintenanceAuditEntry actor(@Nullable String actor) {
    this.actor = actor;
    return this;
  }

  /**
   * Get actor
   * @return actor
   */
  
  @Schema(name = "actor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actor")
  public @Nullable String getActor() {
    return actor;
  }

  public void setActor(@Nullable String actor) {
    this.actor = actor;
  }

  public MaintenanceAuditEntry role(@Nullable String role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  
  @Schema(name = "role", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public @Nullable String getRole() {
    return role;
  }

  public void setRole(@Nullable String role) {
    this.role = role;
  }

  public MaintenanceAuditEntry action(String action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
   */
  @NotNull 
  @Schema(name = "action", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("action")
  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public MaintenanceAuditEntry details(@Nullable String details) {
    this.details = details;
    return this;
  }

  /**
   * Get details
   * @return details
   */
  
  @Schema(name = "details", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("details")
  public @Nullable String getDetails() {
    return details;
  }

  public void setDetails(@Nullable String details) {
    this.details = details;
  }

  public MaintenanceAuditEntry attachments(List<@Valid MaintenanceAuditEntryAttachmentsInner> attachments) {
    this.attachments = attachments;
    return this;
  }

  public MaintenanceAuditEntry addAttachmentsItem(MaintenanceAuditEntryAttachmentsInner attachmentsItem) {
    if (this.attachments == null) {
      this.attachments = new ArrayList<>();
    }
    this.attachments.add(attachmentsItem);
    return this;
  }

  /**
   * Get attachments
   * @return attachments
   */
  @Valid 
  @Schema(name = "attachments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attachments")
  public List<@Valid MaintenanceAuditEntryAttachmentsInner> getAttachments() {
    return attachments;
  }

  public void setAttachments(List<@Valid MaintenanceAuditEntryAttachmentsInner> attachments) {
    this.attachments = attachments;
  }

  public MaintenanceAuditEntry timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @NotNull @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timestamp")
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaintenanceAuditEntry maintenanceAuditEntry = (MaintenanceAuditEntry) o;
    return Objects.equals(this.entryId, maintenanceAuditEntry.entryId) &&
        Objects.equals(this.windowId, maintenanceAuditEntry.windowId) &&
        Objects.equals(this.actor, maintenanceAuditEntry.actor) &&
        Objects.equals(this.role, maintenanceAuditEntry.role) &&
        Objects.equals(this.action, maintenanceAuditEntry.action) &&
        Objects.equals(this.details, maintenanceAuditEntry.details) &&
        Objects.equals(this.attachments, maintenanceAuditEntry.attachments) &&
        Objects.equals(this.timestamp, maintenanceAuditEntry.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entryId, windowId, actor, role, action, details, attachments, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaintenanceAuditEntry {\n");
    sb.append("    entryId: ").append(toIndentedString(entryId)).append("\n");
    sb.append("    windowId: ").append(toIndentedString(windowId)).append("\n");
    sb.append("    actor: ").append(toIndentedString(actor)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    details: ").append(toIndentedString(details)).append("\n");
    sb.append("    attachments: ").append(toIndentedString(attachments)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
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

