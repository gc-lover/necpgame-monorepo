package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UIFeature
 */


public class UIFeature {

  private @Nullable String featureId;

  private @Nullable String name;

  private @Nullable String description;

  private @Nullable Boolean enabled;

  private JsonNullable<Integer> requiredLevel = JsonNullable.<Integer>undefined();

  public UIFeature featureId(@Nullable String featureId) {
    this.featureId = featureId;
    return this;
  }

  /**
   * Get featureId
   * @return featureId
   */
  
  @Schema(name = "feature_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("feature_id")
  public @Nullable String getFeatureId() {
    return featureId;
  }

  public void setFeatureId(@Nullable String featureId) {
    this.featureId = featureId;
  }

  public UIFeature name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public UIFeature description(@Nullable String description) {
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

  public UIFeature enabled(@Nullable Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * Get enabled
   * @return enabled
   */
  
  @Schema(name = "enabled", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("enabled")
  public @Nullable Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(@Nullable Boolean enabled) {
    this.enabled = enabled;
  }

  public UIFeature requiredLevel(Integer requiredLevel) {
    this.requiredLevel = JsonNullable.of(requiredLevel);
    return this;
  }

  /**
   * Get requiredLevel
   * @return requiredLevel
   */
  
  @Schema(name = "required_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_level")
  public JsonNullable<Integer> getRequiredLevel() {
    return requiredLevel;
  }

  public void setRequiredLevel(JsonNullable<Integer> requiredLevel) {
    this.requiredLevel = requiredLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UIFeature uiFeature = (UIFeature) o;
    return Objects.equals(this.featureId, uiFeature.featureId) &&
        Objects.equals(this.name, uiFeature.name) &&
        Objects.equals(this.description, uiFeature.description) &&
        Objects.equals(this.enabled, uiFeature.enabled) &&
        equalsNullable(this.requiredLevel, uiFeature.requiredLevel);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(featureId, name, description, enabled, hashCodeNullable(requiredLevel));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UIFeature {\n");
    sb.append("    featureId: ").append(toIndentedString(featureId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    requiredLevel: ").append(toIndentedString(requiredLevel)).append("\n");
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

