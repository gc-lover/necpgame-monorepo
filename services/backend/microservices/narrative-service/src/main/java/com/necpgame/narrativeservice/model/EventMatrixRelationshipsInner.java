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
 * EventMatrixRelationshipsInner
 */

@JsonTypeName("EventMatrix_relationships_inner")

public class EventMatrixRelationshipsInner {

  private @Nullable String eventA;

  private @Nullable String eventB;

  /**
   * Gets or Sets relationshipType
   */
  public enum RelationshipTypeEnum {
    PREREQUISITE("PREREQUISITE"),
    
    CONSEQUENCE("CONSEQUENCE"),
    
    EXCLUSIVE("EXCLUSIVE"),
    
    PARALLEL("PARALLEL");

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

  public EventMatrixRelationshipsInner eventA(@Nullable String eventA) {
    this.eventA = eventA;
    return this;
  }

  /**
   * Get eventA
   * @return eventA
   */
  
  @Schema(name = "event_a", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_a")
  public @Nullable String getEventA() {
    return eventA;
  }

  public void setEventA(@Nullable String eventA) {
    this.eventA = eventA;
  }

  public EventMatrixRelationshipsInner eventB(@Nullable String eventB) {
    this.eventB = eventB;
    return this;
  }

  /**
   * Get eventB
   * @return eventB
   */
  
  @Schema(name = "event_b", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_b")
  public @Nullable String getEventB() {
    return eventB;
  }

  public void setEventB(@Nullable String eventB) {
    this.eventB = eventB;
  }

  public EventMatrixRelationshipsInner relationshipType(@Nullable RelationshipTypeEnum relationshipType) {
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
    EventMatrixRelationshipsInner eventMatrixRelationshipsInner = (EventMatrixRelationshipsInner) o;
    return Objects.equals(this.eventA, eventMatrixRelationshipsInner.eventA) &&
        Objects.equals(this.eventB, eventMatrixRelationshipsInner.eventB) &&
        Objects.equals(this.relationshipType, eventMatrixRelationshipsInner.relationshipType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventA, eventB, relationshipType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventMatrixRelationshipsInner {\n");
    sb.append("    eventA: ").append(toIndentedString(eventA)).append("\n");
    sb.append("    eventB: ").append(toIndentedString(eventB)).append("\n");
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

