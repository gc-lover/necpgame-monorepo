package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GameCharacterAppearance
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GameCharacterAppearance {

  private Integer height;

  /**
   * Телосложение
   */
  public enum BodyTypeEnum {
    THIN("thin"),
    
    NORMAL("normal"),
    
    MUSCULAR("muscular"),
    
    LARGE("large");

    private final String value;

    BodyTypeEnum(String value) {
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
    public static BodyTypeEnum fromValue(String value) {
      for (BodyTypeEnum b : BodyTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private BodyTypeEnum bodyType;

  private String hairColor;

  private String eyeColor;

  private String skinColor;

  private JsonNullable<@Size(max = 500) String> distinctiveFeatures = JsonNullable.<String>undefined();

  public GameCharacterAppearance() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GameCharacterAppearance(Integer height, BodyTypeEnum bodyType, String hairColor, String eyeColor, String skinColor) {
    this.height = height;
    this.bodyType = bodyType;
    this.hairColor = hairColor;
    this.eyeColor = eyeColor;
    this.skinColor = skinColor;
  }

  public GameCharacterAppearance height(Integer height) {
    this.height = height;
    return this;
  }

  /**
   * Рост в см
   * minimum: 150
   * maximum: 220
   * @return height
   */
  @NotNull @Min(value = 150) @Max(value = 220) 
  @Schema(name = "height", example = "180", description = "Рост в см", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("height")
  public Integer getHeight() {
    return height;
  }

  public void setHeight(Integer height) {
    this.height = height;
  }

  public GameCharacterAppearance bodyType(BodyTypeEnum bodyType) {
    this.bodyType = bodyType;
    return this;
  }

  /**
   * Телосложение
   * @return bodyType
   */
  @NotNull 
  @Schema(name = "body_type", example = "normal", description = "Телосложение", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("body_type")
  public BodyTypeEnum getBodyType() {
    return bodyType;
  }

  public void setBodyType(BodyTypeEnum bodyType) {
    this.bodyType = bodyType;
  }

  public GameCharacterAppearance hairColor(String hairColor) {
    this.hairColor = hairColor;
    return this;
  }

  /**
   * Цвет волос
   * @return hairColor
   */
  @NotNull 
  @Schema(name = "hair_color", example = "black", description = "Цвет волос", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("hair_color")
  public String getHairColor() {
    return hairColor;
  }

  public void setHairColor(String hairColor) {
    this.hairColor = hairColor;
  }

  public GameCharacterAppearance eyeColor(String eyeColor) {
    this.eyeColor = eyeColor;
    return this;
  }

  /**
   * Цвет глаз
   * @return eyeColor
   */
  @NotNull 
  @Schema(name = "eye_color", example = "brown", description = "Цвет глаз", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("eye_color")
  public String getEyeColor() {
    return eyeColor;
  }

  public void setEyeColor(String eyeColor) {
    this.eyeColor = eyeColor;
  }

  public GameCharacterAppearance skinColor(String skinColor) {
    this.skinColor = skinColor;
    return this;
  }

  /**
   * Цвет кожи
   * @return skinColor
   */
  @NotNull 
  @Schema(name = "skin_color", example = "light", description = "Цвет кожи", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("skin_color")
  public String getSkinColor() {
    return skinColor;
  }

  public void setSkinColor(String skinColor) {
    this.skinColor = skinColor;
  }

  public GameCharacterAppearance distinctiveFeatures(String distinctiveFeatures) {
    this.distinctiveFeatures = JsonNullable.of(distinctiveFeatures);
    return this;
  }

  /**
   * Особые приметы (максимум 500 символов)
   * @return distinctiveFeatures
   */
  @Size(max = 500) 
  @Schema(name = "distinctive_features", example = "Scar on left cheek", description = "Особые приметы (максимум 500 символов)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("distinctive_features")
  public JsonNullable<@Size(max = 500) String> getDistinctiveFeatures() {
    return distinctiveFeatures;
  }

  public void setDistinctiveFeatures(JsonNullable<String> distinctiveFeatures) {
    this.distinctiveFeatures = distinctiveFeatures;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameCharacterAppearance gameCharacterAppearance = (GameCharacterAppearance) o;
    return Objects.equals(this.height, gameCharacterAppearance.height) &&
        Objects.equals(this.bodyType, gameCharacterAppearance.bodyType) &&
        Objects.equals(this.hairColor, gameCharacterAppearance.hairColor) &&
        Objects.equals(this.eyeColor, gameCharacterAppearance.eyeColor) &&
        Objects.equals(this.skinColor, gameCharacterAppearance.skinColor) &&
        equalsNullable(this.distinctiveFeatures, gameCharacterAppearance.distinctiveFeatures);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(height, bodyType, hairColor, eyeColor, skinColor, hashCodeNullable(distinctiveFeatures));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GameCharacterAppearance {\n");
    sb.append("    height: ").append(toIndentedString(height)).append("\n");
    sb.append("    bodyType: ").append(toIndentedString(bodyType)).append("\n");
    sb.append("    hairColor: ").append(toIndentedString(hairColor)).append("\n");
    sb.append("    eyeColor: ").append(toIndentedString(eyeColor)).append("\n");
    sb.append("    skinColor: ").append(toIndentedString(skinColor)).append("\n");
    sb.append("    distinctiveFeatures: ").append(toIndentedString(distinctiveFeatures)).append("\n");
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

