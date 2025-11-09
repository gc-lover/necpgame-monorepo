package com.necpgame.gameplayservice.model;

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
 * WeaponDetailsRequirements
 */

@JsonTypeName("WeaponDetails_requirements")

public class WeaponDetailsRequirements {

  private @Nullable Integer level;

  private @Nullable Object attributes;

  public WeaponDetailsRequirements level(@Nullable Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * @return level
   */
  
  @Schema(name = "level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level")
  public @Nullable Integer getLevel() {
    return level;
  }

  public void setLevel(@Nullable Integer level) {
    this.level = level;
  }

  public WeaponDetailsRequirements attributes(@Nullable Object attributes) {
    this.attributes = attributes;
    return this;
  }

  /**
   * Требования к атрибутам (REF, TECH и т.д.)
   * @return attributes
   */
  
  @Schema(name = "attributes", description = "Требования к атрибутам (REF, TECH и т.д.)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attributes")
  public @Nullable Object getAttributes() {
    return attributes;
  }

  public void setAttributes(@Nullable Object attributes) {
    this.attributes = attributes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WeaponDetailsRequirements weaponDetailsRequirements = (WeaponDetailsRequirements) o;
    return Objects.equals(this.level, weaponDetailsRequirements.level) &&
        Objects.equals(this.attributes, weaponDetailsRequirements.attributes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(level, attributes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WeaponDetailsRequirements {\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
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

