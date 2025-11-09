package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.DetermineRomanceTriggerRequestRecentInteractionsInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * DetermineRomanceTriggerRequest
 */

@JsonTypeName("determineRomanceTrigger_request")

public class DetermineRomanceTriggerRequest {

  private @Nullable UUID relationshipId;

  private @Nullable Object currentState;

  @Valid
  private List<@Valid DetermineRomanceTriggerRequestRecentInteractionsInner> recentInteractions = new ArrayList<>();

  public DetermineRomanceTriggerRequest relationshipId(@Nullable UUID relationshipId) {
    this.relationshipId = relationshipId;
    return this;
  }

  /**
   * Get relationshipId
   * @return relationshipId
   */
  @Valid 
  @Schema(name = "relationship_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relationship_id")
  public @Nullable UUID getRelationshipId() {
    return relationshipId;
  }

  public void setRelationshipId(@Nullable UUID relationshipId) {
    this.relationshipId = relationshipId;
  }

  public DetermineRomanceTriggerRequest currentState(@Nullable Object currentState) {
    this.currentState = currentState;
    return this;
  }

  /**
   * Get currentState
   * @return currentState
   */
  
  @Schema(name = "current_state", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_state")
  public @Nullable Object getCurrentState() {
    return currentState;
  }

  public void setCurrentState(@Nullable Object currentState) {
    this.currentState = currentState;
  }

  public DetermineRomanceTriggerRequest recentInteractions(List<@Valid DetermineRomanceTriggerRequestRecentInteractionsInner> recentInteractions) {
    this.recentInteractions = recentInteractions;
    return this;
  }

  public DetermineRomanceTriggerRequest addRecentInteractionsItem(DetermineRomanceTriggerRequestRecentInteractionsInner recentInteractionsItem) {
    if (this.recentInteractions == null) {
      this.recentInteractions = new ArrayList<>();
    }
    this.recentInteractions.add(recentInteractionsItem);
    return this;
  }

  /**
   * Get recentInteractions
   * @return recentInteractions
   */
  @Valid 
  @Schema(name = "recent_interactions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recent_interactions")
  public List<@Valid DetermineRomanceTriggerRequestRecentInteractionsInner> getRecentInteractions() {
    return recentInteractions;
  }

  public void setRecentInteractions(List<@Valid DetermineRomanceTriggerRequestRecentInteractionsInner> recentInteractions) {
    this.recentInteractions = recentInteractions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DetermineRomanceTriggerRequest determineRomanceTriggerRequest = (DetermineRomanceTriggerRequest) o;
    return Objects.equals(this.relationshipId, determineRomanceTriggerRequest.relationshipId) &&
        Objects.equals(this.currentState, determineRomanceTriggerRequest.currentState) &&
        Objects.equals(this.recentInteractions, determineRomanceTriggerRequest.recentInteractions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(relationshipId, currentState, recentInteractions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DetermineRomanceTriggerRequest {\n");
    sb.append("    relationshipId: ").append(toIndentedString(relationshipId)).append("\n");
    sb.append("    currentState: ").append(toIndentedString(currentState)).append("\n");
    sb.append("    recentInteractions: ").append(toIndentedString(recentInteractions)).append("\n");
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

