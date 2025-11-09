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
 * DaemonRequirements
 */

@JsonTypeName("Daemon_requirements")

public class DaemonRequirements {

  private @Nullable String propertyClass;

  private @Nullable Integer intelligence;

  private @Nullable Integer tech;

  public DaemonRequirements propertyClass(@Nullable String propertyClass) {
    this.propertyClass = propertyClass;
    return this;
  }

  /**
   * Get propertyClass
   * @return propertyClass
   */
  
  @Schema(name = "class", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("class")
  public @Nullable String getPropertyClass() {
    return propertyClass;
  }

  public void setPropertyClass(@Nullable String propertyClass) {
    this.propertyClass = propertyClass;
  }

  public DaemonRequirements intelligence(@Nullable Integer intelligence) {
    this.intelligence = intelligence;
    return this;
  }

  /**
   * Get intelligence
   * @return intelligence
   */
  
  @Schema(name = "intelligence", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("intelligence")
  public @Nullable Integer getIntelligence() {
    return intelligence;
  }

  public void setIntelligence(@Nullable Integer intelligence) {
    this.intelligence = intelligence;
  }

  public DaemonRequirements tech(@Nullable Integer tech) {
    this.tech = tech;
    return this;
  }

  /**
   * Get tech
   * @return tech
   */
  
  @Schema(name = "tech", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tech")
  public @Nullable Integer getTech() {
    return tech;
  }

  public void setTech(@Nullable Integer tech) {
    this.tech = tech;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DaemonRequirements daemonRequirements = (DaemonRequirements) o;
    return Objects.equals(this.propertyClass, daemonRequirements.propertyClass) &&
        Objects.equals(this.intelligence, daemonRequirements.intelligence) &&
        Objects.equals(this.tech, daemonRequirements.tech);
  }

  @Override
  public int hashCode() {
    return Objects.hash(propertyClass, intelligence, tech);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DaemonRequirements {\n");
    sb.append("    propertyClass: ").append(toIndentedString(propertyClass)).append("\n");
    sb.append("    intelligence: ").append(toIndentedString(intelligence)).append("\n");
    sb.append("    tech: ").append(toIndentedString(tech)).append("\n");
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

