package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * Ending
 */


public class Ending {

  private @Nullable String endingId;

  private @Nullable String name;

  private @Nullable String description;

  @Valid
  private List<String> requirements = new ArrayList<>();

  private @Nullable Boolean unlocked;

  public Ending endingId(@Nullable String endingId) {
    this.endingId = endingId;
    return this;
  }

  /**
   * Get endingId
   * @return endingId
   */
  
  @Schema(name = "ending_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ending_id")
  public @Nullable String getEndingId() {
    return endingId;
  }

  public void setEndingId(@Nullable String endingId) {
    this.endingId = endingId;
  }

  public Ending name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public Ending description(@Nullable String description) {
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

  public Ending requirements(List<String> requirements) {
    this.requirements = requirements;
    return this;
  }

  public Ending addRequirementsItem(String requirementsItem) {
    if (this.requirements == null) {
      this.requirements = new ArrayList<>();
    }
    this.requirements.add(requirementsItem);
    return this;
  }

  /**
   * Get requirements
   * @return requirements
   */
  
  @Schema(name = "requirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requirements")
  public List<String> getRequirements() {
    return requirements;
  }

  public void setRequirements(List<String> requirements) {
    this.requirements = requirements;
  }

  public Ending unlocked(@Nullable Boolean unlocked) {
    this.unlocked = unlocked;
    return this;
  }

  /**
   * Get unlocked
   * @return unlocked
   */
  
  @Schema(name = "unlocked", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlocked")
  public @Nullable Boolean getUnlocked() {
    return unlocked;
  }

  public void setUnlocked(@Nullable Boolean unlocked) {
    this.unlocked = unlocked;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Ending ending = (Ending) o;
    return Objects.equals(this.endingId, ending.endingId) &&
        Objects.equals(this.name, ending.name) &&
        Objects.equals(this.description, ending.description) &&
        Objects.equals(this.requirements, ending.requirements) &&
        Objects.equals(this.unlocked, ending.unlocked);
  }

  @Override
  public int hashCode() {
    return Objects.hash(endingId, name, description, requirements, unlocked);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Ending {\n");
    sb.append("    endingId: ").append(toIndentedString(endingId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
    sb.append("    unlocked: ").append(toIndentedString(unlocked)).append("\n");
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

