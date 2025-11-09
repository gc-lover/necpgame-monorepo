package com.necpgame.adminservice.model;

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
 * GenerateRomanceDialogue200Response
 */

@JsonTypeName("generateRomanceDialogue_200_response")

public class GenerateRomanceDialogue200Response {

  private @Nullable String dialogueText;

  private @Nullable String tone;

  @Valid
  private List<Object> choices = new ArrayList<>();

  public GenerateRomanceDialogue200Response dialogueText(@Nullable String dialogueText) {
    this.dialogueText = dialogueText;
    return this;
  }

  /**
   * Get dialogueText
   * @return dialogueText
   */
  
  @Schema(name = "dialogue_text", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dialogue_text")
  public @Nullable String getDialogueText() {
    return dialogueText;
  }

  public void setDialogueText(@Nullable String dialogueText) {
    this.dialogueText = dialogueText;
  }

  public GenerateRomanceDialogue200Response tone(@Nullable String tone) {
    this.tone = tone;
    return this;
  }

  /**
   * Get tone
   * @return tone
   */
  
  @Schema(name = "tone", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tone")
  public @Nullable String getTone() {
    return tone;
  }

  public void setTone(@Nullable String tone) {
    this.tone = tone;
  }

  public GenerateRomanceDialogue200Response choices(List<Object> choices) {
    this.choices = choices;
    return this;
  }

  public GenerateRomanceDialogue200Response addChoicesItem(Object choicesItem) {
    if (this.choices == null) {
      this.choices = new ArrayList<>();
    }
    this.choices.add(choicesItem);
    return this;
  }

  /**
   * Get choices
   * @return choices
   */
  
  @Schema(name = "choices", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("choices")
  public List<Object> getChoices() {
    return choices;
  }

  public void setChoices(List<Object> choices) {
    this.choices = choices;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenerateRomanceDialogue200Response generateRomanceDialogue200Response = (GenerateRomanceDialogue200Response) o;
    return Objects.equals(this.dialogueText, generateRomanceDialogue200Response.dialogueText) &&
        Objects.equals(this.tone, generateRomanceDialogue200Response.tone) &&
        Objects.equals(this.choices, generateRomanceDialogue200Response.choices);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dialogueText, tone, choices);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateRomanceDialogue200Response {\n");
    sb.append("    dialogueText: ").append(toIndentedString(dialogueText)).append("\n");
    sb.append("    tone: ").append(toIndentedString(tone)).append("\n");
    sb.append("    choices: ").append(toIndentedString(choices)).append("\n");
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

