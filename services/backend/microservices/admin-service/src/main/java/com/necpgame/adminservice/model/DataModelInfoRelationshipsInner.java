package com.necpgame.adminservice.model;

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
 * DataModelInfoRelationshipsInner
 */

@JsonTypeName("DataModelInfo_relationships_inner")

public class DataModelInfoRelationshipsInner {

  private @Nullable String relatedModel;

  /**
   * Gets or Sets relationshipType
   */
  public enum RelationshipTypeEnum {
    ONE_TO_ONE("one-to-one"),
    
    ONE_TO_MANY("one-to-many"),
    
    MANY_TO_MANY("many-to-many");

    private final String value;

    RelationshipTypeEnum(String value) {
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
    public static RelationshipTypeEnum fromValue(String value) {
      for (RelationshipTypeEnum b : RelationshipTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RelationshipTypeEnum relationshipType;

  public DataModelInfoRelationshipsInner relatedModel(@Nullable String relatedModel) {
    this.relatedModel = relatedModel;
    return this;
  }

  /**
   * Get relatedModel
   * @return relatedModel
   */
  
  @Schema(name = "related_model", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("related_model")
  public @Nullable String getRelatedModel() {
    return relatedModel;
  }

  public void setRelatedModel(@Nullable String relatedModel) {
    this.relatedModel = relatedModel;
  }

  public DataModelInfoRelationshipsInner relationshipType(@Nullable RelationshipTypeEnum relationshipType) {
    this.relationshipType = relationshipType;
    return this;
  }

  /**
   * Get relationshipType
   * @return relationshipType
   */
  
  @Schema(name = "relationship_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relationship_type")
  public @Nullable RelationshipTypeEnum getRelationshipType() {
    return relationshipType;
  }

  public void setRelationshipType(@Nullable RelationshipTypeEnum relationshipType) {
    this.relationshipType = relationshipType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataModelInfoRelationshipsInner dataModelInfoRelationshipsInner = (DataModelInfoRelationshipsInner) o;
    return Objects.equals(this.relatedModel, dataModelInfoRelationshipsInner.relatedModel) &&
        Objects.equals(this.relationshipType, dataModelInfoRelationshipsInner.relationshipType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(relatedModel, relationshipType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DataModelInfoRelationshipsInner {\n");
    sb.append("    relatedModel: ").append(toIndentedString(relatedModel)).append("\n");
    sb.append("    relationshipType: ").append(toIndentedString(relationshipType)).append("\n");
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

