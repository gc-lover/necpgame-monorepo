package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * CharacterAppearance
 */


public class CharacterAppearance {

  /**
   * Gets or Sets bodyType
   */
  public enum BodyTypeEnum {
    SLIM("slim"),
    
    ATHLETIC("athletic"),
    
    HEAVY("heavy"),
    
    CYBERNETIC("cybernetic");

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

  private String skinTone;

  private String hairStyle;

  private @Nullable String hairColor;

  private String eyeColor;

  @Valid
  private List<String> tattoos = new ArrayList<>();

  @Valid
  private List<String> scars = new ArrayList<>();

  @Valid
  private List<String> implantsVisible = new ArrayList<>();

  private JsonNullable<String> makeupPreset = JsonNullable.<String>undefined();

  public CharacterAppearance() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterAppearance(BodyTypeEnum bodyType, String skinTone, String hairStyle, String eyeColor) {
    this.bodyType = bodyType;
    this.skinTone = skinTone;
    this.hairStyle = hairStyle;
    this.eyeColor = eyeColor;
  }

  public CharacterAppearance bodyType(BodyTypeEnum bodyType) {
    this.bodyType = bodyType;
    return this;
  }

  /**
   * Get bodyType
   * @return bodyType
   */
  @NotNull 
  @Schema(name = "bodyType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("bodyType")
  public BodyTypeEnum getBodyType() {
    return bodyType;
  }

  public void setBodyType(BodyTypeEnum bodyType) {
    this.bodyType = bodyType;
  }

  public CharacterAppearance skinTone(String skinTone) {
    this.skinTone = skinTone;
    return this;
  }

  /**
   * Get skinTone
   * @return skinTone
   */
  @NotNull 
  @Schema(name = "skinTone", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("skinTone")
  public String getSkinTone() {
    return skinTone;
  }

  public void setSkinTone(String skinTone) {
    this.skinTone = skinTone;
  }

  public CharacterAppearance hairStyle(String hairStyle) {
    this.hairStyle = hairStyle;
    return this;
  }

  /**
   * Get hairStyle
   * @return hairStyle
   */
  @NotNull 
  @Schema(name = "hairStyle", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("hairStyle")
  public String getHairStyle() {
    return hairStyle;
  }

  public void setHairStyle(String hairStyle) {
    this.hairStyle = hairStyle;
  }

  public CharacterAppearance hairColor(@Nullable String hairColor) {
    this.hairColor = hairColor;
    return this;
  }

  /**
   * Get hairColor
   * @return hairColor
   */
  @Pattern(regexp = "^#(?:[0-9a-fA-F]{6})$") 
  @Schema(name = "hairColor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hairColor")
  public @Nullable String getHairColor() {
    return hairColor;
  }

  public void setHairColor(@Nullable String hairColor) {
    this.hairColor = hairColor;
  }

  public CharacterAppearance eyeColor(String eyeColor) {
    this.eyeColor = eyeColor;
    return this;
  }

  /**
   * Get eyeColor
   * @return eyeColor
   */
  @NotNull @Pattern(regexp = "^#(?:[0-9a-fA-F]{6})$") 
  @Schema(name = "eyeColor", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("eyeColor")
  public String getEyeColor() {
    return eyeColor;
  }

  public void setEyeColor(String eyeColor) {
    this.eyeColor = eyeColor;
  }

  public CharacterAppearance tattoos(List<String> tattoos) {
    this.tattoos = tattoos;
    return this;
  }

  public CharacterAppearance addTattoosItem(String tattoosItem) {
    if (this.tattoos == null) {
      this.tattoos = new ArrayList<>();
    }
    this.tattoos.add(tattoosItem);
    return this;
  }

  /**
   * Get tattoos
   * @return tattoos
   */
  
  @Schema(name = "tattoos", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tattoos")
  public List<String> getTattoos() {
    return tattoos;
  }

  public void setTattoos(List<String> tattoos) {
    this.tattoos = tattoos;
  }

  public CharacterAppearance scars(List<String> scars) {
    this.scars = scars;
    return this;
  }

  public CharacterAppearance addScarsItem(String scarsItem) {
    if (this.scars == null) {
      this.scars = new ArrayList<>();
    }
    this.scars.add(scarsItem);
    return this;
  }

  /**
   * Get scars
   * @return scars
   */
  
  @Schema(name = "scars", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scars")
  public List<String> getScars() {
    return scars;
  }

  public void setScars(List<String> scars) {
    this.scars = scars;
  }

  public CharacterAppearance implantsVisible(List<String> implantsVisible) {
    this.implantsVisible = implantsVisible;
    return this;
  }

  public CharacterAppearance addImplantsVisibleItem(String implantsVisibleItem) {
    if (this.implantsVisible == null) {
      this.implantsVisible = new ArrayList<>();
    }
    this.implantsVisible.add(implantsVisibleItem);
    return this;
  }

  /**
   * Get implantsVisible
   * @return implantsVisible
   */
  
  @Schema(name = "implantsVisible", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implantsVisible")
  public List<String> getImplantsVisible() {
    return implantsVisible;
  }

  public void setImplantsVisible(List<String> implantsVisible) {
    this.implantsVisible = implantsVisible;
  }

  public CharacterAppearance makeupPreset(String makeupPreset) {
    this.makeupPreset = JsonNullable.of(makeupPreset);
    return this;
  }

  /**
   * Get makeupPreset
   * @return makeupPreset
   */
  
  @Schema(name = "makeupPreset", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("makeupPreset")
  public JsonNullable<String> getMakeupPreset() {
    return makeupPreset;
  }

  public void setMakeupPreset(JsonNullable<String> makeupPreset) {
    this.makeupPreset = makeupPreset;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterAppearance characterAppearance = (CharacterAppearance) o;
    return Objects.equals(this.bodyType, characterAppearance.bodyType) &&
        Objects.equals(this.skinTone, characterAppearance.skinTone) &&
        Objects.equals(this.hairStyle, characterAppearance.hairStyle) &&
        Objects.equals(this.hairColor, characterAppearance.hairColor) &&
        Objects.equals(this.eyeColor, characterAppearance.eyeColor) &&
        Objects.equals(this.tattoos, characterAppearance.tattoos) &&
        Objects.equals(this.scars, characterAppearance.scars) &&
        Objects.equals(this.implantsVisible, characterAppearance.implantsVisible) &&
        equalsNullable(this.makeupPreset, characterAppearance.makeupPreset);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(bodyType, skinTone, hairStyle, hairColor, eyeColor, tattoos, scars, implantsVisible, hashCodeNullable(makeupPreset));
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
    sb.append("class CharacterAppearance {\n");
    sb.append("    bodyType: ").append(toIndentedString(bodyType)).append("\n");
    sb.append("    skinTone: ").append(toIndentedString(skinTone)).append("\n");
    sb.append("    hairStyle: ").append(toIndentedString(hairStyle)).append("\n");
    sb.append("    hairColor: ").append(toIndentedString(hairColor)).append("\n");
    sb.append("    eyeColor: ").append(toIndentedString(eyeColor)).append("\n");
    sb.append("    tattoos: ").append(toIndentedString(tattoos)).append("\n");
    sb.append("    scars: ").append(toIndentedString(scars)).append("\n");
    sb.append("    implantsVisible: ").append(toIndentedString(implantsVisible)).append("\n");
    sb.append("    makeupPreset: ").append(toIndentedString(makeupPreset)).append("\n");
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

