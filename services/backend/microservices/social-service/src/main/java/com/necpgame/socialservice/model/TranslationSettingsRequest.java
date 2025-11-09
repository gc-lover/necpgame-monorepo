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
 * TranslationSettingsRequest
 */


public class TranslationSettingsRequest {

  private Boolean enabled;

  @Valid
  private List<String> preferredLanguages = new ArrayList<>();

  private Boolean autoDetect = true;

  public TranslationSettingsRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TranslationSettingsRequest(Boolean enabled) {
    this.enabled = enabled;
  }

  public TranslationSettingsRequest enabled(Boolean enabled) {
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

  public TranslationSettingsRequest preferredLanguages(List<String> preferredLanguages) {
    this.preferredLanguages = preferredLanguages;
    return this;
  }

  public TranslationSettingsRequest addPreferredLanguagesItem(String preferredLanguagesItem) {
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

  public TranslationSettingsRequest autoDetect(Boolean autoDetect) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TranslationSettingsRequest translationSettingsRequest = (TranslationSettingsRequest) o;
    return Objects.equals(this.enabled, translationSettingsRequest.enabled) &&
        Objects.equals(this.preferredLanguages, translationSettingsRequest.preferredLanguages) &&
        Objects.equals(this.autoDetect, translationSettingsRequest.autoDetect);
  }

  @Override
  public int hashCode() {
    return Objects.hash(enabled, preferredLanguages, autoDetect);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TranslationSettingsRequest {\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    preferredLanguages: ").append(toIndentedString(preferredLanguages)).append("\n");
    sb.append("    autoDetect: ").append(toIndentedString(autoDetect)).append("\n");
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

