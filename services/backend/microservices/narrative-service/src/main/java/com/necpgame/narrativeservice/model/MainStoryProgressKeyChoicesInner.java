package com.necpgame.narrativeservice.model;

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
 * MainStoryProgressKeyChoicesInner
 */

@JsonTypeName("MainStoryProgress_key_choices_inner")

public class MainStoryProgressKeyChoicesInner {

  private @Nullable String choiceId;

  private @Nullable String decision;

  public MainStoryProgressKeyChoicesInner choiceId(@Nullable String choiceId) {
    this.choiceId = choiceId;
    return this;
  }

  /**
   * Get choiceId
   * @return choiceId
   */
  
  @Schema(name = "choice_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("choice_id")
  public @Nullable String getChoiceId() {
    return choiceId;
  }

  public void setChoiceId(@Nullable String choiceId) {
    this.choiceId = choiceId;
  }

  public MainStoryProgressKeyChoicesInner decision(@Nullable String decision) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MainStoryProgressKeyChoicesInner mainStoryProgressKeyChoicesInner = (MainStoryProgressKeyChoicesInner) o;
    return Objects.equals(this.choiceId, mainStoryProgressKeyChoicesInner.choiceId) &&
        Objects.equals(this.decision, mainStoryProgressKeyChoicesInner.decision);
  }

  @Override
  public int hashCode() {
    return Objects.hash(choiceId, decision);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MainStoryProgressKeyChoicesInner {\n");
    sb.append("    choiceId: ").append(toIndentedString(choiceId)).append("\n");
    sb.append("    decision: ").append(toIndentedString(decision)).append("\n");
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

