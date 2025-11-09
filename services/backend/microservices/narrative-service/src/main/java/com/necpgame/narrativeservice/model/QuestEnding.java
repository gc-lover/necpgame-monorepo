package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.narrativeservice.model.QuestEndingConsequences;
import com.necpgame.narrativeservice.model.QuestEndingRequirements;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * QuestEnding
 */


public class QuestEnding {

  private @Nullable String endingId;

  private @Nullable String name;

  private @Nullable String description;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    GOOD("GOOD"),
    
    NEUTRAL("NEUTRAL"),
    
    BAD("BAD"),
    
    SECRET("SECRET"),
    
    TRAGIC("TRAGIC"),
    
    TRIUMPHANT("TRIUMPHANT");

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

  private @Nullable QuestEndingRequirements requirements;

  private @Nullable QuestEndingConsequences consequences;

  public QuestEnding endingId(@Nullable String endingId) {
    this.endingId = endingId;
    return this;
  }

  /**
   * Get endingId
   * @return endingId
   */
  
  @Schema(name = "ending_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ending_id")
  public @Nullable String getEndingId() {
    return endingId;
  }

  public void setEndingId(@Nullable String endingId) {
    this.endingId = endingId;
  }

  public QuestEnding name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", example = "Justice Served", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public QuestEnding description(@Nullable String description) {
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

  public QuestEnding type(@Nullable TypeEnum type) {
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

  public QuestEnding requirements(@Nullable QuestEndingRequirements requirements) {
    this.requirements = requirements;
    return this;
  }

  /**
   * Get requirements
   * @return requirements
   */
  @Valid 
  @Schema(name = "requirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requirements")
  public @Nullable QuestEndingRequirements getRequirements() {
    return requirements;
  }

  public void setRequirements(@Nullable QuestEndingRequirements requirements) {
    this.requirements = requirements;
  }

  public QuestEnding consequences(@Nullable QuestEndingConsequences consequences) {
    this.consequences = consequences;
    return this;
  }

  /**
   * Get consequences
   * @return consequences
   */
  @Valid 
  @Schema(name = "consequences", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consequences")
  public @Nullable QuestEndingConsequences getConsequences() {
    return consequences;
  }

  public void setConsequences(@Nullable QuestEndingConsequences consequences) {
    this.consequences = consequences;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestEnding questEnding = (QuestEnding) o;
    return Objects.equals(this.endingId, questEnding.endingId) &&
        Objects.equals(this.name, questEnding.name) &&
        Objects.equals(this.description, questEnding.description) &&
        Objects.equals(this.type, questEnding.type) &&
        Objects.equals(this.requirements, questEnding.requirements) &&
        Objects.equals(this.consequences, questEnding.consequences);
  }

  @Override
  public int hashCode() {
    return Objects.hash(endingId, name, description, type, requirements, consequences);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestEnding {\n");
    sb.append("    endingId: ").append(toIndentedString(endingId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
    sb.append("    consequences: ").append(toIndentedString(consequences)).append("\n");
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

