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
 * Переход между стадиями
 */

@Schema(name = "ProgressionTriggerResult_stage_transition", description = "Переход между стадиями")
@JsonTypeName("ProgressionTriggerResult_stage_transition")

public class ProgressionTriggerResultStageTransition {

  /**
   * Gets or Sets fromStage
   */
  public enum FromStageEnum {
    EARLY("early"),
    
    MIDDLE("middle"),
    
    LATE("late"),
    
    CYBERPSYCHOSIS("cyberpsychosis");

    private final String value;

    FromStageEnum(String value) {
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
    public static FromStageEnum fromValue(String value) {
      for (FromStageEnum b : FromStageEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable FromStageEnum fromStage;

  /**
   * Gets or Sets toStage
   */
  public enum ToStageEnum {
    EARLY("early"),
    
    MIDDLE("middle"),
    
    LATE("late"),
    
    CYBERPSYCHOSIS("cyberpsychosis");

    private final String value;

    ToStageEnum(String value) {
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
    public static ToStageEnum fromValue(String value) {
      for (ToStageEnum b : ToStageEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ToStageEnum toStage;

  public ProgressionTriggerResultStageTransition fromStage(@Nullable FromStageEnum fromStage) {
    this.fromStage = fromStage;
    return this;
  }

  /**
   * Get fromStage
   * @return fromStage
   */
  
  @Schema(name = "from_stage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("from_stage")
  public @Nullable FromStageEnum getFromStage() {
    return fromStage;
  }

  public void setFromStage(@Nullable FromStageEnum fromStage) {
    this.fromStage = fromStage;
  }

  public ProgressionTriggerResultStageTransition toStage(@Nullable ToStageEnum toStage) {
    this.toStage = toStage;
    return this;
  }

  /**
   * Get toStage
   * @return toStage
   */
  
  @Schema(name = "to_stage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("to_stage")
  public @Nullable ToStageEnum getToStage() {
    return toStage;
  }

  public void setToStage(@Nullable ToStageEnum toStage) {
    this.toStage = toStage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProgressionTriggerResultStageTransition progressionTriggerResultStageTransition = (ProgressionTriggerResultStageTransition) o;
    return Objects.equals(this.fromStage, progressionTriggerResultStageTransition.fromStage) &&
        Objects.equals(this.toStage, progressionTriggerResultStageTransition.toStage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fromStage, toStage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProgressionTriggerResultStageTransition {\n");
    sb.append("    fromStage: ").append(toIndentedString(fromStage)).append("\n");
    sb.append("    toStage: ").append(toIndentedString(toStage)).append("\n");
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

