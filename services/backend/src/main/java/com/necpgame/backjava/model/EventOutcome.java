package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.EventOutcomeConsequences;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * EventOutcome
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class EventOutcome {

  private @Nullable String outcomeId;

  private @Nullable String description;

  private @Nullable EventOutcomeConsequences consequences;

  public EventOutcome outcomeId(@Nullable String outcomeId) {
    this.outcomeId = outcomeId;
    return this;
  }

  /**
   * Get outcomeId
   * @return outcomeId
   */
  
  @Schema(name = "outcome_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("outcome_id")
  public @Nullable String getOutcomeId() {
    return outcomeId;
  }

  public void setOutcomeId(@Nullable String outcomeId) {
    this.outcomeId = outcomeId;
  }

  public EventOutcome description(@Nullable String description) {
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

  public EventOutcome consequences(@Nullable EventOutcomeConsequences consequences) {
    this.consequences = consequences;
    return this;
  }

  /**
   * Get consequences
   * @return consequences
   */
  @Valid 
  @Schema(name = "consequences", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consequences")
  public @Nullable EventOutcomeConsequences getConsequences() {
    return consequences;
  }

  public void setConsequences(@Nullable EventOutcomeConsequences consequences) {
    this.consequences = consequences;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventOutcome eventOutcome = (EventOutcome) o;
    return Objects.equals(this.outcomeId, eventOutcome.outcomeId) &&
        Objects.equals(this.description, eventOutcome.description) &&
        Objects.equals(this.consequences, eventOutcome.consequences);
  }

  @Override
  public int hashCode() {
    return Objects.hash(outcomeId, description, consequences);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventOutcome {\n");
    sb.append("    outcomeId: ").append(toIndentedString(outcomeId)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    consequences: ").append(toIndentedString(consequences)).append("\n");
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

