package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.AdjustmentAction;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AutotuneResultRejectedActionsInner
 */

@JsonTypeName("AutotuneResult_rejectedActions_inner")

public class AutotuneResultRejectedActionsInner {

  private @Nullable AdjustmentAction action;

  private @Nullable String reason;

  public AutotuneResultRejectedActionsInner action(@Nullable AdjustmentAction action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
   */
  @Valid 
  @Schema(name = "action", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("action")
  public @Nullable AdjustmentAction getAction() {
    return action;
  }

  public void setAction(@Nullable AdjustmentAction action) {
    this.action = action;
  }

  public AutotuneResultRejectedActionsInner reason(@Nullable String reason) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AutotuneResultRejectedActionsInner autotuneResultRejectedActionsInner = (AutotuneResultRejectedActionsInner) o;
    return Objects.equals(this.action, autotuneResultRejectedActionsInner.action) &&
        Objects.equals(this.reason, autotuneResultRejectedActionsInner.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(action, reason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AutotuneResultRejectedActionsInner {\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
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

