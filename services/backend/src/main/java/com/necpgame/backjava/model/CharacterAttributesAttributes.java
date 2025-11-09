package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.Attribute;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CharacterAttributesAttributes
 */

@JsonTypeName("CharacterAttributes_attributes")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class CharacterAttributesAttributes {

  private @Nullable Attribute BODY;

  private @Nullable Attribute REFLEXES;

  private @Nullable Attribute TECHNICAL_ABILITY;

  private @Nullable Attribute INTELLIGENCE;

  private @Nullable Attribute COOL;

  public CharacterAttributesAttributes BODY(@Nullable Attribute BODY) {
    this.BODY = BODY;
    return this;
  }

  /**
   * Get BODY
   * @return BODY
   */
  @Valid 
  @Schema(name = "BODY", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("BODY")
  public @Nullable Attribute getBODY() {
    return BODY;
  }

  public void setBODY(@Nullable Attribute BODY) {
    this.BODY = BODY;
  }

  public CharacterAttributesAttributes REFLEXES(@Nullable Attribute REFLEXES) {
    this.REFLEXES = REFLEXES;
    return this;
  }

  /**
   * Get REFLEXES
   * @return REFLEXES
   */
  @Valid 
  @Schema(name = "REFLEXES", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("REFLEXES")
  public @Nullable Attribute getREFLEXES() {
    return REFLEXES;
  }

  public void setREFLEXES(@Nullable Attribute REFLEXES) {
    this.REFLEXES = REFLEXES;
  }

  public CharacterAttributesAttributes TECHNICAL_ABILITY(@Nullable Attribute TECHNICAL_ABILITY) {
    this.TECHNICAL_ABILITY = TECHNICAL_ABILITY;
    return this;
  }

  /**
   * Get TECHNICAL_ABILITY
   * @return TECHNICAL_ABILITY
   */
  @Valid 
  @Schema(name = "TECHNICAL_ABILITY", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("TECHNICAL_ABILITY")
  public @Nullable Attribute getTECHNICALABILITY() {
    return TECHNICAL_ABILITY;
  }

  public void setTECHNICALABILITY(@Nullable Attribute TECHNICAL_ABILITY) {
    this.TECHNICAL_ABILITY = TECHNICAL_ABILITY;
  }

  public CharacterAttributesAttributes INTELLIGENCE(@Nullable Attribute INTELLIGENCE) {
    this.INTELLIGENCE = INTELLIGENCE;
    return this;
  }

  /**
   * Get INTELLIGENCE
   * @return INTELLIGENCE
   */
  @Valid 
  @Schema(name = "INTELLIGENCE", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("INTELLIGENCE")
  public @Nullable Attribute getINTELLIGENCE() {
    return INTELLIGENCE;
  }

  public void setINTELLIGENCE(@Nullable Attribute INTELLIGENCE) {
    this.INTELLIGENCE = INTELLIGENCE;
  }

  public CharacterAttributesAttributes COOL(@Nullable Attribute COOL) {
    this.COOL = COOL;
    return this;
  }

  /**
   * Get COOL
   * @return COOL
   */
  @Valid 
  @Schema(name = "COOL", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("COOL")
  public @Nullable Attribute getCOOL() {
    return COOL;
  }

  public void setCOOL(@Nullable Attribute COOL) {
    this.COOL = COOL;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterAttributesAttributes characterAttributesAttributes = (CharacterAttributesAttributes) o;
    return Objects.equals(this.BODY, characterAttributesAttributes.BODY) &&
        Objects.equals(this.REFLEXES, characterAttributesAttributes.REFLEXES) &&
        Objects.equals(this.TECHNICAL_ABILITY, characterAttributesAttributes.TECHNICAL_ABILITY) &&
        Objects.equals(this.INTELLIGENCE, characterAttributesAttributes.INTELLIGENCE) &&
        Objects.equals(this.COOL, characterAttributesAttributes.COOL);
  }

  @Override
  public int hashCode() {
    return Objects.hash(BODY, REFLEXES, TECHNICAL_ABILITY, INTELLIGENCE, COOL);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterAttributesAttributes {\n");
    sb.append("    BODY: ").append(toIndentedString(BODY)).append("\n");
    sb.append("    REFLEXES: ").append(toIndentedString(REFLEXES)).append("\n");
    sb.append("    TECHNICAL_ABILITY: ").append(toIndentedString(TECHNICAL_ABILITY)).append("\n");
    sb.append("    INTELLIGENCE: ").append(toIndentedString(INTELLIGENCE)).append("\n");
    sb.append("    COOL: ").append(toIndentedString(COOL)).append("\n");
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

