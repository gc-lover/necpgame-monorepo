package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * DialogueState
 */


public class DialogueState {

  private String id;

  private @Nullable String description;

  @Valid
  private Map<String, String> requirements = new HashMap<>();

  @Valid
  private List<String> unlocks = new ArrayList<>();

  private @Nullable String failureFallback;

  public DialogueState() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DialogueState(String id, Map<String, String> requirements) {
    this.id = id;
    this.requirements = requirements;
  }

  public DialogueState id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @NotNull 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public DialogueState description(@Nullable String description) {
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

  public DialogueState requirements(Map<String, String> requirements) {
    this.requirements = requirements;
    return this;
  }

  public DialogueState putRequirementsItem(String key, String requirementsItem) {
    if (this.requirements == null) {
      this.requirements = new HashMap<>();
    }
    this.requirements.put(key, requirementsItem);
    return this;
  }

  /**
   * Get requirements
   * @return requirements
   */
  @NotNull 
  @Schema(name = "requirements", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("requirements")
  public Map<String, String> getRequirements() {
    return requirements;
  }

  public void setRequirements(Map<String, String> requirements) {
    this.requirements = requirements;
  }

  public DialogueState unlocks(List<String> unlocks) {
    this.unlocks = unlocks;
    return this;
  }

  public DialogueState addUnlocksItem(String unlocksItem) {
    if (this.unlocks == null) {
      this.unlocks = new ArrayList<>();
    }
    this.unlocks.add(unlocksItem);
    return this;
  }

  /**
   * Get unlocks
   * @return unlocks
   */
  
  @Schema(name = "unlocks", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlocks")
  public List<String> getUnlocks() {
    return unlocks;
  }

  public void setUnlocks(List<String> unlocks) {
    this.unlocks = unlocks;
  }

  public DialogueState failureFallback(@Nullable String failureFallback) {
    this.failureFallback = failureFallback;
    return this;
  }

  /**
   * Get failureFallback
   * @return failureFallback
   */
  
  @Schema(name = "failureFallback", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("failureFallback")
  public @Nullable String getFailureFallback() {
    return failureFallback;
  }

  public void setFailureFallback(@Nullable String failureFallback) {
    this.failureFallback = failureFallback;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DialogueState dialogueState = (DialogueState) o;
    return Objects.equals(this.id, dialogueState.id) &&
        Objects.equals(this.description, dialogueState.description) &&
        Objects.equals(this.requirements, dialogueState.requirements) &&
        Objects.equals(this.unlocks, dialogueState.unlocks) &&
        Objects.equals(this.failureFallback, dialogueState.failureFallback);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, description, requirements, unlocks, failureFallback);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DialogueState {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
    sb.append("    unlocks: ").append(toIndentedString(unlocks)).append("\n");
    sb.append("    failureFallback: ").append(toIndentedString(failureFallback)).append("\n");
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

