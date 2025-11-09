package com.necpgame.gameplayservice.model;

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
 * CompanionDetailBonding
 */

@JsonTypeName("CompanionDetail_bonding")

public class CompanionDetailBonding {

  private @Nullable Integer level;

  private @Nullable String emotionState;

  public CompanionDetailBonding level(@Nullable Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * @return level
   */
  
  @Schema(name = "level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level")
  public @Nullable Integer getLevel() {
    return level;
  }

  public void setLevel(@Nullable Integer level) {
    this.level = level;
  }

  public CompanionDetailBonding emotionState(@Nullable String emotionState) {
    this.emotionState = emotionState;
    return this;
  }

  /**
   * Get emotionState
   * @return emotionState
   */
  
  @Schema(name = "emotionState", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("emotionState")
  public @Nullable String getEmotionState() {
    return emotionState;
  }

  public void setEmotionState(@Nullable String emotionState) {
    this.emotionState = emotionState;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompanionDetailBonding companionDetailBonding = (CompanionDetailBonding) o;
    return Objects.equals(this.level, companionDetailBonding.level) &&
        Objects.equals(this.emotionState, companionDetailBonding.emotionState);
  }

  @Override
  public int hashCode() {
    return Objects.hash(level, emotionState);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompanionDetailBonding {\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    emotionState: ").append(toIndentedString(emotionState)).append("\n");
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

