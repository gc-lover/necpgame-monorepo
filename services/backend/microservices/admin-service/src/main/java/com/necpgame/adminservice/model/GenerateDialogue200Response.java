package com.necpgame.adminservice.model;

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
 * GenerateDialogue200Response
 */

@JsonTypeName("generateDialogue_200_response")

public class GenerateDialogue200Response {

  private @Nullable String text;

  private @Nullable String tone;

  private @Nullable String emotion;

  public GenerateDialogue200Response text(@Nullable String text) {
    this.text = text;
    return this;
  }

  /**
   * Get text
   * @return text
   */
  
  @Schema(name = "text", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("text")
  public @Nullable String getText() {
    return text;
  }

  public void setText(@Nullable String text) {
    this.text = text;
  }

  public GenerateDialogue200Response tone(@Nullable String tone) {
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

  public GenerateDialogue200Response emotion(@Nullable String emotion) {
    this.emotion = emotion;
    return this;
  }

  /**
   * Get emotion
   * @return emotion
   */
  
  @Schema(name = "emotion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("emotion")
  public @Nullable String getEmotion() {
    return emotion;
  }

  public void setEmotion(@Nullable String emotion) {
    this.emotion = emotion;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenerateDialogue200Response generateDialogue200Response = (GenerateDialogue200Response) o;
    return Objects.equals(this.text, generateDialogue200Response.text) &&
        Objects.equals(this.tone, generateDialogue200Response.tone) &&
        Objects.equals(this.emotion, generateDialogue200Response.emotion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(text, tone, emotion);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateDialogue200Response {\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    tone: ").append(toIndentedString(tone)).append("\n");
    sb.append("    emotion: ").append(toIndentedString(emotion)).append("\n");
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

