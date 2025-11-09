package com.necpgame.socialservice.model;

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
 * LockLobbyRequest
 */


public class LockLobbyRequest {

  private Boolean locked;

  private @Nullable String reason;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime autoCloseAt;

  public LockLobbyRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LockLobbyRequest(Boolean locked) {
    this.locked = locked;
  }

  public LockLobbyRequest locked(Boolean locked) {
    this.locked = locked;
    return this;
  }

  /**
   * Get locked
   * @return locked
   */
  @NotNull 
  @Schema(name = "locked", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("locked")
  public Boolean getLocked() {
    return locked;
  }

  public void setLocked(Boolean locked) {
    this.locked = locked;
  }

  public LockLobbyRequest reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  public LockLobbyRequest autoCloseAt(@Nullable OffsetDateTime autoCloseAt) {
    this.autoCloseAt = autoCloseAt;
    return this;
  }

  /**
   * Get autoCloseAt
   * @return autoCloseAt
   */
  @Valid 
  @Schema(name = "autoCloseAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("autoCloseAt")
  public @Nullable OffsetDateTime getAutoCloseAt() {
    return autoCloseAt;
  }

  public void setAutoCloseAt(@Nullable OffsetDateTime autoCloseAt) {
    this.autoCloseAt = autoCloseAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LockLobbyRequest lockLobbyRequest = (LockLobbyRequest) o;
    return Objects.equals(this.locked, lockLobbyRequest.locked) &&
        Objects.equals(this.reason, lockLobbyRequest.reason) &&
        Objects.equals(this.autoCloseAt, lockLobbyRequest.autoCloseAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(locked, reason, autoCloseAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LockLobbyRequest {\n");
    sb.append("    locked: ").append(toIndentedString(locked)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    autoCloseAt: ").append(toIndentedString(autoCloseAt)).append("\n");
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

