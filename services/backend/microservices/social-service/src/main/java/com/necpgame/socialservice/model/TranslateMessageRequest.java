package com.necpgame.socialservice.model;

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
 * TranslateMessageRequest
 */


public class TranslateMessageRequest {

  private String text;

  private @Nullable String sourceLanguage;

  @Valid
  private List<String> targetLanguages = new ArrayList<>();

  public TranslateMessageRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TranslateMessageRequest(String text, List<String> targetLanguages) {
    this.text = text;
    this.targetLanguages = targetLanguages;
  }

  public TranslateMessageRequest text(String text) {
    this.text = text;
    return this;
  }

  /**
   * Get text
   * @return text
   */
  @NotNull @Size(max = 1000) 
  @Schema(name = "text", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("text")
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public TranslateMessageRequest sourceLanguage(@Nullable String sourceLanguage) {
    this.sourceLanguage = sourceLanguage;
    return this;
  }

  /**
   * ISO 639-1 код языка
   * @return sourceLanguage
   */
  
  @Schema(name = "sourceLanguage", example = "en", description = "ISO 639-1 код языка", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sourceLanguage")
  public @Nullable String getSourceLanguage() {
    return sourceLanguage;
  }

  public void setSourceLanguage(@Nullable String sourceLanguage) {
    this.sourceLanguage = sourceLanguage;
  }

  public TranslateMessageRequest targetLanguages(List<String> targetLanguages) {
    this.targetLanguages = targetLanguages;
    return this;
  }

  public TranslateMessageRequest addTargetLanguagesItem(String targetLanguagesItem) {
    if (this.targetLanguages == null) {
      this.targetLanguages = new ArrayList<>();
    }
    this.targetLanguages.add(targetLanguagesItem);
    return this;
  }

  /**
   * Get targetLanguages
   * @return targetLanguages
   */
  @NotNull @Size(min = 1, max = 5) 
  @Schema(name = "targetLanguages", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("targetLanguages")
  public List<String> getTargetLanguages() {
    return targetLanguages;
  }

  public void setTargetLanguages(List<String> targetLanguages) {
    this.targetLanguages = targetLanguages;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TranslateMessageRequest translateMessageRequest = (TranslateMessageRequest) o;
    return Objects.equals(this.text, translateMessageRequest.text) &&
        Objects.equals(this.sourceLanguage, translateMessageRequest.sourceLanguage) &&
        Objects.equals(this.targetLanguages, translateMessageRequest.targetLanguages);
  }

  @Override
  public int hashCode() {
    return Objects.hash(text, sourceLanguage, targetLanguages);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TranslateMessageRequest {\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    sourceLanguage: ").append(toIndentedString(sourceLanguage)).append("\n");
    sb.append("    targetLanguages: ").append(toIndentedString(targetLanguages)).append("\n");
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

