package com.necpgame.worldservice.model;

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
 * LocalizationBundle
 */


public class LocalizationBundle {

  private @Nullable String locale;

  private @Nullable String title;

  private @Nullable String description;

  private @Nullable String callToAction;

  public LocalizationBundle locale(@Nullable String locale) {
    this.locale = locale;
    return this;
  }

  /**
   * Get locale
   * @return locale
   */
  
  @Schema(name = "locale", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("locale")
  public @Nullable String getLocale() {
    return locale;
  }

  public void setLocale(@Nullable String locale) {
    this.locale = locale;
  }

  public LocalizationBundle title(@Nullable String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  
  @Schema(name = "title", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public @Nullable String getTitle() {
    return title;
  }

  public void setTitle(@Nullable String title) {
    this.title = title;
  }

  public LocalizationBundle description(@Nullable String description) {
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

  public LocalizationBundle callToAction(@Nullable String callToAction) {
    this.callToAction = callToAction;
    return this;
  }

  /**
   * Get callToAction
   * @return callToAction
   */
  
  @Schema(name = "callToAction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("callToAction")
  public @Nullable String getCallToAction() {
    return callToAction;
  }

  public void setCallToAction(@Nullable String callToAction) {
    this.callToAction = callToAction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LocalizationBundle localizationBundle = (LocalizationBundle) o;
    return Objects.equals(this.locale, localizationBundle.locale) &&
        Objects.equals(this.title, localizationBundle.title) &&
        Objects.equals(this.description, localizationBundle.description) &&
        Objects.equals(this.callToAction, localizationBundle.callToAction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(locale, title, description, callToAction);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LocalizationBundle {\n");
    sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    callToAction: ").append(toIndentedString(callToAction)).append("\n");
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

