package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
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
 * ValidateNarrativeRequest
 */

@JsonTypeName("validateNarrative_request")

public class ValidateNarrativeRequest {

  @Valid
  private List<Map<String, Object>> questSequence = new ArrayList<>();

  @Valid
  private List<Map<String, Object>> playerChoices = new ArrayList<>();

  public ValidateNarrativeRequest questSequence(List<Map<String, Object>> questSequence) {
    this.questSequence = questSequence;
    return this;
  }

  public ValidateNarrativeRequest addQuestSequenceItem(Map<String, Object> questSequenceItem) {
    if (this.questSequence == null) {
      this.questSequence = new ArrayList<>();
    }
    this.questSequence.add(questSequenceItem);
    return this;
  }

  /**
   * Get questSequence
   * @return questSequence
   */
  @Valid 
  @Schema(name = "quest_sequence", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_sequence")
  public List<Map<String, Object>> getQuestSequence() {
    return questSequence;
  }

  public void setQuestSequence(List<Map<String, Object>> questSequence) {
    this.questSequence = questSequence;
  }

  public ValidateNarrativeRequest playerChoices(List<Map<String, Object>> playerChoices) {
    this.playerChoices = playerChoices;
    return this;
  }

  public ValidateNarrativeRequest addPlayerChoicesItem(Map<String, Object> playerChoicesItem) {
    if (this.playerChoices == null) {
      this.playerChoices = new ArrayList<>();
    }
    this.playerChoices.add(playerChoicesItem);
    return this;
  }

  /**
   * Get playerChoices
   * @return playerChoices
   */
  @Valid 
  @Schema(name = "player_choices", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("player_choices")
  public List<Map<String, Object>> getPlayerChoices() {
    return playerChoices;
  }

  public void setPlayerChoices(List<Map<String, Object>> playerChoices) {
    this.playerChoices = playerChoices;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValidateNarrativeRequest validateNarrativeRequest = (ValidateNarrativeRequest) o;
    return Objects.equals(this.questSequence, validateNarrativeRequest.questSequence) &&
        Objects.equals(this.playerChoices, validateNarrativeRequest.playerChoices);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questSequence, playerChoices);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ValidateNarrativeRequest {\n");
    sb.append("    questSequence: ").append(toIndentedString(questSequence)).append("\n");
    sb.append("    playerChoices: ").append(toIndentedString(playerChoices)).append("\n");
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

