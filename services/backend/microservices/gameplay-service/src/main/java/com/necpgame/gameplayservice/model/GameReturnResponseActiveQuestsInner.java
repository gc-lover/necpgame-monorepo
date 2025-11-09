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
 * GameReturnResponseActiveQuestsInner
 */

@JsonTypeName("GameReturnResponse_activeQuests_inner")

public class GameReturnResponseActiveQuestsInner {

  private @Nullable String questId;

  private @Nullable Integer progress;

  public GameReturnResponseActiveQuestsInner questId(@Nullable String questId) {
    this.questId = questId;
    return this;
  }

  /**
   * ID квеста
   * @return questId
   */
  
  @Schema(name = "questId", description = "ID квеста", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("questId")
  public @Nullable String getQuestId() {
    return questId;
  }

  public void setQuestId(@Nullable String questId) {
    this.questId = questId;
  }

  public GameReturnResponseActiveQuestsInner progress(@Nullable Integer progress) {
    this.progress = progress;
    return this;
  }

  /**
   * Прогресс квеста в процентах
   * minimum: 0
   * maximum: 100
   * @return progress
   */
  @Min(value = 0) @Max(value = 100) 
  @Schema(name = "progress", description = "Прогресс квеста в процентах", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progress")
  public @Nullable Integer getProgress() {
    return progress;
  }

  public void setProgress(@Nullable Integer progress) {
    this.progress = progress;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameReturnResponseActiveQuestsInner gameReturnResponseActiveQuestsInner = (GameReturnResponseActiveQuestsInner) o;
    return Objects.equals(this.questId, gameReturnResponseActiveQuestsInner.questId) &&
        Objects.equals(this.progress, gameReturnResponseActiveQuestsInner.progress);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, progress);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GameReturnResponseActiveQuestsInner {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    progress: ").append(toIndentedString(progress)).append("\n");
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

