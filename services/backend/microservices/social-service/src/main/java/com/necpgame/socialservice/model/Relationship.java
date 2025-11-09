package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Relationship
 */


public class Relationship {

  private @Nullable String relationshipId;

  private @Nullable String characterId;

  private @Nullable String targetId;

  private @Nullable String targetName;

  /**
   * Gets or Sets targetType
   */
  public enum TargetTypeEnum {
    NPC("npc"),
    
    PLAYER("player");

    private final String value;

    TargetTypeEnum(String value) {
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
    public static TargetTypeEnum fromValue(String value) {
      for (TargetTypeEnum b : TargetTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TargetTypeEnum targetType;

  /**
   * Gets or Sets relationshipType
   */
  public enum RelationshipTypeEnum {
    FRIENDSHIP("friendship"),
    
    ROMANCE("romance"),
    
    RIVALRY("rivalry"),
    
    PROFESSIONAL("professional"),
    
    FAMILY("family");

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

  private @Nullable BigDecimal level;

  private @Nullable String stage;

  public Relationship relationshipId(@Nullable String relationshipId) {
    this.relationshipId = relationshipId;
    return this;
  }

  /**
   * Get relationshipId
   * @return relationshipId
   */
  
  @Schema(name = "relationship_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relationship_id")
  public @Nullable String getRelationshipId() {
    return relationshipId;
  }

  public void setRelationshipId(@Nullable String relationshipId) {
    this.relationshipId = relationshipId;
  }

  public Relationship characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public Relationship targetId(@Nullable String targetId) {
    this.targetId = targetId;
    return this;
  }

  /**
   * Get targetId
   * @return targetId
   */
  
  @Schema(name = "target_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target_id")
  public @Nullable String getTargetId() {
    return targetId;
  }

  public void setTargetId(@Nullable String targetId) {
    this.targetId = targetId;
  }

  public Relationship targetName(@Nullable String targetName) {
    this.targetName = targetName;
    return this;
  }

  /**
   * Get targetName
   * @return targetName
   */
  
  @Schema(name = "target_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target_name")
  public @Nullable String getTargetName() {
    return targetName;
  }

  public void setTargetName(@Nullable String targetName) {
    this.targetName = targetName;
  }

  public Relationship targetType(@Nullable TargetTypeEnum targetType) {
    this.targetType = targetType;
    return this;
  }

  /**
   * Get targetType
   * @return targetType
   */
  
  @Schema(name = "target_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target_type")
  public @Nullable TargetTypeEnum getTargetType() {
    return targetType;
  }

  public void setTargetType(@Nullable TargetTypeEnum targetType) {
    this.targetType = targetType;
  }

  public Relationship relationshipType(@Nullable RelationshipTypeEnum relationshipType) {
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

  public Relationship level(@Nullable BigDecimal level) {
    this.level = level;
    return this;
  }

  /**
   * Уровень отношений (0-100)
   * minimum: 0
   * maximum: 100
   * @return level
   */
  @Valid @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "level", description = "Уровень отношений (0-100)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level")
  public @Nullable BigDecimal getLevel() {
    return level;
  }

  public void setLevel(@Nullable BigDecimal level) {
    this.level = level;
  }

  public Relationship stage(@Nullable String stage) {
    this.stage = stage;
    return this;
  }

  /**
   * Стадия отношений
   * @return stage
   */
  
  @Schema(name = "stage", description = "Стадия отношений", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stage")
  public @Nullable String getStage() {
    return stage;
  }

  public void setStage(@Nullable String stage) {
    this.stage = stage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Relationship relationship = (Relationship) o;
    return Objects.equals(this.relationshipId, relationship.relationshipId) &&
        Objects.equals(this.characterId, relationship.characterId) &&
        Objects.equals(this.targetId, relationship.targetId) &&
        Objects.equals(this.targetName, relationship.targetName) &&
        Objects.equals(this.targetType, relationship.targetType) &&
        Objects.equals(this.relationshipType, relationship.relationshipType) &&
        Objects.equals(this.level, relationship.level) &&
        Objects.equals(this.stage, relationship.stage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(relationshipId, characterId, targetId, targetName, targetType, relationshipType, level, stage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Relationship {\n");
    sb.append("    relationshipId: ").append(toIndentedString(relationshipId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    targetName: ").append(toIndentedString(targetName)).append("\n");
    sb.append("    targetType: ").append(toIndentedString(targetType)).append("\n");
    sb.append("    relationshipType: ").append(toIndentedString(relationshipType)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    stage: ").append(toIndentedString(stage)).append("\n");
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

