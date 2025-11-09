package com.necpgame.socialservice.model;

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
 * RomanceNPC
 */


public class RomanceNPC {

  private @Nullable String npcId;

  private @Nullable String name;

  private @Nullable String region;

  private @Nullable Integer age;

  private @Nullable String gender;

  /**
   * Gets or Sets orientation
   */
  public enum OrientationEnum {
    HETERO("HETERO"),
    
    HOMO("HOMO"),
    
    BI("BI"),
    
    PAN("PAN");

    private final String value;

    OrientationEnum(String value) {
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
    public static OrientationEnum fromValue(String value) {
      for (OrientationEnum b : OrientationEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable OrientationEnum orientation;

  @Valid
  private List<String> personalityTraits = new ArrayList<>();

  @Valid
  private List<String> interests = new ArrayList<>();

  /**
   * Gets or Sets romanceDifficulty
   */
  public enum RomanceDifficultyEnum {
    EASY("EASY"),
    
    MEDIUM("MEDIUM"),
    
    HARD("HARD"),
    
    VERY_HARD("VERY_HARD");

    private final String value;

    RomanceDifficultyEnum(String value) {
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
    public static RomanceDifficultyEnum fromValue(String value) {
      for (RomanceDifficultyEnum b : RomanceDifficultyEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RomanceDifficultyEnum romanceDifficulty;

  private @Nullable Integer compatibilityScore;

  /**
   * Gets or Sets currentRelationshipStatus
   */
  public enum CurrentRelationshipStatusEnum {
    STRANGER("STRANGER"),
    
    ACQUAINTANCE("ACQUAINTANCE"),
    
    FRIEND("FRIEND"),
    
    DATING("DATING"),
    
    COMMITTED("COMMITTED");

    private final String value;

    CurrentRelationshipStatusEnum(String value) {
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
    public static CurrentRelationshipStatusEnum fromValue(String value) {
      for (CurrentRelationshipStatusEnum b : CurrentRelationshipStatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      return null;
    }
  }

  private JsonNullable<CurrentRelationshipStatusEnum> currentRelationshipStatus = JsonNullable.<CurrentRelationshipStatusEnum>undefined();

  public RomanceNPC npcId(@Nullable String npcId) {
    this.npcId = npcId;
    return this;
  }

  /**
   * Get npcId
   * @return npcId
   */
  
  @Schema(name = "npc_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npc_id")
  public @Nullable String getNpcId() {
    return npcId;
  }

  public void setNpcId(@Nullable String npcId) {
    this.npcId = npcId;
  }

  public RomanceNPC name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public RomanceNPC region(@Nullable String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  
  @Schema(name = "region", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("region")
  public @Nullable String getRegion() {
    return region;
  }

  public void setRegion(@Nullable String region) {
    this.region = region;
  }

  public RomanceNPC age(@Nullable Integer age) {
    this.age = age;
    return this;
  }

  /**
   * Get age
   * @return age
   */
  
  @Schema(name = "age", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("age")
  public @Nullable Integer getAge() {
    return age;
  }

  public void setAge(@Nullable Integer age) {
    this.age = age;
  }

  public RomanceNPC gender(@Nullable String gender) {
    this.gender = gender;
    return this;
  }

  /**
   * Get gender
   * @return gender
   */
  
  @Schema(name = "gender", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("gender")
  public @Nullable String getGender() {
    return gender;
  }

  public void setGender(@Nullable String gender) {
    this.gender = gender;
  }

  public RomanceNPC orientation(@Nullable OrientationEnum orientation) {
    this.orientation = orientation;
    return this;
  }

  /**
   * Get orientation
   * @return orientation
   */
  
  @Schema(name = "orientation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("orientation")
  public @Nullable OrientationEnum getOrientation() {
    return orientation;
  }

  public void setOrientation(@Nullable OrientationEnum orientation) {
    this.orientation = orientation;
  }

  public RomanceNPC personalityTraits(List<String> personalityTraits) {
    this.personalityTraits = personalityTraits;
    return this;
  }

  public RomanceNPC addPersonalityTraitsItem(String personalityTraitsItem) {
    if (this.personalityTraits == null) {
      this.personalityTraits = new ArrayList<>();
    }
    this.personalityTraits.add(personalityTraitsItem);
    return this;
  }

  /**
   * Get personalityTraits
   * @return personalityTraits
   */
  
  @Schema(name = "personality_traits", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("personality_traits")
  public List<String> getPersonalityTraits() {
    return personalityTraits;
  }

  public void setPersonalityTraits(List<String> personalityTraits) {
    this.personalityTraits = personalityTraits;
  }

  public RomanceNPC interests(List<String> interests) {
    this.interests = interests;
    return this;
  }

  public RomanceNPC addInterestsItem(String interestsItem) {
    if (this.interests == null) {
      this.interests = new ArrayList<>();
    }
    this.interests.add(interestsItem);
    return this;
  }

  /**
   * Get interests
   * @return interests
   */
  
  @Schema(name = "interests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("interests")
  public List<String> getInterests() {
    return interests;
  }

  public void setInterests(List<String> interests) {
    this.interests = interests;
  }

  public RomanceNPC romanceDifficulty(@Nullable RomanceDifficultyEnum romanceDifficulty) {
    this.romanceDifficulty = romanceDifficulty;
    return this;
  }

  /**
   * Get romanceDifficulty
   * @return romanceDifficulty
   */
  
  @Schema(name = "romance_difficulty", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("romance_difficulty")
  public @Nullable RomanceDifficultyEnum getRomanceDifficulty() {
    return romanceDifficulty;
  }

  public void setRomanceDifficulty(@Nullable RomanceDifficultyEnum romanceDifficulty) {
    this.romanceDifficulty = romanceDifficulty;
  }

  public RomanceNPC compatibilityScore(@Nullable Integer compatibilityScore) {
    this.compatibilityScore = compatibilityScore;
    return this;
  }

  /**
   * С вашим персонажем (0-100)
   * @return compatibilityScore
   */
  
  @Schema(name = "compatibility_score", description = "С вашим персонажем (0-100)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("compatibility_score")
  public @Nullable Integer getCompatibilityScore() {
    return compatibilityScore;
  }

  public void setCompatibilityScore(@Nullable Integer compatibilityScore) {
    this.compatibilityScore = compatibilityScore;
  }

  public RomanceNPC currentRelationshipStatus(CurrentRelationshipStatusEnum currentRelationshipStatus) {
    this.currentRelationshipStatus = JsonNullable.of(currentRelationshipStatus);
    return this;
  }

  /**
   * Get currentRelationshipStatus
   * @return currentRelationshipStatus
   */
  
  @Schema(name = "current_relationship_status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_relationship_status")
  public JsonNullable<CurrentRelationshipStatusEnum> getCurrentRelationshipStatus() {
    return currentRelationshipStatus;
  }

  public void setCurrentRelationshipStatus(JsonNullable<CurrentRelationshipStatusEnum> currentRelationshipStatus) {
    this.currentRelationshipStatus = currentRelationshipStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RomanceNPC romanceNPC = (RomanceNPC) o;
    return Objects.equals(this.npcId, romanceNPC.npcId) &&
        Objects.equals(this.name, romanceNPC.name) &&
        Objects.equals(this.region, romanceNPC.region) &&
        Objects.equals(this.age, romanceNPC.age) &&
        Objects.equals(this.gender, romanceNPC.gender) &&
        Objects.equals(this.orientation, romanceNPC.orientation) &&
        Objects.equals(this.personalityTraits, romanceNPC.personalityTraits) &&
        Objects.equals(this.interests, romanceNPC.interests) &&
        Objects.equals(this.romanceDifficulty, romanceNPC.romanceDifficulty) &&
        Objects.equals(this.compatibilityScore, romanceNPC.compatibilityScore) &&
        equalsNullable(this.currentRelationshipStatus, romanceNPC.currentRelationshipStatus);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(npcId, name, region, age, gender, orientation, personalityTraits, interests, romanceDifficulty, compatibilityScore, hashCodeNullable(currentRelationshipStatus));
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
    sb.append("class RomanceNPC {\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    age: ").append(toIndentedString(age)).append("\n");
    sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
    sb.append("    orientation: ").append(toIndentedString(orientation)).append("\n");
    sb.append("    personalityTraits: ").append(toIndentedString(personalityTraits)).append("\n");
    sb.append("    interests: ").append(toIndentedString(interests)).append("\n");
    sb.append("    romanceDifficulty: ").append(toIndentedString(romanceDifficulty)).append("\n");
    sb.append("    compatibilityScore: ").append(toIndentedString(compatibilityScore)).append("\n");
    sb.append("    currentRelationshipStatus: ").append(toIndentedString(currentRelationshipStatus)).append("\n");
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

