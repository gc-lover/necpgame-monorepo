package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * AutotuneResultScheduledRollbacksInner
 */

@JsonTypeName("AutotuneResult_scheduledRollbacks_inner")

public class AutotuneResultScheduledRollbacksInner {

  private @Nullable String actionId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime rollbackAt;

  public AutotuneResultScheduledRollbacksInner actionId(@Nullable String actionId) {
    this.actionId = actionId;
    return this;
  }

  /**
   * Get actionId
   * @return actionId
   */
  
  @Schema(name = "actionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actionId")
  public @Nullable String getActionId() {
    return actionId;
  }

  public void setActionId(@Nullable String actionId) {
    this.actionId = actionId;
  }

  public AutotuneResultScheduledRollbacksInner rollbackAt(@Nullable OffsetDateTime rollbackAt) {
    this.rollbackAt = rollbackAt;
    return this;
  }

  /**
   * Get rollbackAt
   * @return rollbackAt
   */
  @Valid 
  @Schema(name = "rollbackAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rollbackAt")
  public @Nullable OffsetDateTime getRollbackAt() {
    return rollbackAt;
  }

  public void setRollbackAt(@Nullable OffsetDateTime rollbackAt) {
    this.rollbackAt = rollbackAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AutotuneResultScheduledRollbacksInner autotuneResultScheduledRollbacksInner = (AutotuneResultScheduledRollbacksInner) o;
    return Objects.equals(this.actionId, autotuneResultScheduledRollbacksInner.actionId) &&
        Objects.equals(this.rollbackAt, autotuneResultScheduledRollbacksInner.rollbackAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(actionId, rollbackAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AutotuneResultScheduledRollbacksInner {\n");
    sb.append("    actionId: ").append(toIndentedString(actionId)).append("\n");
    sb.append("    rollbackAt: ").append(toIndentedString(rollbackAt)).append("\n");
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

