package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ProgressionMilestoneRequirement
 */

@JsonTypeName("ProgressionMilestone_requirement")

public class ProgressionMilestoneRequirement {

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    LEVEL("LEVEL"),
    
    SKILL_LEVEL("SKILL_LEVEL"),
    
    TOTAL_EXPERIENCE("TOTAL_EXPERIENCE");

    private final String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  private @Nullable Integer targetValue;

  public ProgressionMilestoneRequirement type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public ProgressionMilestoneRequirement targetValue(@Nullable Integer targetValue) {
    this.targetValue = targetValue;
    return this;
  }

  /**
   * Get targetValue
   * @return targetValue
   */
  
  @Schema(name = "target_value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target_value")
  public @Nullable Integer getTargetValue() {
    return targetValue;
  }

  public void setTargetValue(@Nullable Integer targetValue) {
    this.targetValue = targetValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProgressionMilestoneRequirement progressionMilestoneRequirement = (ProgressionMilestoneRequirement) o;
    return Objects.equals(this.type, progressionMilestoneRequirement.type) &&
        Objects.equals(this.targetValue, progressionMilestoneRequirement.targetValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, targetValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProgressionMilestoneRequirement {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    targetValue: ").append(toIndentedString(targetValue)).append("\n");
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

