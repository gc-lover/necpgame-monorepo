package com.necpgame.backjava.model;

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
 * GetEventDependencies200Response
 */

@JsonTypeName("getEventDependencies_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetEventDependencies200Response {

  private @Nullable String eventId;

  @Valid
  private List<String> prerequisites = new ArrayList<>();

  @Valid
  private List<String> consequences = new ArrayList<>();

  @Valid
  private List<String> mutuallyExclusive = new ArrayList<>();

  public GetEventDependencies200Response eventId(@Nullable String eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  
  @Schema(name = "event_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_id")
  public @Nullable String getEventId() {
    return eventId;
  }

  public void setEventId(@Nullable String eventId) {
    this.eventId = eventId;
  }

  public GetEventDependencies200Response prerequisites(List<String> prerequisites) {
    this.prerequisites = prerequisites;
    return this;
  }

  public GetEventDependencies200Response addPrerequisitesItem(String prerequisitesItem) {
    if (this.prerequisites == null) {
      this.prerequisites = new ArrayList<>();
    }
    this.prerequisites.add(prerequisitesItem);
    return this;
  }

  /**
   * Что должно произойти перед этим событием
   * @return prerequisites
   */
  
  @Schema(name = "prerequisites", description = "Что должно произойти перед этим событием", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("prerequisites")
  public List<String> getPrerequisites() {
    return prerequisites;
  }

  public void setPrerequisites(List<String> prerequisites) {
    this.prerequisites = prerequisites;
  }

  public GetEventDependencies200Response consequences(List<String> consequences) {
    this.consequences = consequences;
    return this;
  }

  public GetEventDependencies200Response addConsequencesItem(String consequencesItem) {
    if (this.consequences == null) {
      this.consequences = new ArrayList<>();
    }
    this.consequences.add(consequencesItem);
    return this;
  }

  /**
   * Что происходит после этого события
   * @return consequences
   */
  
  @Schema(name = "consequences", description = "Что происходит после этого события", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consequences")
  public List<String> getConsequences() {
    return consequences;
  }

  public void setConsequences(List<String> consequences) {
    this.consequences = consequences;
  }

  public GetEventDependencies200Response mutuallyExclusive(List<String> mutuallyExclusive) {
    this.mutuallyExclusive = mutuallyExclusive;
    return this;
  }

  public GetEventDependencies200Response addMutuallyExclusiveItem(String mutuallyExclusiveItem) {
    if (this.mutuallyExclusive == null) {
      this.mutuallyExclusive = new ArrayList<>();
    }
    this.mutuallyExclusive.add(mutuallyExclusiveItem);
    return this;
  }

  /**
   * События, несовместимые с этим
   * @return mutuallyExclusive
   */
  
  @Schema(name = "mutually_exclusive", description = "События, несовместимые с этим", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mutually_exclusive")
  public List<String> getMutuallyExclusive() {
    return mutuallyExclusive;
  }

  public void setMutuallyExclusive(List<String> mutuallyExclusive) {
    this.mutuallyExclusive = mutuallyExclusive;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetEventDependencies200Response getEventDependencies200Response = (GetEventDependencies200Response) o;
    return Objects.equals(this.eventId, getEventDependencies200Response.eventId) &&
        Objects.equals(this.prerequisites, getEventDependencies200Response.prerequisites) &&
        Objects.equals(this.consequences, getEventDependencies200Response.consequences) &&
        Objects.equals(this.mutuallyExclusive, getEventDependencies200Response.mutuallyExclusive);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, prerequisites, consequences, mutuallyExclusive);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetEventDependencies200Response {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    prerequisites: ").append(toIndentedString(prerequisites)).append("\n");
    sb.append("    consequences: ").append(toIndentedString(consequences)).append("\n");
    sb.append("    mutuallyExclusive: ").append(toIndentedString(mutuallyExclusive)).append("\n");
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

