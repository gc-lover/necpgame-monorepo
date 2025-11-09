package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.narrativeservice.model.MainStoryProgressKeyChoicesInner;
import java.math.BigDecimal;
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
 * MainStoryProgress
 */


public class MainStoryProgress {

  private @Nullable String characterId;

  /**
   * Gets or Sets currentAct
   */
  public enum CurrentActEnum {
    PROLOGUE("prologue"),
    
    ACT1("act1"),
    
    ACT2("act2"),
    
    ACT3("act3"),
    
    EPILOGUE("epilogue");

    private final String value;

    CurrentActEnum(String value) {
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
    public static CurrentActEnum fromValue(String value) {
      for (CurrentActEnum b : CurrentActEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable CurrentActEnum currentAct;

  private @Nullable String currentQuestId;

  /**
   * Gets or Sets lifePath
   */
  public enum LifePathEnum {
    CORPO("corpo"),
    
    STREET_KID("street_kid"),
    
    NOMAD("nomad");

    private final String value;

    LifePathEnum(String value) {
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
    public static LifePathEnum fromValue(String value) {
      for (LifePathEnum b : LifePathEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable LifePathEnum lifePath;

  @Valid
  private List<@Valid MainStoryProgressKeyChoicesInner> keyChoices = new ArrayList<>();

  private @Nullable BigDecimal humanityScore;

  @Valid
  private List<String> endingsUnlocked = new ArrayList<>();

  public MainStoryProgress characterId(@Nullable String characterId) {
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

  public MainStoryProgress currentAct(@Nullable CurrentActEnum currentAct) {
    this.currentAct = currentAct;
    return this;
  }

  /**
   * Get currentAct
   * @return currentAct
   */
  
  @Schema(name = "current_act", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_act")
  public @Nullable CurrentActEnum getCurrentAct() {
    return currentAct;
  }

  public void setCurrentAct(@Nullable CurrentActEnum currentAct) {
    this.currentAct = currentAct;
  }

  public MainStoryProgress currentQuestId(@Nullable String currentQuestId) {
    this.currentQuestId = currentQuestId;
    return this;
  }

  /**
   * Get currentQuestId
   * @return currentQuestId
   */
  
  @Schema(name = "current_quest_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_quest_id")
  public @Nullable String getCurrentQuestId() {
    return currentQuestId;
  }

  public void setCurrentQuestId(@Nullable String currentQuestId) {
    this.currentQuestId = currentQuestId;
  }

  public MainStoryProgress lifePath(@Nullable LifePathEnum lifePath) {
    this.lifePath = lifePath;
    return this;
  }

  /**
   * Get lifePath
   * @return lifePath
   */
  
  @Schema(name = "life_path", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("life_path")
  public @Nullable LifePathEnum getLifePath() {
    return lifePath;
  }

  public void setLifePath(@Nullable LifePathEnum lifePath) {
    this.lifePath = lifePath;
  }

  public MainStoryProgress keyChoices(List<@Valid MainStoryProgressKeyChoicesInner> keyChoices) {
    this.keyChoices = keyChoices;
    return this;
  }

  public MainStoryProgress addKeyChoicesItem(MainStoryProgressKeyChoicesInner keyChoicesItem) {
    if (this.keyChoices == null) {
      this.keyChoices = new ArrayList<>();
    }
    this.keyChoices.add(keyChoicesItem);
    return this;
  }

  /**
   * Get keyChoices
   * @return keyChoices
   */
  @Valid 
  @Schema(name = "key_choices", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("key_choices")
  public List<@Valid MainStoryProgressKeyChoicesInner> getKeyChoices() {
    return keyChoices;
  }

  public void setKeyChoices(List<@Valid MainStoryProgressKeyChoicesInner> keyChoices) {
    this.keyChoices = keyChoices;
  }

  public MainStoryProgress humanityScore(@Nullable BigDecimal humanityScore) {
    this.humanityScore = humanityScore;
    return this;
  }

  /**
   * Get humanityScore
   * @return humanityScore
   */
  @Valid 
  @Schema(name = "humanity_score", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("humanity_score")
  public @Nullable BigDecimal getHumanityScore() {
    return humanityScore;
  }

  public void setHumanityScore(@Nullable BigDecimal humanityScore) {
    this.humanityScore = humanityScore;
  }

  public MainStoryProgress endingsUnlocked(List<String> endingsUnlocked) {
    this.endingsUnlocked = endingsUnlocked;
    return this;
  }

  public MainStoryProgress addEndingsUnlockedItem(String endingsUnlockedItem) {
    if (this.endingsUnlocked == null) {
      this.endingsUnlocked = new ArrayList<>();
    }
    this.endingsUnlocked.add(endingsUnlockedItem);
    return this;
  }

  /**
   * Get endingsUnlocked
   * @return endingsUnlocked
   */
  
  @Schema(name = "endings_unlocked", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endings_unlocked")
  public List<String> getEndingsUnlocked() {
    return endingsUnlocked;
  }

  public void setEndingsUnlocked(List<String> endingsUnlocked) {
    this.endingsUnlocked = endingsUnlocked;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MainStoryProgress mainStoryProgress = (MainStoryProgress) o;
    return Objects.equals(this.characterId, mainStoryProgress.characterId) &&
        Objects.equals(this.currentAct, mainStoryProgress.currentAct) &&
        Objects.equals(this.currentQuestId, mainStoryProgress.currentQuestId) &&
        Objects.equals(this.lifePath, mainStoryProgress.lifePath) &&
        Objects.equals(this.keyChoices, mainStoryProgress.keyChoices) &&
        Objects.equals(this.humanityScore, mainStoryProgress.humanityScore) &&
        Objects.equals(this.endingsUnlocked, mainStoryProgress.endingsUnlocked);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, currentAct, currentQuestId, lifePath, keyChoices, humanityScore, endingsUnlocked);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MainStoryProgress {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    currentAct: ").append(toIndentedString(currentAct)).append("\n");
    sb.append("    currentQuestId: ").append(toIndentedString(currentQuestId)).append("\n");
    sb.append("    lifePath: ").append(toIndentedString(lifePath)).append("\n");
    sb.append("    keyChoices: ").append(toIndentedString(keyChoices)).append("\n");
    sb.append("    humanityScore: ").append(toIndentedString(humanityScore)).append("\n");
    sb.append("    endingsUnlocked: ").append(toIndentedString(endingsUnlocked)).append("\n");
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

