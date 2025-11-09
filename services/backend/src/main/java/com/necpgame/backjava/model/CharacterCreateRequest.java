package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.CharacterAppearance;
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
 * CharacterCreateRequest
 */


public class CharacterCreateRequest {

  private String name;

  /**
   * Gets or Sets origin
   */
  public enum OriginEnum {
    CORPO("CORPO"),
    
    STREETKID("STREETKID"),
    
    NOMAD("NOMAD"),
    
    CUSTOM("CUSTOM");

    private final String value;

    OriginEnum(String value) {
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
    public static OriginEnum fromValue(String value) {
      for (OriginEnum b : OriginEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private OriginEnum origin;

  /**
   * Gets or Sets characterClass
   */
  public enum CharacterClassEnum {
    SOLO("SOLO"),
    
    NETRUNNER("NETRUNNER"),
    
    TECHIE("TECHIE"),
    
    FIXER("FIXER"),
    
    NOMAD("NOMAD");

    private final String value;

    CharacterClassEnum(String value) {
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
    public static CharacterClassEnum fromValue(String value) {
      for (CharacterClassEnum b : CharacterClassEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private CharacterClassEnum characterClass;

  private CharacterAppearance appearance;

  private @Nullable String seed;

  private Boolean acceptTerms = true;

  @Valid
  private List<String> tutorialPreferences = new ArrayList<>();

  public CharacterCreateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterCreateRequest(String name, OriginEnum origin, CharacterClassEnum characterClass, CharacterAppearance appearance) {
    this.name = name;
    this.origin = origin;
    this.characterClass = characterClass;
    this.appearance = appearance;
  }

  public CharacterCreateRequest name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull @Pattern(regexp = "^[a-zA-Z0-9 \\\\-]+$") @Size(min = 3, max = 20) 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CharacterCreateRequest origin(OriginEnum origin) {
    this.origin = origin;
    return this;
  }

  /**
   * Get origin
   * @return origin
   */
  @NotNull 
  @Schema(name = "origin", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("origin")
  public OriginEnum getOrigin() {
    return origin;
  }

  public void setOrigin(OriginEnum origin) {
    this.origin = origin;
  }

  public CharacterCreateRequest characterClass(CharacterClassEnum characterClass) {
    this.characterClass = characterClass;
    return this;
  }

  /**
   * Get characterClass
   * @return characterClass
   */
  @NotNull 
  @Schema(name = "characterClass", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characterClass")
  public CharacterClassEnum getCharacterClass() {
    return characterClass;
  }

  public void setCharacterClass(CharacterClassEnum characterClass) {
    this.characterClass = characterClass;
  }

  public CharacterCreateRequest appearance(CharacterAppearance appearance) {
    this.appearance = appearance;
    return this;
  }

  /**
   * Get appearance
   * @return appearance
   */
  @NotNull @Valid 
  @Schema(name = "appearance", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("appearance")
  public CharacterAppearance getAppearance() {
    return appearance;
  }

  public void setAppearance(CharacterAppearance appearance) {
    this.appearance = appearance;
  }

  public CharacterCreateRequest seed(@Nullable String seed) {
    this.seed = seed;
    return this;
  }

  /**
   * Get seed
   * @return seed
   */
  @Size(min = 8, max = 32) 
  @Schema(name = "seed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("seed")
  public @Nullable String getSeed() {
    return seed;
  }

  public void setSeed(@Nullable String seed) {
    this.seed = seed;
  }

  public CharacterCreateRequest acceptTerms(Boolean acceptTerms) {
    this.acceptTerms = acceptTerms;
    return this;
  }

  /**
   * Get acceptTerms
   * @return acceptTerms
   */
  
  @Schema(name = "acceptTerms", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("acceptTerms")
  public Boolean getAcceptTerms() {
    return acceptTerms;
  }

  public void setAcceptTerms(Boolean acceptTerms) {
    this.acceptTerms = acceptTerms;
  }

  public CharacterCreateRequest tutorialPreferences(List<String> tutorialPreferences) {
    this.tutorialPreferences = tutorialPreferences;
    return this;
  }

  public CharacterCreateRequest addTutorialPreferencesItem(String tutorialPreferencesItem) {
    if (this.tutorialPreferences == null) {
      this.tutorialPreferences = new ArrayList<>();
    }
    this.tutorialPreferences.add(tutorialPreferencesItem);
    return this;
  }

  /**
   * Get tutorialPreferences
   * @return tutorialPreferences
   */
  
  @Schema(name = "tutorialPreferences", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tutorialPreferences")
  public List<String> getTutorialPreferences() {
    return tutorialPreferences;
  }

  public void setTutorialPreferences(List<String> tutorialPreferences) {
    this.tutorialPreferences = tutorialPreferences;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterCreateRequest characterCreateRequest = (CharacterCreateRequest) o;
    return Objects.equals(this.name, characterCreateRequest.name) &&
        Objects.equals(this.origin, characterCreateRequest.origin) &&
        Objects.equals(this.characterClass, characterCreateRequest.characterClass) &&
        Objects.equals(this.appearance, characterCreateRequest.appearance) &&
        Objects.equals(this.seed, characterCreateRequest.seed) &&
        Objects.equals(this.acceptTerms, characterCreateRequest.acceptTerms) &&
        Objects.equals(this.tutorialPreferences, characterCreateRequest.tutorialPreferences);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, origin, characterClass, appearance, seed, acceptTerms, tutorialPreferences);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterCreateRequest {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    origin: ").append(toIndentedString(origin)).append("\n");
    sb.append("    characterClass: ").append(toIndentedString(characterClass)).append("\n");
    sb.append("    appearance: ").append(toIndentedString(appearance)).append("\n");
    sb.append("    seed: ").append(toIndentedString(seed)).append("\n");
    sb.append("    acceptTerms: ").append(toIndentedString(acceptTerms)).append("\n");
    sb.append("    tutorialPreferences: ").append(toIndentedString(tutorialPreferences)).append("\n");
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

