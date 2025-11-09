package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.PendingConsequence;
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
 * ImpactResult
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ImpactResult {

  private @Nullable UUID impactId;

  @Valid
  private List<String> immediateConsequences = new ArrayList<>();

  @Valid
  private List<@Valid PendingConsequence> delayedConsequences = new ArrayList<>();

  private @Nullable Object worldStateChanges;

  private @Nullable Object reputationChanges;

  public ImpactResult impactId(@Nullable UUID impactId) {
    this.impactId = impactId;
    return this;
  }

  /**
   * Get impactId
   * @return impactId
   */
  @Valid 
  @Schema(name = "impact_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("impact_id")
  public @Nullable UUID getImpactId() {
    return impactId;
  }

  public void setImpactId(@Nullable UUID impactId) {
    this.impactId = impactId;
  }

  public ImpactResult immediateConsequences(List<String> immediateConsequences) {
    this.immediateConsequences = immediateConsequences;
    return this;
  }

  public ImpactResult addImmediateConsequencesItem(String immediateConsequencesItem) {
    if (this.immediateConsequences == null) {
      this.immediateConsequences = new ArrayList<>();
    }
    this.immediateConsequences.add(immediateConsequencesItem);
    return this;
  }

  /**
   * Get immediateConsequences
   * @return immediateConsequences
   */
  
  @Schema(name = "immediate_consequences", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("immediate_consequences")
  public List<String> getImmediateConsequences() {
    return immediateConsequences;
  }

  public void setImmediateConsequences(List<String> immediateConsequences) {
    this.immediateConsequences = immediateConsequences;
  }

  public ImpactResult delayedConsequences(List<@Valid PendingConsequence> delayedConsequences) {
    this.delayedConsequences = delayedConsequences;
    return this;
  }

  public ImpactResult addDelayedConsequencesItem(PendingConsequence delayedConsequencesItem) {
    if (this.delayedConsequences == null) {
      this.delayedConsequences = new ArrayList<>();
    }
    this.delayedConsequences.add(delayedConsequencesItem);
    return this;
  }

  /**
   * Get delayedConsequences
   * @return delayedConsequences
   */
  @Valid 
  @Schema(name = "delayed_consequences", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("delayed_consequences")
  public List<@Valid PendingConsequence> getDelayedConsequences() {
    return delayedConsequences;
  }

  public void setDelayedConsequences(List<@Valid PendingConsequence> delayedConsequences) {
    this.delayedConsequences = delayedConsequences;
  }

  public ImpactResult worldStateChanges(@Nullable Object worldStateChanges) {
    this.worldStateChanges = worldStateChanges;
    return this;
  }

  /**
   * Get worldStateChanges
   * @return worldStateChanges
   */
  
  @Schema(name = "world_state_changes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("world_state_changes")
  public @Nullable Object getWorldStateChanges() {
    return worldStateChanges;
  }

  public void setWorldStateChanges(@Nullable Object worldStateChanges) {
    this.worldStateChanges = worldStateChanges;
  }

  public ImpactResult reputationChanges(@Nullable Object reputationChanges) {
    this.reputationChanges = reputationChanges;
    return this;
  }

  /**
   * Get reputationChanges
   * @return reputationChanges
   */
  
  @Schema(name = "reputation_changes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation_changes")
  public @Nullable Object getReputationChanges() {
    return reputationChanges;
  }

  public void setReputationChanges(@Nullable Object reputationChanges) {
    this.reputationChanges = reputationChanges;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImpactResult impactResult = (ImpactResult) o;
    return Objects.equals(this.impactId, impactResult.impactId) &&
        Objects.equals(this.immediateConsequences, impactResult.immediateConsequences) &&
        Objects.equals(this.delayedConsequences, impactResult.delayedConsequences) &&
        Objects.equals(this.worldStateChanges, impactResult.worldStateChanges) &&
        Objects.equals(this.reputationChanges, impactResult.reputationChanges);
  }

  @Override
  public int hashCode() {
    return Objects.hash(impactId, immediateConsequences, delayedConsequences, worldStateChanges, reputationChanges);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImpactResult {\n");
    sb.append("    impactId: ").append(toIndentedString(impactId)).append("\n");
    sb.append("    immediateConsequences: ").append(toIndentedString(immediateConsequences)).append("\n");
    sb.append("    delayedConsequences: ").append(toIndentedString(delayedConsequences)).append("\n");
    sb.append("    worldStateChanges: ").append(toIndentedString(worldStateChanges)).append("\n");
    sb.append("    reputationChanges: ").append(toIndentedString(reputationChanges)).append("\n");
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

