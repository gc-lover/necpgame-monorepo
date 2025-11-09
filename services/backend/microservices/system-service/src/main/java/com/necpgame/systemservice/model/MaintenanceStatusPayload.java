package com.necpgame.systemservice.model;

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
 * MaintenanceStatusPayload
 */


public class MaintenanceStatusPayload {

  private String status;

  private @Nullable Float progressPercent;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  private @Nullable String message;

  private @Nullable Boolean _public;

  public MaintenanceStatusPayload() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MaintenanceStatusPayload(String status, OffsetDateTime timestamp) {
    this.status = status;
    this.timestamp = timestamp;
  }

  public MaintenanceStatusPayload status(String status) {
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

  public MaintenanceStatusPayload progressPercent(@Nullable Float progressPercent) {
    this.progressPercent = progressPercent;
    return this;
  }

  /**
   * Get progressPercent
   * @return progressPercent
   */
  
  @Schema(name = "progressPercent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progressPercent")
  public @Nullable Float getProgressPercent() {
    return progressPercent;
  }

  public void setProgressPercent(@Nullable Float progressPercent) {
    this.progressPercent = progressPercent;
  }

  public MaintenanceStatusPayload timestamp(OffsetDateTime timestamp) {
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

  public MaintenanceStatusPayload message(@Nullable String message) {
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

  public MaintenanceStatusPayload _public(@Nullable Boolean _public) {
    this._public = _public;
    return this;
  }

  /**
   * Get _public
   * @return _public
   */
  
  @Schema(name = "public", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("public")
  public @Nullable Boolean getPublic() {
    return _public;
  }

  public void setPublic(@Nullable Boolean _public) {
    this._public = _public;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaintenanceStatusPayload maintenanceStatusPayload = (MaintenanceStatusPayload) o;
    return Objects.equals(this.status, maintenanceStatusPayload.status) &&
        Objects.equals(this.progressPercent, maintenanceStatusPayload.progressPercent) &&
        Objects.equals(this.timestamp, maintenanceStatusPayload.timestamp) &&
        Objects.equals(this.message, maintenanceStatusPayload.message) &&
        Objects.equals(this._public, maintenanceStatusPayload._public);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, progressPercent, timestamp, message, _public);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaintenanceStatusPayload {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    progressPercent: ").append(toIndentedString(progressPercent)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    _public: ").append(toIndentedString(_public)).append("\n");
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

