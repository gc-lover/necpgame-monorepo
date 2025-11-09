package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.DataModelInfoFieldsInner;
import com.necpgame.adminservice.model.DataModelInfoRelationshipsInner;
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
 * DataModelInfo
 */


public class DataModelInfo {

  private @Nullable String modelName;

  private @Nullable String category;

  private @Nullable String description;

  @Valid
  private List<@Valid DataModelInfoFieldsInner> fields = new ArrayList<>();

  @Valid
  private List<@Valid DataModelInfoRelationshipsInner> relationships = new ArrayList<>();

  private @Nullable String databaseTable;

  @Valid
  private List<String> usedInEndpoints = new ArrayList<>();

  public DataModelInfo modelName(@Nullable String modelName) {
    this.modelName = modelName;
    return this;
  }

  /**
   * Get modelName
   * @return modelName
   */
  
  @Schema(name = "model_name", example = "Player", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("model_name")
  public @Nullable String getModelName() {
    return modelName;
  }

  public void setModelName(@Nullable String modelName) {
    this.modelName = modelName;
  }

  public DataModelInfo category(@Nullable String category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", example = "core", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable String getCategory() {
    return category;
  }

  public void setCategory(@Nullable String category) {
    this.category = category;
  }

  public DataModelInfo description(@Nullable String description) {
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

  public DataModelInfo fields(List<@Valid DataModelInfoFieldsInner> fields) {
    this.fields = fields;
    return this;
  }

  public DataModelInfo addFieldsItem(DataModelInfoFieldsInner fieldsItem) {
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
  public List<@Valid DataModelInfoFieldsInner> getFields() {
    return fields;
  }

  public void setFields(List<@Valid DataModelInfoFieldsInner> fields) {
    this.fields = fields;
  }

  public DataModelInfo relationships(List<@Valid DataModelInfoRelationshipsInner> relationships) {
    this.relationships = relationships;
    return this;
  }

  public DataModelInfo addRelationshipsItem(DataModelInfoRelationshipsInner relationshipsItem) {
    if (this.relationships == null) {
      this.relationships = new ArrayList<>();
    }
    this.relationships.add(relationshipsItem);
    return this;
  }

  /**
   * Relationships с другими models
   * @return relationships
   */
  @Valid 
  @Schema(name = "relationships", description = "Relationships с другими models", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relationships")
  public List<@Valid DataModelInfoRelationshipsInner> getRelationships() {
    return relationships;
  }

  public void setRelationships(List<@Valid DataModelInfoRelationshipsInner> relationships) {
    this.relationships = relationships;
  }

  public DataModelInfo databaseTable(@Nullable String databaseTable) {
    this.databaseTable = databaseTable;
    return this;
  }

  /**
   * Get databaseTable
   * @return databaseTable
   */
  
  @Schema(name = "database_table", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("database_table")
  public @Nullable String getDatabaseTable() {
    return databaseTable;
  }

  public void setDatabaseTable(@Nullable String databaseTable) {
    this.databaseTable = databaseTable;
  }

  public DataModelInfo usedInEndpoints(List<String> usedInEndpoints) {
    this.usedInEndpoints = usedInEndpoints;
    return this;
  }

  public DataModelInfo addUsedInEndpointsItem(String usedInEndpointsItem) {
    if (this.usedInEndpoints == null) {
      this.usedInEndpoints = new ArrayList<>();
    }
    this.usedInEndpoints.add(usedInEndpointsItem);
    return this;
  }

  /**
   * Endpoints использующие эту model
   * @return usedInEndpoints
   */
  
  @Schema(name = "used_in_endpoints", description = "Endpoints использующие эту model", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("used_in_endpoints")
  public List<String> getUsedInEndpoints() {
    return usedInEndpoints;
  }

  public void setUsedInEndpoints(List<String> usedInEndpoints) {
    this.usedInEndpoints = usedInEndpoints;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataModelInfo dataModelInfo = (DataModelInfo) o;
    return Objects.equals(this.modelName, dataModelInfo.modelName) &&
        Objects.equals(this.category, dataModelInfo.category) &&
        Objects.equals(this.description, dataModelInfo.description) &&
        Objects.equals(this.fields, dataModelInfo.fields) &&
        Objects.equals(this.relationships, dataModelInfo.relationships) &&
        Objects.equals(this.databaseTable, dataModelInfo.databaseTable) &&
        Objects.equals(this.usedInEndpoints, dataModelInfo.usedInEndpoints);
  }

  @Override
  public int hashCode() {
    return Objects.hash(modelName, category, description, fields, relationships, databaseTable, usedInEndpoints);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DataModelInfo {\n");
    sb.append("    modelName: ").append(toIndentedString(modelName)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    fields: ").append(toIndentedString(fields)).append("\n");
    sb.append("    relationships: ").append(toIndentedString(relationships)).append("\n");
    sb.append("    databaseTable: ").append(toIndentedString(databaseTable)).append("\n");
    sb.append("    usedInEndpoints: ").append(toIndentedString(usedInEndpoints)).append("\n");
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

