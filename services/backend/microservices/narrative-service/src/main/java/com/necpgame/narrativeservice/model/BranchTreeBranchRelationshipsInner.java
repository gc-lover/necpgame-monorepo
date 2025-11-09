package com.necpgame.narrativeservice.model;

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
 * BranchTreeBranchRelationshipsInner
 */

@JsonTypeName("BranchTree_branch_relationships_inner")

public class BranchTreeBranchRelationshipsInner {

  private @Nullable String fromBranch;

  private @Nullable String toBranch;

  /**
   * Gets or Sets relationshipType
   */
  public enum RelationshipTypeEnum {
    LEADS_TO("leads_to"),
    
    EXCLUSIVE_WITH("exclusive_with"),
    
    REQUIRES("requires"),
    
    BLOCKS("blocks");

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

  public BranchTreeBranchRelationshipsInner fromBranch(@Nullable String fromBranch) {
    this.fromBranch = fromBranch;
    return this;
  }

  /**
   * Get fromBranch
   * @return fromBranch
   */
  
  @Schema(name = "from_branch", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("from_branch")
  public @Nullable String getFromBranch() {
    return fromBranch;
  }

  public void setFromBranch(@Nullable String fromBranch) {
    this.fromBranch = fromBranch;
  }

  public BranchTreeBranchRelationshipsInner toBranch(@Nullable String toBranch) {
    this.toBranch = toBranch;
    return this;
  }

  /**
   * Get toBranch
   * @return toBranch
   */
  
  @Schema(name = "to_branch", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("to_branch")
  public @Nullable String getToBranch() {
    return toBranch;
  }

  public void setToBranch(@Nullable String toBranch) {
    this.toBranch = toBranch;
  }

  public BranchTreeBranchRelationshipsInner relationshipType(@Nullable RelationshipTypeEnum relationshipType) {
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
    BranchTreeBranchRelationshipsInner branchTreeBranchRelationshipsInner = (BranchTreeBranchRelationshipsInner) o;
    return Objects.equals(this.fromBranch, branchTreeBranchRelationshipsInner.fromBranch) &&
        Objects.equals(this.toBranch, branchTreeBranchRelationshipsInner.toBranch) &&
        Objects.equals(this.relationshipType, branchTreeBranchRelationshipsInner.relationshipType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fromBranch, toBranch, relationshipType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BranchTreeBranchRelationshipsInner {\n");
    sb.append("    fromBranch: ").append(toIndentedString(fromBranch)).append("\n");
    sb.append("    toBranch: ").append(toIndentedString(toBranch)).append("\n");
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

