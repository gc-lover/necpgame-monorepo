package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * ErrorGroup
 */


public class ErrorGroup {

  private @Nullable String errorType;

  private @Nullable Integer count;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime firstSeen;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastSeen;

  private @Nullable Integer affectedUsers;

  /**
   * Gets or Sets severity
   */
  public enum SeverityEnum {
    LOW("low"),
    
    MEDIUM("medium"),
    
    HIGH("high"),
    
    CRITICAL("critical");

    private final String value;

    SeverityEnum(String value) {
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
    public static SeverityEnum fromValue(String value) {
      for (SeverityEnum b : SeverityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable SeverityEnum severity;

  public ErrorGroup errorType(@Nullable String errorType) {
    this.errorType = errorType;
    return this;
  }

  /**
   * Get errorType
   * @return errorType
   */
  
  @Schema(name = "error_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("error_type")
  public @Nullable String getErrorType() {
    return errorType;
  }

  public void setErrorType(@Nullable String errorType) {
    this.errorType = errorType;
  }

  public ErrorGroup count(@Nullable Integer count) {
    this.count = count;
    return this;
  }

  /**
   * Get count
   * @return count
   */
  
  @Schema(name = "count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("count")
  public @Nullable Integer getCount() {
    return count;
  }

  public void setCount(@Nullable Integer count) {
    this.count = count;
  }

  public ErrorGroup firstSeen(@Nullable OffsetDateTime firstSeen) {
    this.firstSeen = firstSeen;
    return this;
  }

  /**
   * Get firstSeen
   * @return firstSeen
   */
  @Valid 
  @Schema(name = "first_seen", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("first_seen")
  public @Nullable OffsetDateTime getFirstSeen() {
    return firstSeen;
  }

  public void setFirstSeen(@Nullable OffsetDateTime firstSeen) {
    this.firstSeen = firstSeen;
  }

  public ErrorGroup lastSeen(@Nullable OffsetDateTime lastSeen) {
    this.lastSeen = lastSeen;
    return this;
  }

  /**
   * Get lastSeen
   * @return lastSeen
   */
  @Valid 
  @Schema(name = "last_seen", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("last_seen")
  public @Nullable OffsetDateTime getLastSeen() {
    return lastSeen;
  }

  public void setLastSeen(@Nullable OffsetDateTime lastSeen) {
    this.lastSeen = lastSeen;
  }

  public ErrorGroup affectedUsers(@Nullable Integer affectedUsers) {
    this.affectedUsers = affectedUsers;
    return this;
  }

  /**
   * Get affectedUsers
   * @return affectedUsers
   */
  
  @Schema(name = "affected_users", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affected_users")
  public @Nullable Integer getAffectedUsers() {
    return affectedUsers;
  }

  public void setAffectedUsers(@Nullable Integer affectedUsers) {
    this.affectedUsers = affectedUsers;
  }

  public ErrorGroup severity(@Nullable SeverityEnum severity) {
    this.severity = severity;
    return this;
  }

  /**
   * Get severity
   * @return severity
   */
  
  @Schema(name = "severity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("severity")
  public @Nullable SeverityEnum getSeverity() {
    return severity;
  }

  public void setSeverity(@Nullable SeverityEnum severity) {
    this.severity = severity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ErrorGroup errorGroup = (ErrorGroup) o;
    return Objects.equals(this.errorType, errorGroup.errorType) &&
        Objects.equals(this.count, errorGroup.count) &&
        Objects.equals(this.firstSeen, errorGroup.firstSeen) &&
        Objects.equals(this.lastSeen, errorGroup.lastSeen) &&
        Objects.equals(this.affectedUsers, errorGroup.affectedUsers) &&
        Objects.equals(this.severity, errorGroup.severity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(errorType, count, firstSeen, lastSeen, affectedUsers, severity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ErrorGroup {\n");
    sb.append("    errorType: ").append(toIndentedString(errorType)).append("\n");
    sb.append("    count: ").append(toIndentedString(count)).append("\n");
    sb.append("    firstSeen: ").append(toIndentedString(firstSeen)).append("\n");
    sb.append("    lastSeen: ").append(toIndentedString(lastSeen)).append("\n");
    sb.append("    affectedUsers: ").append(toIndentedString(affectedUsers)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
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

