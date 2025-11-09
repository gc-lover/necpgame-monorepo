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
 * PlayerAchievementProgress
 */

@JsonTypeName("PlayerAchievement_progress")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class PlayerAchievementProgress {

  private @Nullable Integer current;

  private @Nullable Integer target;

  private @Nullable Float percentage;

  public PlayerAchievementProgress current(@Nullable Integer current) {
    this.current = current;
    return this;
  }

  /**
   * Get current
   * @return current
   */
  
  @Schema(name = "current", example = "50", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current")
  public @Nullable Integer getCurrent() {
    return current;
  }

  public void setCurrent(@Nullable Integer current) {
    this.current = current;
  }

  public PlayerAchievementProgress target(@Nullable Integer target) {
    this.target = target;
    return this;
  }

  /**
   * Get target
   * @return target
   */
  
  @Schema(name = "target", example = "100", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target")
  public @Nullable Integer getTarget() {
    return target;
  }

  public void setTarget(@Nullable Integer target) {
    this.target = target;
  }

  public PlayerAchievementProgress percentage(@Nullable Float percentage) {
    this.percentage = percentage;
    return this;
  }

  /**
   * Get percentage
   * @return percentage
   */
  
  @Schema(name = "percentage", example = "50.0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("percentage")
  public @Nullable Float getPercentage() {
    return percentage;
  }

  public void setPercentage(@Nullable Float percentage) {
    this.percentage = percentage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerAchievementProgress playerAchievementProgress = (PlayerAchievementProgress) o;
    return Objects.equals(this.current, playerAchievementProgress.current) &&
        Objects.equals(this.target, playerAchievementProgress.target) &&
        Objects.equals(this.percentage, playerAchievementProgress.percentage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(current, target, percentage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerAchievementProgress {\n");
    sb.append("    current: ").append(toIndentedString(current)).append("\n");
    sb.append("    target: ").append(toIndentedString(target)).append("\n");
    sb.append("    percentage: ").append(toIndentedString(percentage)).append("\n");
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

