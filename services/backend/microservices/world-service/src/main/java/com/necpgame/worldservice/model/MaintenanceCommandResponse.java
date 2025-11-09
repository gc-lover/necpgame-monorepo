package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * MaintenanceCommandResponse
 */


public class MaintenanceCommandResponse {

  private UUID commandId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACCEPTED("accepted"),
    
    REJECTED("rejected"),
    
    IN_PROGRESS("in_progress");

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

  private StatusEnum status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime issuedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime effectiveFrom;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  private @Nullable String issuedBy;

  private @Nullable String notes;

  public MaintenanceCommandResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MaintenanceCommandResponse(UUID commandId, StatusEnum status, OffsetDateTime issuedAt) {
    this.commandId = commandId;
    this.status = status;
    this.issuedAt = issuedAt;
  }

  public MaintenanceCommandResponse commandId(UUID commandId) {
    this.commandId = commandId;
    return this;
  }

  /**
   * Get commandId
   * @return commandId
   */
  @NotNull @Valid 
  @Schema(name = "commandId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("commandId")
  public UUID getCommandId() {
    return commandId;
  }

  public void setCommandId(UUID commandId) {
    this.commandId = commandId;
  }

  public MaintenanceCommandResponse status(StatusEnum status) {
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
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public MaintenanceCommandResponse issuedAt(OffsetDateTime issuedAt) {
    this.issuedAt = issuedAt;
    return this;
  }

  /**
   * Get issuedAt
   * @return issuedAt
   */
  @NotNull @Valid 
  @Schema(name = "issuedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("issuedAt")
  public OffsetDateTime getIssuedAt() {
    return issuedAt;
  }

  public void setIssuedAt(OffsetDateTime issuedAt) {
    this.issuedAt = issuedAt;
  }

  public MaintenanceCommandResponse effectiveFrom(@Nullable OffsetDateTime effectiveFrom) {
    this.effectiveFrom = effectiveFrom;
    return this;
  }

  /**
   * Get effectiveFrom
   * @return effectiveFrom
   */
  @Valid 
  @Schema(name = "effectiveFrom", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("effectiveFrom")
  public @Nullable OffsetDateTime getEffectiveFrom() {
    return effectiveFrom;
  }

  public void setEffectiveFrom(@Nullable OffsetDateTime effectiveFrom) {
    this.effectiveFrom = effectiveFrom;
  }

  public MaintenanceCommandResponse expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expiresAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expiresAt")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public MaintenanceCommandResponse issuedBy(@Nullable String issuedBy) {
    this.issuedBy = issuedBy;
    return this;
  }

  /**
   * Get issuedBy
   * @return issuedBy
   */
  
  @Schema(name = "issuedBy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("issuedBy")
  public @Nullable String getIssuedBy() {
    return issuedBy;
  }

  public void setIssuedBy(@Nullable String issuedBy) {
    this.issuedBy = issuedBy;
  }

  public MaintenanceCommandResponse notes(@Nullable String notes) {
    this.notes = notes;
    return this;
  }

  /**
   * Get notes
   * @return notes
   */
  
  @Schema(name = "notes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notes")
  public @Nullable String getNotes() {
    return notes;
  }

  public void setNotes(@Nullable String notes) {
    this.notes = notes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaintenanceCommandResponse maintenanceCommandResponse = (MaintenanceCommandResponse) o;
    return Objects.equals(this.commandId, maintenanceCommandResponse.commandId) &&
        Objects.equals(this.status, maintenanceCommandResponse.status) &&
        Objects.equals(this.issuedAt, maintenanceCommandResponse.issuedAt) &&
        Objects.equals(this.effectiveFrom, maintenanceCommandResponse.effectiveFrom) &&
        Objects.equals(this.expiresAt, maintenanceCommandResponse.expiresAt) &&
        Objects.equals(this.issuedBy, maintenanceCommandResponse.issuedBy) &&
        Objects.equals(this.notes, maintenanceCommandResponse.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(commandId, status, issuedAt, effectiveFrom, expiresAt, issuedBy, notes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaintenanceCommandResponse {\n");
    sb.append("    commandId: ").append(toIndentedString(commandId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    issuedAt: ").append(toIndentedString(issuedAt)).append("\n");
    sb.append("    effectiveFrom: ").append(toIndentedString(effectiveFrom)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    issuedBy: ").append(toIndentedString(issuedBy)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
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

