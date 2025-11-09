package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetAIDifficulty200ResponseDifficultyLevelsInner
 */

@JsonTypeName("getAIDifficulty_200_response_difficulty_levels_inner")

public class GetAIDifficulty200ResponseDifficultyLevelsInner {

  /**
   * Gets or Sets level
   */
  public enum LevelEnum {
    EASY("easy"),
    
    NORMAL("normal"),
    
    HARD("hard"),
    
    EXTREME("extreme"),
    
    DEADLY("deadly");

    private final String value;

    LevelEnum(String value) {
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
    public static LevelEnum fromValue(String value) {
      for (LevelEnum b : LevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable LevelEnum level;

  private @Nullable String description;

  private @Nullable BigDecimal rewardMultiplier;

  public GetAIDifficulty200ResponseDifficultyLevelsInner level(@Nullable LevelEnum level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * @return level
   */
  
  @Schema(name = "level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level")
  public @Nullable LevelEnum getLevel() {
    return level;
  }

  public void setLevel(@Nullable LevelEnum level) {
    this.level = level;
  }

  public GetAIDifficulty200ResponseDifficultyLevelsInner description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public GetAIDifficulty200ResponseDifficultyLevelsInner rewardMultiplier(@Nullable BigDecimal rewardMultiplier) {
    this.rewardMultiplier = rewardMultiplier;
    return this;
  }

  /**
   * Get rewardMultiplier
   * @return rewardMultiplier
   */
  @Valid 
  @Schema(name = "reward_multiplier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reward_multiplier")
  public @Nullable BigDecimal getRewardMultiplier() {
    return rewardMultiplier;
  }

  public void setRewardMultiplier(@Nullable BigDecimal rewardMultiplier) {
    this.rewardMultiplier = rewardMultiplier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAIDifficulty200ResponseDifficultyLevelsInner getAIDifficulty200ResponseDifficultyLevelsInner = (GetAIDifficulty200ResponseDifficultyLevelsInner) o;
    return Objects.equals(this.level, getAIDifficulty200ResponseDifficultyLevelsInner.level) &&
        Objects.equals(this.description, getAIDifficulty200ResponseDifficultyLevelsInner.description) &&
        Objects.equals(this.rewardMultiplier, getAIDifficulty200ResponseDifficultyLevelsInner.rewardMultiplier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(level, description, rewardMultiplier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAIDifficulty200ResponseDifficultyLevelsInner {\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    rewardMultiplier: ").append(toIndentedString(rewardMultiplier)).append("\n");
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

