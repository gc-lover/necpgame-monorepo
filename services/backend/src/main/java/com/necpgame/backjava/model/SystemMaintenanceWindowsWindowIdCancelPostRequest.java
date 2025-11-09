package com.necpgame.backjava.model;

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
 * SystemMaintenanceWindowsWindowIdCancelPostRequest
 */

@JsonTypeName("_system_maintenance_windows__windowId__cancel_post_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class SystemMaintenanceWindowsWindowIdCancelPostRequest {

  private String reason;

  private Boolean notifyPlayers = true;

  public SystemMaintenanceWindowsWindowIdCancelPostRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SystemMaintenanceWindowsWindowIdCancelPostRequest(String reason) {
    this.reason = reason;
  }

  public SystemMaintenanceWindowsWindowIdCancelPostRequest reason(String reason) {
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

  public SystemMaintenanceWindowsWindowIdCancelPostRequest notifyPlayers(Boolean notifyPlayers) {
    this.notifyPlayers = notifyPlayers;
    return this;
  }

  /**
   * Get notifyPlayers
   * @return notifyPlayers
   */
  
  @Schema(name = "notifyPlayers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notifyPlayers")
  public Boolean getNotifyPlayers() {
    return notifyPlayers;
  }

  public void setNotifyPlayers(Boolean notifyPlayers) {
    this.notifyPlayers = notifyPlayers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SystemMaintenanceWindowsWindowIdCancelPostRequest systemMaintenanceWindowsWindowIdCancelPostRequest = (SystemMaintenanceWindowsWindowIdCancelPostRequest) o;
    return Objects.equals(this.reason, systemMaintenanceWindowsWindowIdCancelPostRequest.reason) &&
        Objects.equals(this.notifyPlayers, systemMaintenanceWindowsWindowIdCancelPostRequest.notifyPlayers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reason, notifyPlayers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SystemMaintenanceWindowsWindowIdCancelPostRequest {\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    notifyPlayers: ").append(toIndentedString(notifyPlayers)).append("\n");
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

