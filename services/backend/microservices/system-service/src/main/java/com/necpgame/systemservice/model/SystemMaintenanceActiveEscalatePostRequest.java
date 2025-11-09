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
 * SystemMaintenanceActiveEscalatePostRequest
 */

@JsonTypeName("_system_maintenance_active_escalate_post_request")

public class SystemMaintenanceActiveEscalatePostRequest {

  private String reason;

  private @Nullable String escalationLevel;

  @Valid
  private List<String> actions = new ArrayList<>();

  private @Nullable SystemMaintenanceWindowsWindowIdRollbackPostRequestNotify notify;

  public SystemMaintenanceActiveEscalatePostRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SystemMaintenanceActiveEscalatePostRequest(String reason) {
    this.reason = reason;
  }

  public SystemMaintenanceActiveEscalatePostRequest reason(String reason) {
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

  public SystemMaintenanceActiveEscalatePostRequest escalationLevel(@Nullable String escalationLevel) {
    this.escalationLevel = escalationLevel;
    return this;
  }

  /**
   * Get escalationLevel
   * @return escalationLevel
   */
  
  @Schema(name = "escalationLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("escalationLevel")
  public @Nullable String getEscalationLevel() {
    return escalationLevel;
  }

  public void setEscalationLevel(@Nullable String escalationLevel) {
    this.escalationLevel = escalationLevel;
  }

  public SystemMaintenanceActiveEscalatePostRequest actions(List<String> actions) {
    this.actions = actions;
    return this;
  }

  public SystemMaintenanceActiveEscalatePostRequest addActionsItem(String actionsItem) {
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

  public SystemMaintenanceActiveEscalatePostRequest notify(@Nullable SystemMaintenanceWindowsWindowIdRollbackPostRequestNotify notify) {
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
    SystemMaintenanceActiveEscalatePostRequest systemMaintenanceActiveEscalatePostRequest = (SystemMaintenanceActiveEscalatePostRequest) o;
    return Objects.equals(this.reason, systemMaintenanceActiveEscalatePostRequest.reason) &&
        Objects.equals(this.escalationLevel, systemMaintenanceActiveEscalatePostRequest.escalationLevel) &&
        Objects.equals(this.actions, systemMaintenanceActiveEscalatePostRequest.actions) &&
        Objects.equals(this.notify, systemMaintenanceActiveEscalatePostRequest.notify);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reason, escalationLevel, actions, notify);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SystemMaintenanceActiveEscalatePostRequest {\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    escalationLevel: ").append(toIndentedString(escalationLevel)).append("\n");
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

