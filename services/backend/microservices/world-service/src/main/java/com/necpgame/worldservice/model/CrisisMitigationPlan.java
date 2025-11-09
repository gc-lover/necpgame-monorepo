package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.MitigationAction;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * CrisisMitigationPlan
 */


public class CrisisMitigationPlan {

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    DRAFT("draft"),
    
    IN_PROGRESS("in_progress"),
    
    COMPLETED("completed"),
    
    CANCELLED("cancelled");

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
  private List<@Valid MitigationAction> actions = new ArrayList<>();

  private @Nullable String assignedFaction;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime estimatedResolutionAt;

  public CrisisMitigationPlan status(@Nullable StatusEnum status) {
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

  public CrisisMitigationPlan actions(List<@Valid MitigationAction> actions) {
    this.actions = actions;
    return this;
  }

  public CrisisMitigationPlan addActionsItem(MitigationAction actionsItem) {
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
  @Valid 
  @Schema(name = "actions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actions")
  public List<@Valid MitigationAction> getActions() {
    return actions;
  }

  public void setActions(List<@Valid MitigationAction> actions) {
    this.actions = actions;
  }

  public CrisisMitigationPlan assignedFaction(@Nullable String assignedFaction) {
    this.assignedFaction = assignedFaction;
    return this;
  }

  /**
   * Код фракции, ответственной за план.
   * @return assignedFaction
   */
  
  @Schema(name = "assignedFaction", description = "Код фракции, ответственной за план.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("assignedFaction")
  public @Nullable String getAssignedFaction() {
    return assignedFaction;
  }

  public void setAssignedFaction(@Nullable String assignedFaction) {
    this.assignedFaction = assignedFaction;
  }

  public CrisisMitigationPlan estimatedResolutionAt(@Nullable OffsetDateTime estimatedResolutionAt) {
    this.estimatedResolutionAt = estimatedResolutionAt;
    return this;
  }

  /**
   * Get estimatedResolutionAt
   * @return estimatedResolutionAt
   */
  @Valid 
  @Schema(name = "estimatedResolutionAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimatedResolutionAt")
  public @Nullable OffsetDateTime getEstimatedResolutionAt() {
    return estimatedResolutionAt;
  }

  public void setEstimatedResolutionAt(@Nullable OffsetDateTime estimatedResolutionAt) {
    this.estimatedResolutionAt = estimatedResolutionAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CrisisMitigationPlan crisisMitigationPlan = (CrisisMitigationPlan) o;
    return Objects.equals(this.status, crisisMitigationPlan.status) &&
        Objects.equals(this.actions, crisisMitigationPlan.actions) &&
        Objects.equals(this.assignedFaction, crisisMitigationPlan.assignedFaction) &&
        Objects.equals(this.estimatedResolutionAt, crisisMitigationPlan.estimatedResolutionAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, actions, assignedFaction, estimatedResolutionAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CrisisMitigationPlan {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    actions: ").append(toIndentedString(actions)).append("\n");
    sb.append("    assignedFaction: ").append(toIndentedString(assignedFaction)).append("\n");
    sb.append("    estimatedResolutionAt: ").append(toIndentedString(estimatedResolutionAt)).append("\n");
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

