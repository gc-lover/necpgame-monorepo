package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TranslationSettings
 */


public class TranslationSettings {

  private Boolean enabled;

  @Valid
  private List<String> preferredLanguages = new ArrayList<>();

  private Boolean autoDetect = true;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastUpdatedAt;

  public TranslationSettings() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TranslationSettings(Boolean enabled) {
    this.enabled = enabled;
  }

  public TranslationSettings enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * Get enabled
   * @return enabled
   */
  @NotNull 
  @Schema(name = "enabled", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("enabled")
  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public TranslationSettings preferredLanguages(List<String> preferredLanguages) {
    this.preferredLanguages = preferredLanguages;
    return this;
  }

  public TranslationSettings addPreferredLanguagesItem(String preferredLanguagesItem) {
    if (this.preferredLanguages == null) {
      this.preferredLanguages = new ArrayList<>();
    }
    this.preferredLanguages.add(preferredLanguagesItem);
    return this;
  }

  /**
   * Get preferredLanguages
   * @return preferredLanguages
   */
  @Size(max = 5) 
  @Schema(name = "preferredLanguages", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("preferredLanguages")
  public List<String> getPreferredLanguages() {
    return preferredLanguages;
  }

  public void setPreferredLanguages(List<String> preferredLanguages) {
    this.preferredLanguages = preferredLanguages;
  }

  public TranslationSettings autoDetect(Boolean autoDetect) {
    this.autoDetect = autoDetect;
    return this;
  }

  /**
   * Get autoDetect
   * @return autoDetect
   */
  
  @Schema(name = "autoDetect", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("autoDetect")
  public Boolean getAutoDetect() {
    return autoDetect;
  }

  public void setAutoDetect(Boolean autoDetect) {
    this.autoDetect = autoDetect;
  }

  public TranslationSettings lastUpdatedAt(@Nullable OffsetDateTime lastUpdatedAt) {
    this.lastUpdatedAt = lastUpdatedAt;
    return this;
  }

  /**
   * Get lastUpdatedAt
   * @return lastUpdatedAt
   */
  @Valid 
  @Schema(name = "lastUpdatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastUpdatedAt")
  public @Nullable OffsetDateTime getLastUpdatedAt() {
    return lastUpdatedAt;
  }

  public void setLastUpdatedAt(@Nullable OffsetDateTime lastUpdatedAt) {
    this.lastUpdatedAt = lastUpdatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TranslationSettings translationSettings = (TranslationSettings) o;
    return Objects.equals(this.enabled, translationSettings.enabled) &&
        Objects.equals(this.preferredLanguages, translationSettings.preferredLanguages) &&
        Objects.equals(this.autoDetect, translationSettings.autoDetect) &&
        Objects.equals(this.lastUpdatedAt, translationSettings.lastUpdatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(enabled, preferredLanguages, autoDetect, lastUpdatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TranslationSettings {\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    preferredLanguages: ").append(toIndentedString(preferredLanguages)).append("\n");
    sb.append("    autoDetect: ").append(toIndentedString(autoDetect)).append("\n");
    sb.append("    lastUpdatedAt: ").append(toIndentedString(lastUpdatedAt)).append("\n");
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

