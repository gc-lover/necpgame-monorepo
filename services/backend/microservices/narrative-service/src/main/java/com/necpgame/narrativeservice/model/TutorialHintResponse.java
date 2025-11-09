package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.narrativeservice.model.TutorialHint;
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
 * TutorialHintResponse
 */


public class TutorialHintResponse {

  private String questId;

  @Valid
  private List<@Valid TutorialHint> hints = new ArrayList<>();

  @Valid
  private List<String> replacements = new ArrayList<>();

  public TutorialHintResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TutorialHintResponse(String questId, List<@Valid TutorialHint> hints) {
    this.questId = questId;
    this.hints = hints;
  }

  public TutorialHintResponse questId(String questId) {
    this.questId = questId;
    return this;
  }

  /**
   * Get questId
   * @return questId
   */
  @NotNull 
  @Schema(name = "questId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("questId")
  public String getQuestId() {
    return questId;
  }

  public void setQuestId(String questId) {
    this.questId = questId;
  }

  public TutorialHintResponse hints(List<@Valid TutorialHint> hints) {
    this.hints = hints;
    return this;
  }

  public TutorialHintResponse addHintsItem(TutorialHint hintsItem) {
    if (this.hints == null) {
      this.hints = new ArrayList<>();
    }
    this.hints.add(hintsItem);
    return this;
  }

  /**
   * Get hints
   * @return hints
   */
  @NotNull @Valid 
  @Schema(name = "hints", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("hints")
  public List<@Valid TutorialHint> getHints() {
    return hints;
  }

  public void setHints(List<@Valid TutorialHint> hints) {
    this.hints = hints;
  }

  public TutorialHintResponse replacements(List<String> replacements) {
    this.replacements = replacements;
    return this;
  }

  public TutorialHintResponse addReplacementsItem(String replacementsItem) {
    if (this.replacements == null) {
      this.replacements = new ArrayList<>();
    }
    this.replacements.add(replacementsItem);
    return this;
  }

  /**
   * Get replacements
   * @return replacements
   */
  
  @Schema(name = "replacements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("replacements")
  public List<String> getReplacements() {
    return replacements;
  }

  public void setReplacements(List<String> replacements) {
    this.replacements = replacements;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TutorialHintResponse tutorialHintResponse = (TutorialHintResponse) o;
    return Objects.equals(this.questId, tutorialHintResponse.questId) &&
        Objects.equals(this.hints, tutorialHintResponse.hints) &&
        Objects.equals(this.replacements, tutorialHintResponse.replacements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, hints, replacements);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TutorialHintResponse {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    hints: ").append(toIndentedString(hints)).append("\n");
    sb.append("    replacements: ").append(toIndentedString(replacements)).append("\n");
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

