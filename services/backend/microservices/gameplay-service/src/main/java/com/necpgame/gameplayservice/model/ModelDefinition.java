package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.ModelDefinitionFieldsInner;
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
 * ModelDefinition
 */


public class ModelDefinition {

  private @Nullable String modelName;

  private @Nullable String description;

  @Valid
  private List<@Valid ModelDefinitionFieldsInner> fields = new ArrayList<>();

  public ModelDefinition modelName(@Nullable String modelName) {
    this.modelName = modelName;
    return this;
  }

  /**
   * Get modelName
   * @return modelName
   */
  
  @Schema(name = "model_name", example = "Character", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("model_name")
  public @Nullable String getModelName() {
    return modelName;
  }

  public void setModelName(@Nullable String modelName) {
    this.modelName = modelName;
  }

  public ModelDefinition description(@Nullable String description) {
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

  public ModelDefinition fields(List<@Valid ModelDefinitionFieldsInner> fields) {
    this.fields = fields;
    return this;
  }

  public ModelDefinition addFieldsItem(ModelDefinitionFieldsInner fieldsItem) {
    if (this.fields == null) {
      this.fields = new ArrayList<>();
    }
    this.fields.add(fieldsItem);
    return this;
  }

  /**
   * Get fields
   * @return fields
   */
  @Valid 
  @Schema(name = "fields", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fields")
  public List<@Valid ModelDefinitionFieldsInner> getFields() {
    return fields;
  }

  public void setFields(List<@Valid ModelDefinitionFieldsInner> fields) {
    this.fields = fields;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelDefinition modelDefinition = (ModelDefinition) o;
    return Objects.equals(this.modelName, modelDefinition.modelName) &&
        Objects.equals(this.description, modelDefinition.description) &&
        Objects.equals(this.fields, modelDefinition.fields);
  }

  @Override
  public int hashCode() {
    return Objects.hash(modelName, description, fields);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelDefinition {\n");
    sb.append("    modelName: ").append(toIndentedString(modelName)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    fields: ").append(toIndentedString(fields)).append("\n");
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

