package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * PlayerImpactMajorDecisionsInner
 */

@JsonTypeName("PlayerImpact_major_decisions_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class PlayerImpactMajorDecisionsInner {

  private @Nullable String decisionId;

  private @Nullable String description;

  @Valid
  private List<String> consequences = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  public PlayerImpactMajorDecisionsInner decisionId(@Nullable String decisionId) {
    this.decisionId = decisionId;
    return this;
  }

  /**
   * Get decisionId
   * @return decisionId
   */
  
  @Schema(name = "decision_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("decision_id")
  public @Nullable String getDecisionId() {
    return decisionId;
  }

  public void setDecisionId(@Nullable String decisionId) {
    this.decisionId = decisionId;
  }

  public PlayerImpactMajorDecisionsInner description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public PlayerImpactMajorDecisionsInner consequences(List<String> consequences) {
    this.consequences = consequences;
    return this;
  }

  public PlayerImpactMajorDecisionsInner addConsequencesItem(String consequencesItem) {
    if (this.consequences == null) {
      this.consequences = new ArrayList<>();
    }
    this.consequences.add(consequencesItem);
    return this;
  }

  /**
   * Get consequences
   * @return consequences
   */
  
  @Schema(name = "consequences", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consequences")
  public List<String> getConsequences() {
    return consequences;
  }

  public void setConsequences(List<String> consequences) {
    this.consequences = consequences;
  }

  public PlayerImpactMajorDecisionsInner timestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public @Nullable OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerImpactMajorDecisionsInner playerImpactMajorDecisionsInner = (PlayerImpactMajorDecisionsInner) o;
    return Objects.equals(this.decisionId, playerImpactMajorDecisionsInner.decisionId) &&
        Objects.equals(this.description, playerImpactMajorDecisionsInner.description) &&
        Objects.equals(this.consequences, playerImpactMajorDecisionsInner.consequences) &&
        Objects.equals(this.timestamp, playerImpactMajorDecisionsInner.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(decisionId, description, consequences, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerImpactMajorDecisionsInner {\n");
    sb.append("    decisionId: ").append(toIndentedString(decisionId)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    consequences: ").append(toIndentedString(consequences)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
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

