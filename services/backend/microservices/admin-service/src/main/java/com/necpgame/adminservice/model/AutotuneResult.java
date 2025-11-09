package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.AdjustmentAction;
import com.necpgame.adminservice.model.AutotuneResultRejectedActionsInner;
import com.necpgame.adminservice.model.AutotuneResultScheduledRollbacksInner;
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
 * AutotuneResult
 */


public class AutotuneResult {

  private @Nullable String actionBatchId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACCEPTED("accepted"),
    
    REJECTED("rejected"),
    
    PARTIALLY_APPLIED("partially_applied");

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

  private @Nullable StatusEnum status;

  @Valid
  private List<@Valid AdjustmentAction> appliedActions = new ArrayList<>();

  @Valid
  private List<@Valid AutotuneResultRejectedActionsInner> rejectedActions = new ArrayList<>();

  @Valid
  private List<@Valid AutotuneResultScheduledRollbacksInner> scheduledRollbacks = new ArrayList<>();

  public AutotuneResult actionBatchId(@Nullable String actionBatchId) {
    this.actionBatchId = actionBatchId;
    return this;
  }

  /**
   * Get actionBatchId
   * @return actionBatchId
   */
  
  @Schema(name = "actionBatchId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actionBatchId")
  public @Nullable String getActionBatchId() {
    return actionBatchId;
  }

  public void setActionBatchId(@Nullable String actionBatchId) {
    this.actionBatchId = actionBatchId;
  }

  public AutotuneResult status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public AutotuneResult appliedActions(List<@Valid AdjustmentAction> appliedActions) {
    this.appliedActions = appliedActions;
    return this;
  }

  public AutotuneResult addAppliedActionsItem(AdjustmentAction appliedActionsItem) {
    if (this.appliedActions == null) {
      this.appliedActions = new ArrayList<>();
    }
    this.appliedActions.add(appliedActionsItem);
    return this;
  }

  /**
   * Get appliedActions
   * @return appliedActions
   */
  @Valid 
  @Schema(name = "appliedActions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("appliedActions")
  public List<@Valid AdjustmentAction> getAppliedActions() {
    return appliedActions;
  }

  public void setAppliedActions(List<@Valid AdjustmentAction> appliedActions) {
    this.appliedActions = appliedActions;
  }

  public AutotuneResult rejectedActions(List<@Valid AutotuneResultRejectedActionsInner> rejectedActions) {
    this.rejectedActions = rejectedActions;
    return this;
  }

  public AutotuneResult addRejectedActionsItem(AutotuneResultRejectedActionsInner rejectedActionsItem) {
    if (this.rejectedActions == null) {
      this.rejectedActions = new ArrayList<>();
    }
    this.rejectedActions.add(rejectedActionsItem);
    return this;
  }

  /**
   * Get rejectedActions
   * @return rejectedActions
   */
  @Valid 
  @Schema(name = "rejectedActions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rejectedActions")
  public List<@Valid AutotuneResultRejectedActionsInner> getRejectedActions() {
    return rejectedActions;
  }

  public void setRejectedActions(List<@Valid AutotuneResultRejectedActionsInner> rejectedActions) {
    this.rejectedActions = rejectedActions;
  }

  public AutotuneResult scheduledRollbacks(List<@Valid AutotuneResultScheduledRollbacksInner> scheduledRollbacks) {
    this.scheduledRollbacks = scheduledRollbacks;
    return this;
  }

  public AutotuneResult addScheduledRollbacksItem(AutotuneResultScheduledRollbacksInner scheduledRollbacksItem) {
    if (this.scheduledRollbacks == null) {
      this.scheduledRollbacks = new ArrayList<>();
    }
    this.scheduledRollbacks.add(scheduledRollbacksItem);
    return this;
  }

  /**
   * Get scheduledRollbacks
   * @return scheduledRollbacks
   */
  @Valid 
  @Schema(name = "scheduledRollbacks", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scheduledRollbacks")
  public List<@Valid AutotuneResultScheduledRollbacksInner> getScheduledRollbacks() {
    return scheduledRollbacks;
  }

  public void setScheduledRollbacks(List<@Valid AutotuneResultScheduledRollbacksInner> scheduledRollbacks) {
    this.scheduledRollbacks = scheduledRollbacks;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AutotuneResult autotuneResult = (AutotuneResult) o;
    return Objects.equals(this.actionBatchId, autotuneResult.actionBatchId) &&
        Objects.equals(this.status, autotuneResult.status) &&
        Objects.equals(this.appliedActions, autotuneResult.appliedActions) &&
        Objects.equals(this.rejectedActions, autotuneResult.rejectedActions) &&
        Objects.equals(this.scheduledRollbacks, autotuneResult.scheduledRollbacks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(actionBatchId, status, appliedActions, rejectedActions, scheduledRollbacks);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AutotuneResult {\n");
    sb.append("    actionBatchId: ").append(toIndentedString(actionBatchId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    appliedActions: ").append(toIndentedString(appliedActions)).append("\n");
    sb.append("    rejectedActions: ").append(toIndentedString(rejectedActions)).append("\n");
    sb.append("    scheduledRollbacks: ").append(toIndentedString(scheduledRollbacks)).append("\n");
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

