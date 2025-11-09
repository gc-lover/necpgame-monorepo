package com.necpgame.adminservice.model;

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
 * DataModelInfoFieldsInner
 */

@JsonTypeName("DataModelInfo_fields_inner")

public class DataModelInfoFieldsInner {

  private @Nullable String fieldName;

  private @Nullable String type;

  private @Nullable Boolean required;

  private @Nullable String description;

  public DataModelInfoFieldsInner fieldName(@Nullable String fieldName) {
    this.fieldName = fieldName;
    return this;
  }

  /**
   * Get fieldName
   * @return fieldName
   */
  
  @Schema(name = "field_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("field_name")
  public @Nullable String getFieldName() {
    return fieldName;
  }

  public void setFieldName(@Nullable String fieldName) {
    this.fieldName = fieldName;
  }

  public DataModelInfoFieldsInner type(@Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable String getType() {
    return type;
  }

  public void setType(@Nullable String type) {
    this.type = type;
  }

  public DataModelInfoFieldsInner required(@Nullable Boolean required) {
    this.required = required;
    return this;
  }

  /**
   * Get required
   * @return required
   */
  
  @Schema(name = "required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required")
  public @Nullable Boolean getRequired() {
    return required;
  }

  public void setRequired(@Nullable Boolean required) {
    this.required = required;
  }

  public DataModelInfoFieldsInner description(@Nullable String description) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataModelInfoFieldsInner dataModelInfoFieldsInner = (DataModelInfoFieldsInner) o;
    return Objects.equals(this.fieldName, dataModelInfoFieldsInner.fieldName) &&
        Objects.equals(this.type, dataModelInfoFieldsInner.type) &&
        Objects.equals(this.required, dataModelInfoFieldsInner.required) &&
        Objects.equals(this.description, dataModelInfoFieldsInner.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fieldName, type, required, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DataModelInfoFieldsInner {\n");
    sb.append("    fieldName: ").append(toIndentedString(fieldName)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    required: ").append(toIndentedString(required)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

