package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.AchievementDefinition;
import com.necpgame.backjava.model.PlayerAchievementProgress;
import java.time.OffsetDateTime;
import java.util.Arrays;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
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
 * PlayerAchievement
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class PlayerAchievement {

  private @Nullable AchievementDefinition achievement;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    LOCKED("LOCKED"),
    
    IN_PROGRESS("IN_PROGRESS"),
    
    UNLOCKED("UNLOCKED");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  private @Nullable PlayerAchievementProgress progress;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> unlockedAt = JsonNullable.<OffsetDateTime>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  public PlayerAchievement achievement(@Nullable AchievementDefinition achievement) {
    this.achievement = achievement;
    return this;
  }

  /**
   * Get achievement
   * @return achievement
   */
  @Valid 
  @Schema(name = "achievement", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("achievement")
  public @Nullable AchievementDefinition getAchievement() {
    return achievement;
  }

  public void setAchievement(@Nullable AchievementDefinition achievement) {
    this.achievement = achievement;
  }

  public PlayerAchievement status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public PlayerAchievement progress(@Nullable PlayerAchievementProgress progress) {
    this.progress = progress;
    return this;
  }

  /**
   * Get progress
   * @return progress
   */
  @Valid 
  @Schema(name = "progress", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progress")
  public @Nullable PlayerAchievementProgress getProgress() {
    return progress;
  }

  public void setProgress(@Nullable PlayerAchievementProgress progress) {
    this.progress = progress;
  }

  public PlayerAchievement unlockedAt(OffsetDateTime unlockedAt) {
    this.unlockedAt = JsonNullable.of(unlockedAt);
    return this;
  }

  /**
   * Get unlockedAt
   * @return unlockedAt
   */
  @Valid 
  @Schema(name = "unlocked_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlocked_at")
  public JsonNullable<OffsetDateTime> getUnlockedAt() {
    return unlockedAt;
  }

  public void setUnlockedAt(JsonNullable<OffsetDateTime> unlockedAt) {
    this.unlockedAt = unlockedAt;
  }

  public PlayerAchievement updatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @Valid 
  @Schema(name = "updated_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updated_at")
  public @Nullable OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerAchievement playerAchievement = (PlayerAchievement) o;
    return Objects.equals(this.achievement, playerAchievement.achievement) &&
        Objects.equals(this.status, playerAchievement.status) &&
        Objects.equals(this.progress, playerAchievement.progress) &&
        equalsNullable(this.unlockedAt, playerAchievement.unlockedAt) &&
        Objects.equals(this.updatedAt, playerAchievement.updatedAt);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(achievement, status, progress, hashCodeNullable(unlockedAt), updatedAt);
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
    sb.append("class PlayerAchievement {\n");
    sb.append("    achievement: ").append(toIndentedString(achievement)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    progress: ").append(toIndentedString(progress)).append("\n");
    sb.append("    unlockedAt: ").append(toIndentedString(unlockedAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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

