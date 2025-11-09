package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.LevelUpResultUnlockedContent;
import com.necpgame.gameplayservice.model.LevelUpRewards;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LevelUpResult
 */


public class LevelUpResult {

  private @Nullable UUID characterId;

  private @Nullable Integer previousLevel;

  private @Nullable Integer newLevel;

  private @Nullable LevelUpRewards rewards;

  private @Nullable LevelUpResultUnlockedContent unlockedContent;

  public LevelUpResult characterId(@Nullable UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable UUID characterId) {
    this.characterId = characterId;
  }

  public LevelUpResult previousLevel(@Nullable Integer previousLevel) {
    this.previousLevel = previousLevel;
    return this;
  }

  /**
   * Get previousLevel
   * @return previousLevel
   */
  
  @Schema(name = "previous_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("previous_level")
  public @Nullable Integer getPreviousLevel() {
    return previousLevel;
  }

  public void setPreviousLevel(@Nullable Integer previousLevel) {
    this.previousLevel = previousLevel;
  }

  public LevelUpResult newLevel(@Nullable Integer newLevel) {
    this.newLevel = newLevel;
    return this;
  }

  /**
   * Get newLevel
   * @return newLevel
   */
  
  @Schema(name = "new_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("new_level")
  public @Nullable Integer getNewLevel() {
    return newLevel;
  }

  public void setNewLevel(@Nullable Integer newLevel) {
    this.newLevel = newLevel;
  }

  public LevelUpResult rewards(@Nullable LevelUpRewards rewards) {
    this.rewards = rewards;
    return this;
  }

  /**
   * Get rewards
   * @return rewards
   */
  @Valid 
  @Schema(name = "rewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards")
  public @Nullable LevelUpRewards getRewards() {
    return rewards;
  }

  public void setRewards(@Nullable LevelUpRewards rewards) {
    this.rewards = rewards;
  }

  public LevelUpResult unlockedContent(@Nullable LevelUpResultUnlockedContent unlockedContent) {
    this.unlockedContent = unlockedContent;
    return this;
  }

  /**
   * Get unlockedContent
   * @return unlockedContent
   */
  @Valid 
  @Schema(name = "unlocked_content", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlocked_content")
  public @Nullable LevelUpResultUnlockedContent getUnlockedContent() {
    return unlockedContent;
  }

  public void setUnlockedContent(@Nullable LevelUpResultUnlockedContent unlockedContent) {
    this.unlockedContent = unlockedContent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LevelUpResult levelUpResult = (LevelUpResult) o;
    return Objects.equals(this.characterId, levelUpResult.characterId) &&
        Objects.equals(this.previousLevel, levelUpResult.previousLevel) &&
        Objects.equals(this.newLevel, levelUpResult.newLevel) &&
        Objects.equals(this.rewards, levelUpResult.rewards) &&
        Objects.equals(this.unlockedContent, levelUpResult.unlockedContent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, previousLevel, newLevel, rewards, unlockedContent);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LevelUpResult {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    previousLevel: ").append(toIndentedString(previousLevel)).append("\n");
    sb.append("    newLevel: ").append(toIndentedString(newLevel)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    unlockedContent: ").append(toIndentedString(unlockedContent)).append("\n");
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

