package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * BranchCondition
 */


public class BranchCondition {

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    QUEST_COMPLETED("quest_completed"),
    
    CHOICE_MADE("choice_made"),
    
    REPUTATION_THRESHOLD("reputation_threshold"),
    
    ITEM_OWNED("item_owned"),
    
    SKILL_LEVEL("skill_level"),
    
    RELATIONSHIP_LEVEL("relationship_level"),
    
    FACTION_STANDING("faction_standing"),
    
    WORLD_STATE("world_state");

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

  private TypeEnum type;

  @Valid
  private Map<String, Object> parameters = new HashMap<>();

  private Boolean required = true;

  public BranchCondition() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BranchCondition(TypeEnum type) {
    this.type = type;
  }

  public BranchCondition type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public BranchCondition parameters(Map<String, Object> parameters) {
    this.parameters = parameters;
    return this;
  }

  public BranchCondition putParametersItem(String key, Object parametersItem) {
    if (this.parameters == null) {
      this.parameters = new HashMap<>();
    }
    this.parameters.put(key, parametersItem);
    return this;
  }

  /**
   * Параметры условия
   * @return parameters
   */
  
  @Schema(name = "parameters", description = "Параметры условия", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("parameters")
  public Map<String, Object> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, Object> parameters) {
    this.parameters = parameters;
  }

  public BranchCondition required(Boolean required) {
    this.required = required;
    return this;
  }

  /**
   * Get required
   * @return required
   */
  
  @Schema(name = "required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required")
  public Boolean getRequired() {
    return required;
  }

  public void setRequired(Boolean required) {
    this.required = required;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BranchCondition branchCondition = (BranchCondition) o;
    return Objects.equals(this.type, branchCondition.type) &&
        Objects.equals(this.parameters, branchCondition.parameters) &&
        Objects.equals(this.required, branchCondition.required);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, parameters, required);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BranchCondition {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    parameters: ").append(toIndentedString(parameters)).append("\n");
    sb.append("    required: ").append(toIndentedString(required)).append("\n");
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

