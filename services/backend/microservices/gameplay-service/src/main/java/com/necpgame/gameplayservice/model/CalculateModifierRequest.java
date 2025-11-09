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
 * CalculateModifierRequest
 */

@JsonTypeName("calculateModifier_request")

public class CalculateModifierRequest {

  private String characterId;

  /**
   * Gets or Sets attribute
   */
  public enum AttributeEnum {
    BODY("body"),
    
    REFLEX("reflex"),
    
    TECH("tech"),
    
    INTELLIGENCE("intelligence"),
    
    COOL("cool");

    private final String value;

    AttributeEnum(String value) {
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
    public static AttributeEnum fromValue(String value) {
      for (AttributeEnum b : AttributeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private AttributeEnum attribute;

  private @Nullable String skill;

  public CalculateModifierRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CalculateModifierRequest(String characterId, AttributeEnum attribute) {
    this.characterId = characterId;
    this.attribute = attribute;
  }

  public CalculateModifierRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public CalculateModifierRequest attribute(AttributeEnum attribute) {
    this.attribute = attribute;
    return this;
  }

  /**
   * Get attribute
   * @return attribute
   */
  @NotNull 
  @Schema(name = "attribute", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("attribute")
  public AttributeEnum getAttribute() {
    return attribute;
  }

  public void setAttribute(AttributeEnum attribute) {
    this.attribute = attribute;
  }

  public CalculateModifierRequest skill(@Nullable String skill) {
    this.skill = skill;
    return this;
  }

  /**
   * Опционально, для навыкового бонуса
   * @return skill
   */
  
  @Schema(name = "skill", description = "Опционально, для навыкового бонуса", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill")
  public @Nullable String getSkill() {
    return skill;
  }

  public void setSkill(@Nullable String skill) {
    this.skill = skill;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CalculateModifierRequest calculateModifierRequest = (CalculateModifierRequest) o;
    return Objects.equals(this.characterId, calculateModifierRequest.characterId) &&
        Objects.equals(this.attribute, calculateModifierRequest.attribute) &&
        Objects.equals(this.skill, calculateModifierRequest.skill);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, attribute, skill);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CalculateModifierRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    attribute: ").append(toIndentedString(attribute)).append("\n");
    sb.append("    skill: ").append(toIndentedString(skill)).append("\n");
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

