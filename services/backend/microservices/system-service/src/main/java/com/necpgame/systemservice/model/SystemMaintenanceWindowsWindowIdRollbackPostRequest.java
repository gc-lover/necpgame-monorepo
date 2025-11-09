package com.necpgame.systemservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.systemservice.model.SystemMaintenanceWindowsWindowIdRollbackPostRequestNotify;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SystemMaintenanceWindowsWindowIdRollbackPostRequest
 */

@JsonTypeName("_system_maintenance_windows__windowId__rollback_post_request")

public class SystemMaintenanceWindowsWindowIdRollbackPostRequest {

  private String reason;

  @Valid
  private List<String> actions = new ArrayList<>();

  private @Nullable SystemMaintenanceWindowsWindowIdRollbackPostRequestNotify notify;

  public SystemMaintenanceWindowsWindowIdRollbackPostRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SystemMaintenanceWindowsWindowIdRollbackPostRequest(String reason) {
    this.reason = reason;
  }

  public SystemMaintenanceWindowsWindowIdRollbackPostRequest reason(String reason) {
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

  public SystemMaintenanceWindowsWindowIdRollbackPostRequest actions(List<String> actions) {
    this.actions = actions;
    return this;
  }

  public SystemMaintenanceWindowsWindowIdRollbackPostRequest addActionsItem(String actionsItem) {
    if (this.actions == null) {
      this.actions = new ArrayList<>();
    }
    this.actions.add(actionsItem);
    return this;
  }

  /**
   * Get actions
   * @return actions
   */
  
  @Schema(name = "actions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actions")
  public List<String> getActions() {
    return actions;
  }

  public void setActions(List<String> actions) {
    this.actions = actions;
  }

  public SystemMaintenanceWindowsWindowIdRollbackPostRequest notify(@Nullable SystemMaintenanceWindowsWindowIdRollbackPostRequestNotify notify) {
    this.notify = notify;
    return this;
  }

  /**
   * Get notify
   * @return notify
   */
  @Valid 
  @Schema(name = "notify", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notify")
  public @Nullable SystemMaintenanceWindowsWindowIdRollbackPostRequestNotify getNotify() {
    return notify;
  }

  public void setNotify(@Nullable SystemMaintenanceWindowsWindowIdRollbackPostRequestNotify notify) {
    this.notify = notify;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SystemMaintenanceWindowsWindowIdRollbackPostRequest systemMaintenanceWindowsWindowIdRollbackPostRequest = (SystemMaintenanceWindowsWindowIdRollbackPostRequest) o;
    return Objects.equals(this.reason, systemMaintenanceWindowsWindowIdRollbackPostRequest.reason) &&
        Objects.equals(this.actions, systemMaintenanceWindowsWindowIdRollbackPostRequest.actions) &&
        Objects.equals(this.notify, systemMaintenanceWindowsWindowIdRollbackPostRequest.notify);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reason, actions, notify);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SystemMaintenanceWindowsWindowIdRollbackPostRequest {\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    actions: ").append(toIndentedString(actions)).append("\n");
    sb.append("    notify: ").append(toIndentedString(notify)).append("\n");
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

