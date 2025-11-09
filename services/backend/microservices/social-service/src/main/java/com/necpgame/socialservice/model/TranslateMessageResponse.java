package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.TranslationEntry;
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
 * TranslateMessageResponse
 */


public class TranslateMessageResponse {

  @Valid
  private List<@Valid TranslationEntry> translations = new ArrayList<>();

  public TranslateMessageResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TranslateMessageResponse(List<@Valid TranslationEntry> translations) {
    this.translations = translations;
  }

  public TranslateMessageResponse translations(List<@Valid TranslationEntry> translations) {
    this.translations = translations;
    return this;
  }

  public TranslateMessageResponse addTranslationsItem(TranslationEntry translationsItem) {
    if (this.translations == null) {
      this.translations = new ArrayList<>();
    }
    this.translations.add(translationsItem);
    return this;
  }

  /**
   * Get translations
   * @return translations
   */
  @NotNull @Valid 
  @Schema(name = "translations", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("translations")
  public List<@Valid TranslationEntry> getTranslations() {
    return translations;
  }

  public void setTranslations(List<@Valid TranslationEntry> translations) {
    this.translations = translations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TranslateMessageResponse translateMessageResponse = (TranslateMessageResponse) o;
    return Objects.equals(this.translations, translateMessageResponse.translations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(translations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TranslateMessageResponse {\n");
    sb.append("    translations: ").append(toIndentedString(translations)).append("\n");
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

