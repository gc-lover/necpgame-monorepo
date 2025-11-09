package com.necpgame.backjava.model;

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
 * Внешность персонажа
 */

@Schema(name = "createCharacter_request_appearance", description = "Внешность персонажа")
@JsonTypeName("createCharacter_request_appearance")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class CreateCharacterRequestAppearance {

  private @Nullable String bodyType;

  private @Nullable String hairStyle;

  private @Nullable String hairColor;

  private @Nullable String skinTone;

  public CreateCharacterRequestAppearance bodyType(@Nullable String bodyType) {
    this.bodyType = bodyType;
    return this;
  }

  /**
   * Get bodyType
   * @return bodyType
   */
  
  @Schema(name = "body_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("body_type")
  public @Nullable String getBodyType() {
    return bodyType;
  }

  public void setBodyType(@Nullable String bodyType) {
    this.bodyType = bodyType;
  }

  public CreateCharacterRequestAppearance hairStyle(@Nullable String hairStyle) {
    this.hairStyle = hairStyle;
    return this;
  }

  /**
   * Get hairStyle
   * @return hairStyle
   */
  
  @Schema(name = "hair_style", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hair_style")
  public @Nullable String getHairStyle() {
    return hairStyle;
  }

  public void setHairStyle(@Nullable String hairStyle) {
    this.hairStyle = hairStyle;
  }

  public CreateCharacterRequestAppearance hairColor(@Nullable String hairColor) {
    this.hairColor = hairColor;
    return this;
  }

  /**
   * Get hairColor
   * @return hairColor
   */
  
  @Schema(name = "hair_color", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hair_color")
  public @Nullable String getHairColor() {
    return hairColor;
  }

  public void setHairColor(@Nullable String hairColor) {
    this.hairColor = hairColor;
  }

  public CreateCharacterRequestAppearance skinTone(@Nullable String skinTone) {
    this.skinTone = skinTone;
    return this;
  }

  /**
   * Get skinTone
   * @return skinTone
   */
  
  @Schema(name = "skin_tone", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skin_tone")
  public @Nullable String getSkinTone() {
    return skinTone;
  }

  public void setSkinTone(@Nullable String skinTone) {
    this.skinTone = skinTone;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateCharacterRequestAppearance createCharacterRequestAppearance = (CreateCharacterRequestAppearance) o;
    return Objects.equals(this.bodyType, createCharacterRequestAppearance.bodyType) &&
        Objects.equals(this.hairStyle, createCharacterRequestAppearance.hairStyle) &&
        Objects.equals(this.hairColor, createCharacterRequestAppearance.hairColor) &&
        Objects.equals(this.skinTone, createCharacterRequestAppearance.skinTone);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bodyType, hairStyle, hairColor, skinTone);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateCharacterRequestAppearance {\n");
    sb.append("    bodyType: ").append(toIndentedString(bodyType)).append("\n");
    sb.append("    hairStyle: ").append(toIndentedString(hairStyle)).append("\n");
    sb.append("    hairColor: ").append(toIndentedString(hairColor)).append("\n");
    sb.append("    skinTone: ").append(toIndentedString(skinTone)).append("\n");
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

