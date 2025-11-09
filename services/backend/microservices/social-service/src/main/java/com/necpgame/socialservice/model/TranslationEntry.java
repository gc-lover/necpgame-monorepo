package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TranslationEntry
 */


public class TranslationEntry {

  private String language;

  private String text;

  private @Nullable Float confidence;

  public TranslationEntry() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TranslationEntry(String language, String text) {
    this.language = language;
    this.text = text;
  }

  public TranslationEntry language(String language) {
    this.language = language;
    return this;
  }

  /**
   * ISO 639-1 код языка
   * @return language
   */
  @NotNull 
  @Schema(name = "language", example = "en", description = "ISO 639-1 код языка", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("language")
  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public TranslationEntry text(String text) {
    this.text = text;
    return this;
  }

  /**
   * Get text
   * @return text
   */
  @NotNull 
  @Schema(name = "text", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("text")
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public TranslationEntry confidence(@Nullable Float confidence) {
    this.confidence = confidence;
    return this;
  }

  /**
   * Get confidence
   * @return confidence
   */
  
  @Schema(name = "confidence", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("confidence")
  public @Nullable Float getConfidence() {
    return confidence;
  }

  public void setConfidence(@Nullable Float confidence) {
    this.confidence = confidence;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TranslationEntry translationEntry = (TranslationEntry) o;
    return Objects.equals(this.language, translationEntry.language) &&
        Objects.equals(this.text, translationEntry.text) &&
        Objects.equals(this.confidence, translationEntry.confidence);
  }

  @Override
  public int hashCode() {
    return Objects.hash(language, text, confidence);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TranslationEntry {\n");
    sb.append("    language: ").append(toIndentedString(language)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    confidence: ").append(toIndentedString(confidence)).append("\n");
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

