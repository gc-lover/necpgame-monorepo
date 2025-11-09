package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * FactionQuestsResponseEndingsInner
 */

@JsonTypeName("FactionQuestsResponse_endings_inner")

public class FactionQuestsResponseEndingsInner {

  private @Nullable String endingId;

  private @Nullable String endingName;

  private @Nullable String description;

  @Valid
  private List<String> requirements = new ArrayList<>();

  public FactionQuestsResponseEndingsInner endingId(@Nullable String endingId) {
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

  public FactionQuestsResponseEndingsInner endingName(@Nullable String endingName) {
    this.endingName = endingName;
    return this;
  }

  /**
   * Get endingName
   * @return endingName
   */
  
  @Schema(name = "ending_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ending_name")
  public @Nullable String getEndingName() {
    return endingName;
  }

  public void setEndingName(@Nullable String endingName) {
    this.endingName = endingName;
  }

  public FactionQuestsResponseEndingsInner description(@Nullable String description) {
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

  public FactionQuestsResponseEndingsInner requirements(List<String> requirements) {
    this.requirements = requirements;
    return this;
  }

  public FactionQuestsResponseEndingsInner addRequirementsItem(String requirementsItem) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FactionQuestsResponseEndingsInner factionQuestsResponseEndingsInner = (FactionQuestsResponseEndingsInner) o;
    return Objects.equals(this.endingId, factionQuestsResponseEndingsInner.endingId) &&
        Objects.equals(this.endingName, factionQuestsResponseEndingsInner.endingName) &&
        Objects.equals(this.description, factionQuestsResponseEndingsInner.description) &&
        Objects.equals(this.requirements, factionQuestsResponseEndingsInner.requirements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(endingId, endingName, description, requirements);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FactionQuestsResponseEndingsInner {\n");
    sb.append("    endingId: ").append(toIndentedString(endingId)).append("\n");
    sb.append("    endingName: ").append(toIndentedString(endingName)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
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

