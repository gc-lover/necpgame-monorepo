package com.necpgame.adminservice.model;

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
 * GetNPCDecision200Response
 */

@JsonTypeName("getNPCDecision_200_response")

public class GetNPCDecision200Response {

  private @Nullable String decision;

  private @Nullable String reasoning;

  private @Nullable String emotionalState;

  private @Nullable Integer relationshipChange;

  public GetNPCDecision200Response decision(@Nullable String decision) {
    this.decision = decision;
    return this;
  }

  /**
   * Get decision
   * @return decision
   */
  
  @Schema(name = "decision", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("decision")
  public @Nullable String getDecision() {
    return decision;
  }

  public void setDecision(@Nullable String decision) {
    this.decision = decision;
  }

  public GetNPCDecision200Response reasoning(@Nullable String reasoning) {
    this.reasoning = reasoning;
    return this;
  }

  /**
   * Get reasoning
   * @return reasoning
   */
  
  @Schema(name = "reasoning", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reasoning")
  public @Nullable String getReasoning() {
    return reasoning;
  }

  public void setReasoning(@Nullable String reasoning) {
    this.reasoning = reasoning;
  }

  public GetNPCDecision200Response emotionalState(@Nullable String emotionalState) {
    this.emotionalState = emotionalState;
    return this;
  }

  /**
   * Get emotionalState
   * @return emotionalState
   */
  
  @Schema(name = "emotional_state", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("emotional_state")
  public @Nullable String getEmotionalState() {
    return emotionalState;
  }

  public void setEmotionalState(@Nullable String emotionalState) {
    this.emotionalState = emotionalState;
  }

  public GetNPCDecision200Response relationshipChange(@Nullable Integer relationshipChange) {
    this.relationshipChange = relationshipChange;
    return this;
  }

  /**
   * Get relationshipChange
   * @return relationshipChange
   */
  
  @Schema(name = "relationship_change", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relationship_change")
  public @Nullable Integer getRelationshipChange() {
    return relationshipChange;
  }

  public void setRelationshipChange(@Nullable Integer relationshipChange) {
    this.relationshipChange = relationshipChange;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetNPCDecision200Response getNPCDecision200Response = (GetNPCDecision200Response) o;
    return Objects.equals(this.decision, getNPCDecision200Response.decision) &&
        Objects.equals(this.reasoning, getNPCDecision200Response.reasoning) &&
        Objects.equals(this.emotionalState, getNPCDecision200Response.emotionalState) &&
        Objects.equals(this.relationshipChange, getNPCDecision200Response.relationshipChange);
  }

  @Override
  public int hashCode() {
    return Objects.hash(decision, reasoning, emotionalState, relationshipChange);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetNPCDecision200Response {\n");
    sb.append("    decision: ").append(toIndentedString(decision)).append("\n");
    sb.append("    reasoning: ").append(toIndentedString(reasoning)).append("\n");
    sb.append("    emotionalState: ").append(toIndentedString(emotionalState)).append("\n");
    sb.append("    relationshipChange: ").append(toIndentedString(relationshipChange)).append("\n");
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

