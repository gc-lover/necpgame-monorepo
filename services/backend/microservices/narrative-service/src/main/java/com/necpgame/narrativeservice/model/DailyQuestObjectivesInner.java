package com.necpgame.narrativeservice.model;

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
 * DailyQuestObjectivesInner
 */

@JsonTypeName("DailyQuest_objectives_inner")

public class DailyQuestObjectivesInner {

  private @Nullable String description;

  private @Nullable Integer target;

  public DailyQuestObjectivesInner description(@Nullable String description) {
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

  public DailyQuestObjectivesInner target(@Nullable Integer target) {
    this.target = target;
    return this;
  }

  /**
   * Get target
   * @return target
   */
  
  @Schema(name = "target", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target")
  public @Nullable Integer getTarget() {
    return target;
  }

  public void setTarget(@Nullable Integer target) {
    this.target = target;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DailyQuestObjectivesInner dailyQuestObjectivesInner = (DailyQuestObjectivesInner) o;
    return Objects.equals(this.description, dailyQuestObjectivesInner.description) &&
        Objects.equals(this.target, dailyQuestObjectivesInner.target);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, target);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DailyQuestObjectivesInner {\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    target: ").append(toIndentedString(target)).append("\n");
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

